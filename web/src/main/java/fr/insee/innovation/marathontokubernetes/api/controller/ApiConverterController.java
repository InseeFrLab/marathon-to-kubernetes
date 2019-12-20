package fr.insee.innovation.marathontokubernetes.api.controller;

import fr.insee.innovation.marathontokubernetes.core.services.converter.MarathonToKubernetesConverter;
import fr.insee.innovation.marathontokubernetes.core.services.kubernetes.YmlExporter;
import io.fabric8.kubernetes.api.model.HasMetadata;
import mesosphere.marathon.client.model.v2.App;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ApiConverterController {

    @Autowired
    YmlExporter exporter;

    @Autowired
    MarathonToKubernetesConverter converter;

    @RequestMapping(value = "/convert", method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.TEXT_PLAIN_VALUE })
    @ResponseStatus(code = HttpStatus.OK)
    @ResponseBody
    public String convert(@RequestParam(value = "name", required = false) String name, @RequestBody App app) {
        List<HasMetadata> listHasMetadata = converter.convert(app,
                name == null ? app.getId().replaceAll("\\.", "-") : name);
        return listHasMetadata.stream().map(hasMetadata -> exporter.exportToYml(hasMetadata))
                .collect(Collectors.joining());
    }
}