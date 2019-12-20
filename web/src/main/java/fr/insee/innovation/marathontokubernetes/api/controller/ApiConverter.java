package fr.insee.innovation.marathontokubernetes.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.insee.innovation.marathontokubernetes.core.services.converter.MarathonToKubernetesConverter;
import fr.insee.innovation.marathontokubernetes.core.services.kubernetes.YmlExporter;
import io.fabric8.kubernetes.api.model.HasMetadata;
import mesosphere.marathon.client.model.v2.App;

@RestController
@RequestMapping("/api")
public class ApiConverter {

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