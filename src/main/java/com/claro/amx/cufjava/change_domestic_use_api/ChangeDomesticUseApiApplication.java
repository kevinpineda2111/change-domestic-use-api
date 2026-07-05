package com.claro.amx.cufjava.change_domestic_use_api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@OpenAPIDefinition(info = @Info(title = "${info.app.name}", version = "${info.app.version}", description = "${info.app.description}"))
@ComponentScan(basePackages = {"com.claro"})
@EnableJpaRepositories(basePackages = {"com.claro"})
@EntityScan(basePackages = {"com.claro"})
@EnableFeignClients
@EnableAutoConfiguration
@SpringBootApplication(scanBasePackages = "com.claro")
public class ChangeDomesticUseApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChangeDomesticUseApiApplication.class, args);
    }

}
