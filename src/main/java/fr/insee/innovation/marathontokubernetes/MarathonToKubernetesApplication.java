package fr.insee.innovation.marathontokubernetes;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import fr.insee.innovation.marathontokubernetes.controller.ConvertController;
import fr.insee.innovation.marathontokubernetes.services.converter.MarathonToKubernetesConverter;
import fr.insee.innovation.marathontokubernetes.services.kubernetes.YmlExporter;
import fr.insee.innovation.marathontokubernetes.services.marathon.MarathonImporter;
import io.fabric8.kubernetes.api.model.HasMetadata;
import mesosphere.marathon.client.model.v2.App;

@SpringBootApplication
public class MarathonToKubernetesApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(MarathonToKubernetesApplication.class, args);
	}

	@Autowired
	ConvertController converter;

	@Override
	public void run(String... args) throws Exception {
		if (args.length > 0) {
			String filepath = args[0];
			String name = null;
			if (args.length > 1) {
				name = args[1];
			}
			InputStream input = new FileInputStream(filepath);
			String converted = converter.convert(input, name);
			FileOutputStream out = new FileOutputStream("./service.yml");
			out.write(converted.getBytes());
			out.close();
			System.out.println(converted);
		}
	}

}
