package kr.co.mountaincc.globals.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    @Value("${app.environment}")
    private String appEnvironment;

    @Value("${swagger.server.url}")
    private String swaggerServerUrl;

    @Bean
    public OpenAPI customOpenAPI() {

        boolean swaggerServerEnabled = false;

        if ("prod".equalsIgnoreCase(appEnvironment)) {
            swaggerServerEnabled = true;
        }

        OpenAPI openAPI = new OpenAPI()
                .info(new Info()
                        .title("MountainCrazyClub Swagger")
                        .version("1.0")
                        .description("API's from MCC Server"));

        if (swaggerServerEnabled && ! swaggerServerUrl.isEmpty()) {
            openAPI.servers(List.of(new Server()
                    .url(swaggerServerUrl)
                    .description("MCC Private Server")));
        }

        return openAPI;
    }

}
