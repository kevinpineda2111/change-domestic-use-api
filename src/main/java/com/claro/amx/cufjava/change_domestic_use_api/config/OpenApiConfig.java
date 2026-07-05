package com.claro.amx.cufjava.change_domestic_use_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${swagger-servers.http}")
    private String urlHttp;

    @Value("${swagger-servers.https}")
    private String urlHttps;


    @Bean
    public OpenAPI errorManagerAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url(urlHttp))
                .addServersItem(new Server().url(urlHttps))
                .info(new Info().title("Change Nim Api")
                        .description("All resources available of Error Manager")
                        .version("v1.0.0"));
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
