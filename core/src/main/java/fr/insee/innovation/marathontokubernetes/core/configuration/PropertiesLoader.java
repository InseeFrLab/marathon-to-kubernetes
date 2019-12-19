package fr.insee.innovation.marathontokubernetes.core.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:common.properties")
public class PropertiesLoader {
}
