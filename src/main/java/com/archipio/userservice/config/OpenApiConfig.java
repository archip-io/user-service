package com.archipio.userservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info =
        @Info(
            title = "User Service",
            description =
                "User Service - это микросервис, который отвечает за управление аккаунтами пользователей и предоставляет\n"
                    + "        другим микросервисам их учётные данные",
            version = "0.0.0"),
    servers = {
      @Server(description = "Server URL in Development environment", url = "http://localhost:18082")
    })
@Configuration
public class OpenApiConfig {}
