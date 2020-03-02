package fr.insee.innovation.marathontokubernetes.core.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomObjectMapper {


    @Bean
    public ObjectMapper getMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // This is needed because the Marathon library does not support newer properties like 'role'
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // This is needed so that a single app is read the same way as a JSON array of apps
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY , true);
        return objectMapper;
    }
}
