package fr.insee.innovation.marathontokubernetes.core.services.marathon;

import java.io.IOException;
import java.io.InputStream;

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
    public App importMarathonApp(InputStream input) {
        try {
            return mapper.readValue(input, App.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
