package fr.insee.innovation.marathontokubernetes.services.converter;

import fr.insee.innovation.marathontokubernetes.controller.ConvertController;
import fr.insee.innovation.marathontokubernetes.services.marathon.MarathonImporter;
import io.fabric8.kubernetes.api.model.HasMetadata;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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


    @Test
    public void shouldConvertWithoutErrors() {
        InputStream input = getClass().getResourceAsStream("/marathon/drawio.json");
        List<HasMetadata> kubContract = converter.convert(importer.importMarathonApp(input),"draw-io");
        Assertions.assertNotNull(kubContract);
        Assertions.assertEquals(3, kubContract.size());
    }
}
