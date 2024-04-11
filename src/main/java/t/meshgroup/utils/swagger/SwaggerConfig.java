package t.meshgroup.utils.swagger;

import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.info.Contact;
import io.swagger.oas.models.info.Info;
import io.swagger.oas.models.info.License;
import io.swagger.oas.models.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.List;

public class SwaggerConfig {
    @Bean
    public GroupedOpenApi publicUserApi() {
        return GroupedOpenApi.builder()
                .build();
    }

    @Bean
    public OpenAPI customOpenApi(@Value("${application-description}") String appDescription,
                                 @Value("${application-version}") String appVersion) {
        return new OpenAPI().info(new Info().title("Test Task MeshGroup API")
                        .version(appVersion)
                        .description(appDescription)
                        .license(new License().name("Apache 2.0")
                                .url("http://springdoc.org"))
                        .contact(new Contact().name("Oleg Koltsov")
                                .email("kolczov03@gmail.com")))
                .servers(List.of(new Server().url("http://localhost:8080")
                        .description("Dev Test Task MeshGroup")));
    }
}
