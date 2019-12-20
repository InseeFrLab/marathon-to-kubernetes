package fr.insee.innovation.marathontokubernetes.core.services.converter;

import io.fabric8.kubernetes.api.model.HasMetadata;
import mesosphere.marathon.client.model.v2.App;

import java.util.Arrays;
import java.util.List;

public interface MarathonToKubernetesConverter {

    public default List<HasMetadata> convert(App appToConvert, String name) {
        return convert(Arrays.asList(appToConvert), name);
    }

    public List<HasMetadata> convert(List<App> appsToConvert, String name);
}
