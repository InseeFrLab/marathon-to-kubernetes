package fr.insee.innovation.marathontokubernetes.core.services.marathon;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mesosphere.marathon.client.model.v2.App;

@Service
public class MarathonImporter {

    @Autowired
    private ObjectMapper mapper;

    /**
     * Import the input as a Marathon app
     * @param input
     * @return the app or null if something went wront
     */
    public List<App> importMarathonApp(InputStream input) {
        try {
            return Arrays.asList(mapper.readValue(input, App[].class));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
