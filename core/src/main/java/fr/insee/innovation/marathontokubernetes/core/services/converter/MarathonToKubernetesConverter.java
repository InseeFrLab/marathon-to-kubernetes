package fr.insee.innovation.marathontokubernetes.core.services.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.fabric8.kubernetes.api.model.ConfigMapBuilder;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.ContainerBuilder;
import io.fabric8.kubernetes.api.model.ExecAction;
import io.fabric8.kubernetes.api.model.HTTPGetAction;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.Probe;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.QuantityBuilder;
import io.fabric8.kubernetes.api.model.ResourceRequirementsBuilder;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.api.model.ServicePort;
import io.fabric8.kubernetes.api.model.ServicePortBuilder;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.api.model.extensions.HTTPIngressPathBuilder;
import io.fabric8.kubernetes.api.model.extensions.IngressBackendBuilder;
import io.fabric8.kubernetes.api.model.extensions.IngressBuilder;
import io.fabric8.kubernetes.api.model.extensions.IngressRule;
import io.fabric8.kubernetes.api.model.extensions.IngressRuleBuilder;
import mesosphere.marathon.client.model.v2.App;
import mesosphere.marathon.client.model.v2.Command;
import mesosphere.marathon.client.model.v2.HealthCheck;
import mesosphere.marathon.client.model.v2.Port;

@Service
public class MarathonToKubernetesConverter {

    private static final Pattern PATTERN_HAPROXY_VHOST = Pattern.compile("HAPROXY_([0-9]*)_VHOST");

    @Value("${kubernetes.ingress.class}")
    public String ingressClass;

    public List<HasMetadata> convert(App appToConvert, String name) {
        List<HasMetadata> components = new ArrayList<>();
        components.add(getDeployment(appToConvert, name));
        components.add(getService(appToConvert, name));
        if (appToConvert.getLabels() != null && appToConvert.getLabels().size() > 0) {
            components.add(getIngress(appToConvert, name));
        }

        if (appToConvert.getEnv() != null && appToConvert.getEnv().size() > 0) {
            components.add(getConfigMap(appToConvert, name));
        }
        return components;
    }

    private HasMetadata getConfigMap(App appToConvert, String name) {
        Map<String, String> env = new HashMap<>();
        appToConvert.getEnv().entrySet().forEach(entry -> {
            env.put(entry.getKey(), String.valueOf(entry.getValue()));
        });
        return new ConfigMapBuilder().withNewMetadata().withName(name + "-config").endMetadata().withData(env).build();
    }

    private HasMetadata getIngress(App appToConvert, String name) {
        Collection<Port> portMappings = getPortMappings(appToConvert);
        List<IngressRule> rules = appToConvert.getLabels().entrySet().stream()
                .filter(entry -> PATTERN_HAPROXY_VHOST.matcher(entry.getKey()).matches()).map(entry -> {
                    Matcher matcher = PATTERN_HAPROXY_VHOST.matcher(entry.getKey());
                    matcher.find();
                    int portIndex = Integer.parseInt(matcher.group(1));
                    int port = ((Port) portMappings.toArray()[portIndex]).getContainerPort();
                    return new IngressRuleBuilder()
                            .withHost(
                                    entry.getValue())
                            .withNewHttp()
                            .withPaths(
                                    new HTTPIngressPathBuilder().withPath("/").withBackend(new IngressBackendBuilder()
                                            .withNewServicePort(port).withNewServiceName(name).build()).build())
                            .endHttp().build();
                }).collect(Collectors.toList());
        return new IngressBuilder().withNewMetadata().withName(name)
                .withAnnotations(Map.of("kubernetes.io/ingress.class", ingressClass)).endMetadata().withNewSpec()
                .withRules(rules).endSpec().build();
    }

    private Collection<Port> getPortMappings(App appToConvert) {
        Collection<Port> portMappings = appToConvert.getContainer().getDocker().getPortMappings() != null
                ? appToConvert.getContainer().getDocker().getPortMappings()
                : appToConvert.getContainer().getPortMappings();
        return portMappings;
    }

    private HasMetadata getService(App appToConvert, String name) {
        Collection<Port> portMappings = getPortMappings(appToConvert);
        List<ServicePort> servicePorts = portMappings.stream()
                .map(portMapping -> new ServicePortBuilder()
                        .withName(portMapping.getName() == null ? name + "-" + UUID.randomUUID().toString()
                                : portMapping.getName())
                        .withTargetPort(new IntOrString(portMapping.getContainerPort()))
                        .withPort(portMapping.getContainerPort()).build())
                .collect(Collectors.toList());

        return new ServiceBuilder().withNewMetadata().withName(name).endMetadata().withNewSpec().withPorts(servicePorts)
                .withSelector(Map.of("app", name)).endSpec().build();
    }

    private HasMetadata getDeployment(App appToConvert, String name) {
        ContainerBuilder containerBuilder = new ContainerBuilder();
        if (appToConvert.getEnv() != null && appToConvert.getEnv().size() > 0) {
            containerBuilder.addNewEnvFrom().withNewConfigMapRef().withName(name + "-config").endConfigMapRef()
                    .endEnvFrom();
        }
        if (appToConvert.getContainer().getDocker().isPrivileged()) {
            containerBuilder.editOrNewSecurityContext().withPrivileged(true).endSecurityContext();
        }
        Map<String, Quantity> resourcesRequests = new HashMap<>();
        resourcesRequests.put("memory",
                new QuantityBuilder().withNewAmount(appToConvert.getMem().intValue() + "M").build());
        resourcesRequests.put("cpu",
                new QuantityBuilder().withNewAmount((appToConvert.getCpus() * 1000) + "m").build());
        containerBuilder.withResources(new ResourceRequirementsBuilder().withRequests(resourcesRequests).build());
        Container container = containerBuilder.withName(name)
                .withImage(appToConvert.getContainer().getDocker().getImage())
                .withLivenessProbe(getHealthcheck(appToConvert)).build();

        return new DeploymentBuilder().withNewMetadata().withName(name).endMetadata().withNewSpec()
                .withReplicas(appToConvert.getInstances() == null ? 1 : appToConvert.getInstances()).withNewSelector()
                .withMatchLabels(Map.of("app", name)).endSelector().withNewTemplate().withNewMetadata()
                .withLabels(Map.of("app", name)).endMetadata().withNewSpec().withContainers(List.of(container))
                .endSpec().endTemplate().endSpec().build();
    }

    private Probe getHealthcheck(App appToConvert) {
        if (appToConvert.getHealthChecks() != null && appToConvert.getHealthChecks().size() > 0) {
            HealthCheck healthCheck = appToConvert.getHealthChecks().get(0);
            Probe probe = new Probe();
            if (healthCheck.getCommand() == null) {
                String path = healthCheck.getPath();
                IntOrString port = new IntOrString(healthCheck.getPortIndex());
                String scheme = healthCheck.getProtocol();
                HTTPGetAction httpGet = new HTTPGetAction();
                httpGet.setPath(path);
                httpGet.setPort(port);
                httpGet.setScheme(scheme);
                probe.setHttpGet(httpGet);
            } else {
                ExecAction exec = new ExecAction();
                List<String> commandList = new ArrayList<>();
                commandList.add(healthCheck.getCommand().getValue());
                exec.setCommand(commandList);
                probe.setExec(exec);
            }
            probe.setInitialDelaySeconds(healthCheck.getGracePeriodSeconds());
            probe.setPeriodSeconds(healthCheck.getIntervalSeconds());
            probe.setTimeoutSeconds(healthCheck.getTimeoutSeconds());
            return probe;
        }
        return null;
    }
}
