package fr.insee.innovation.marathontokubernetes.core.controller;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import fr.insee.innovation.marathontokubernetes.core.services.converter.MarathonToKubernetesConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import fr.insee.innovation.marathontokubernetes.core.services.kubernetes.YmlExporter;
import fr.insee.innovation.marathontokubernetes.core.services.marathon.MarathonImporter;
import io.fabric8.kubernetes.api.model.HasMetadata;
import mesosphere.marathon.client.model.v2.App;

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
        List<App> apps = importer.importMarathonApp(input);
        App app = apps.get(0);
        List<HasMetadata> listHasMetadata = converter.convert(app, name == null ? sanitizeName(app.getId()) : name);
        return listHasMetadata.stream().map(hasMetadata -> exporter.exportToYml(hasMetadata)).collect(Collectors.joining());
    }

    private String sanitizeName(String name) {
        return name.replaceAll("\\.","-");
    }
}
