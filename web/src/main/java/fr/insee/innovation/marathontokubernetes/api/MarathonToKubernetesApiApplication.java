package fr.insee.innovation.marathontokubernetes.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;

@SpringBootApplication(scanBasePackages = "fr.insee.innovation.marathontokubernetes")
public class MarathonToKubernetesApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarathonToKubernetesApiApplication.class, args);
	}


}
