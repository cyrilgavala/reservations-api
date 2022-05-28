package sk.cyrilgavala.reservationsApi.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sk.cyrilgavala.reservationsApi.web.interceptor.RequestLoggingInterceptor;

import javax.inject.Inject;
import java.util.List;
import java.util.TimeZone;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

	private final transient RequestLoggingInterceptor requestLoggingInterceptor;

	@Inject
	public WebConfiguration(RequestLoggingInterceptor requestLoggingInterceptor) {
		this.requestLoggingInterceptor = requestLoggingInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		WebMvcConfigurer.super.addInterceptors(registry);
		registry.addInterceptor(requestLoggingInterceptor).addPathPatterns("/api/reservation/*", "/api/reservation");
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		WebMvcConfigurer.super.configureMessageConverters(converters);

		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		builder.modules(new JavaTimeModule(), new Jdk8Module());
		builder.timeZone(TimeZone.getTimeZone("UTC"));
		builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(builder.build());
	}

}
