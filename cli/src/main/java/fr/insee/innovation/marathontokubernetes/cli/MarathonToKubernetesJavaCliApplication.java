package fr.insee.innovation.marathontokubernetes.cli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "fr.insee.innovation.marathontokubernetes")
public class MarathonToKubernetesJavaCliApplication {

	public static void main(String[] args) {
		System.exit(SpringApplication.exit(SpringApplication.run(MarathonToKubernetesJavaCliApplication.class, args)));
	}

}
