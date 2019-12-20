package fr.insee.innovation.marathontokubernetes.api.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfiguration {

    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Marathon to Kubernetes API")
                        .contact(new Contact().name("InseeFRLab").email("innovation@insee.fr").url("https://github.com/InseeFrLab/marathon-to-kubernetes"))
                        .description("A brief api to convert marathon contracts to k8s"));
    }
}
