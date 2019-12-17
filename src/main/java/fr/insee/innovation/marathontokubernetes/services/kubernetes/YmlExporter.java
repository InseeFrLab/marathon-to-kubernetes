package fr.insee.innovation.marathontokubernetes.services.kubernetes;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.client.internal.SerializationUtils;
import org.springframework.stereotype.Service;

@Service
public class YmlExporter {

    public String exportToYml(HasMetadata toExport) {
        try {
            return SerializationUtils.dumpAsYaml(toExport);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }
}
