package fr.insee.innovation.marathontokubernetes.api.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

    @Value("${openapi.serverurls:}")
    private List<String> serverUrls;


    @Bean
    public OpenAPI customOpenAPI() {
        OpenAPI openAPI = new OpenAPI();
        if (serverUrls != null) {
            serverUrls.stream().forEach(serverUrl -> {
                openAPI.addServersItem(new Server().url(serverUrl));
            });
        }
        return openAPI
                .info(new Info().title("Marathon to Kubernetes API")
                        .contact(new Contact().name("InseeFRLab").email("innovation@insee.fr").url("https://github.com/InseeFrLab/marathon-to-kubernetes"))
                        .description("A brief api to convert marathon contracts to k8s"));
    }
}
