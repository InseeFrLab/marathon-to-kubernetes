package fr.insee.innovation.marathontokubernetes.core.services.marathon;

import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import mesosphere.marathon.client.model.v2.App;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MarathonImporterTest {

    @Autowired
    private MarathonImporter importer;

    @ParameterizedTest
    @ValueSource(strings = {"/marathon/drawio.json"})
    public void shouldImportDrawIo(String location) {
        InputStream input = getClass().getResourceAsStream(location);
        List<App> apps = importer.importMarathonApp(input);
        assertThat(apps).isNotNull().hasSize(1);
        assertThat(apps).first().satisfies(app -> assertThat(app.getCpus() == 2));
    }
}
