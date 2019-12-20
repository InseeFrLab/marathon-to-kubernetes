package fr.insee.innovation.marathontokubernetes.core.services.converter;

import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import fr.insee.innovation.marathontokubernetes.core.services.marathon.MarathonImporter;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.apps.Deployment;

@SpringBootTest
public class MarathonToKubernetesConverterTest {

    @Autowired
    MarathonToKubernetesConverterImpl converter;

    @Autowired
    MarathonImporter importer;


    @ParameterizedTest
    @ValueSource(strings = {"/marathon/drawio.json","/marathon/adventofcodeleaderboard.json"})
    public void shouldConvertWithoutErrors( String location) {
        InputStream input = getClass().getResourceAsStream(location);
        List<HasMetadata> kubContract = converter.convert(importer.importMarathonApp(input),"draw-io");
        Assertions.assertNotNull(kubContract);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/marathon/adventofcodeleaderboard.json"})
    public void shouldConvertWithEnv( String location) {
        InputStream input = getClass().getResourceAsStream(location);
        List<HasMetadata> kubContract = converter.convert(importer.importMarathonApp(input),"adventofcode");
        Assertions.assertNotNull(kubContract);
        Assertions.assertEquals(4, kubContract.size());
        ConfigMap configMap = (ConfigMap) kubContract.stream().filter(e -> e instanceof ConfigMap).findFirst().get();
        Assertions.assertEquals(4, configMap.getData().size());
    }

    @ParameterizedTest
    @ValueSource(strings = {"/marathon/privileged.json"})
    public void shouldRunInPrivilegedMode(String location) {
        InputStream input = getClass().getResourceAsStream(location);
        List<HasMetadata> kubContract = converter.convert(importer.importMarathonApp(input),"privileged");
        Assertions.assertNotNull(kubContract);
        Deployment deployment = (Deployment) kubContract.stream().filter(e -> e instanceof Deployment).findFirst().get();
        Assertions.assertEquals(1,deployment.getSpec().getTemplate().getSpec().getContainers().size());
        Assertions.assertEquals(true,deployment.getSpec().getTemplate().getSpec().getContainers().get(0).getSecurityContext().getPrivileged());
    }
}
