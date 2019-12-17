package fr.insee.innovation.marathontokubernetes.controller;

import fr.insee.innovation.marathontokubernetes.MarathonToKubernetesApplication;
import fr.insee.innovation.marathontokubernetes.services.converter.MarathonToKubernetesConverter;
import fr.insee.innovation.marathontokubernetes.services.kubernetes.YmlExporter;
import fr.insee.innovation.marathontokubernetes.services.marathon.MarathonImporter;
import io.fabric8.kubernetes.api.model.HasMetadata;
import mesosphere.marathon.client.model.v2.App;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ConvertController {

    @Autowired
    private YmlExporter exporter;

    @Autowired
    private MarathonImporter importer;

    @Autowired
    private MarathonToKubernetesConverter converter;

    public String convert(InputStream input) {
        return convert(input, null);
    }

    public String convert(InputStream input, String name) {
        App app = importer.importMarathonApp(input);
        List<HasMetadata> listHasMetadata = converter.convert(app, name == null ? sanitizeName(app.getId()) : name);
        return listHasMetadata.stream().map(hasMetadata -> exporter.exportToYml(hasMetadata)).collect(Collectors.joining());
    }

    private String sanitizeName(String name) {
        return name.replaceAll("\\.","-");
    }
}
