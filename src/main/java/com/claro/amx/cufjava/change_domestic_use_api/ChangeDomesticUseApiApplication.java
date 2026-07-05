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
//Sirve para documentar la API, con: titulo, version y descripcion, que salen desde application.yaml
//Se están usando placeholders "${info.app.name}", que significa: "Buscá estos valores en la
//configuración del proyecto (src/main/resources/aplication.yaml
@ComponentScan(basePackages = {"com.claro"})
//Le dice a Spring: buscá componentes dentro del paquete com.claro
//Componentes pueden ser Clases como: @Controller @Service @Repository @Component
@EnableJpaRepositories(basePackages = {"com.claro"})
//Le dice a Spring: "Buscá repositories JPA dentro de com.claro". Y un repository
//es una clase/interfaz encargada de interactuar con base de datos.
@EntityScan(basePackages = {"com.claro"})
//Le indica a Spring dónde buscar clases Entity. Una Entity representa una Tabla o estructura de DB.
//Una entidad suele ser una clase de Java que representa datos de base de datos.
@EnableFeignClients
//Esto solo sirve para habilitar el uso de esta Api a Clientes Feign(apis externas usando interfaces)
@EnableAutoConfiguration
//esta anotación le dice a Spring Boot: "Configurá automáticamente
//lo que puedas según las dependencias del proyecto."
@SpringBootApplication(scanBasePackages = "com.claro")
public class ChangeDomesticUseApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChangeDomesticUseApiApplication.class, args);
    }

}
