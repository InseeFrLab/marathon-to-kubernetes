package fr.insee.innovation.marathontokubernetes.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "fr.insee.innovation.marathontokubernetes")
public class MarathonToKubernetesApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarathonToKubernetesApiApplication.class, args);
	}


}
