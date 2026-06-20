package com.fitproject.bff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Clase principal de la aplicación Backend For Frontend (BFF) para FitProject.
 * 
 * Esta aplicación actúa como intermediario entre los clientes móviles y los microservicios
 * backend, proporcionando una API optimizada para el frontend móvil.
 * 
 * @EnableFeignClients habilita la configuración de clientes Feign para la comunicación
 * con otros microservicios.
 */
@SpringBootApplication
@EnableFeignClients
public class BffApplication {

    public static void main(String[] args) {
        SpringApplication.run(BffApplication.class, args);
    }
}
