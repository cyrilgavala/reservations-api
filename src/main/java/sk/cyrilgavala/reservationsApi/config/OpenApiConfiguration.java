package sk.cyrilgavala.reservationsApi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

	@Bean
	public OpenAPI springShopOpenAPI() {
		return new OpenAPI()
			.info(new Info().title("Reservations API")
				.description("Simple API for implementing basic reservation system.")
				.version("v1.0.0"));
	}
}
