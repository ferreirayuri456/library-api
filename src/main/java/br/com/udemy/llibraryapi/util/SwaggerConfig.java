package br.com.udemy.llibraryapi.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig implements WebMvcConfigurer {

	public static final String LOAN = "Empréstimo";
	public static final String BOOK = "Livros";

	@Value("${springfox.documentation.swagger.v2.home}")
	private String home;
	@Value("${springfox.documentation.swagger.v2.contactEmail}")
	private String email;

	@Autowired
	private Docket docket;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		docket.apiInfo(apiInfo());
		registry.addResourceHandler(home + "/***").addResourceLocations("classpath:/META-INF/resources");
		registry.addResourceHandler("/webjars").addResourceLocations("classpath:/META-INF/resources/webjars");
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Library API Rest")
				.description("Documentos da API de acesso aos endpoints de Library").version("Version API 1.0")
				.contact(new Contact("Yuri Ferreira", null, email)).build();
	}

	@Bean
	public Docket docket() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("br.com.udemy.llibraryapi.controller"))
				.paths(PathSelectors.any()).build().apiInfo(apiInfo());
	}

}
