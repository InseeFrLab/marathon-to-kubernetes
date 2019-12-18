package fr.insee.innovation.marathontokubernetes.services.marathon;

import mesosphere.marathon.client.model.v2.App;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;

@SpringBootTest
public class MarathonImporterTest {

    @Autowired
    private MarathonImporter importer;

    @ParameterizedTest
    @ValueSource(strings = {"/marathon/drawio.json"})
    public void shouldImportDrawIo(String location) {
        InputStream input = getClass().getResourceAsStream(location);
        App app = importer.importMarathonApp(input);
        Assertions.assertNotNull(app);
        Assertions.assertEquals(2, app.getCpus());
    }
}
