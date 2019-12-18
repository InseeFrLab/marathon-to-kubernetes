package fr.insee.innovation.marathontokubernetes.services.converter;

import fr.insee.innovation.marathontokubernetes.controller.ConvertController;
import fr.insee.innovation.marathontokubernetes.services.marathon.MarathonImporter;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.HasMetadata;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;
import java.util.List;

@SpringBootTest
public class MarathonToKubernetesConverterTest {

    @Autowired
    MarathonToKubernetesConverter converter;

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
}
