package com.together_english.deiz.config

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@SecurityScheme(
        name = "Authorization",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
class OpenApiConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
                .info(Info()
                        .title("Together English API")
                        .version("1.0.0")
                        .description("API documentation for Together English application")
                        .termsOfService("https://www.example.com/terms")
                        .license(
                                License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")
                        )
                        .contact(
                                Contact()
                                        .name("Support Team")
                                        .url("https://www.example.com/support")
                                        .email("kg64779@gmail.com")
                                )
                )
    }
}