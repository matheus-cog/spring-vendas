package com.matheusguedes.config;

import com.matheusguedes.api.dto.*;
import com.matheusguedes.domain.entity.Cliente;
import com.matheusguedes.domain.entity.Produto;
import com.matheusguedes.domain.entity.Usuario;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .ignoredParameterTypes(
                        TokenDTO.class, PedidoDTO.class, ItemPedidoDTO.class,
                        InformacoesPedidoDTO.class, InformacoesItensPedidoDTO.class,
                        CredenciaisDTO.class, AtualizacaoStatusPedidoDTO.class,
                        Cliente.class, Produto.class, Usuario.class
                )
                .select()
                    .apis(RequestHandlerSelectors.basePackage("com.matheusguedes.api.controller"))
                    .paths(PathSelectors.any())
                    .build()
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Collections.singletonList(apiKey()))
                .apiInfo(apiInfo());
    }

    private Contact contact() {
        return new Contact("Matheus Guedes", "https://matheusguedes.com", "contato@matheusguedes.com");
    }

    public ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.any())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        var authorizationScope = new AuthorizationScope("global", "accessAll");
        var scopes = new AuthorizationScope[]{authorizationScope};
        var reference = new SecurityReference("JWT", scopes);
        var auths = new ArrayList<SecurityReference>();
        auths.add(reference);
        return auths;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Vendas API")
                .description("Serviço RESTful gerenciador de vendas.")
                .version("1.0.0")
                .contact(contact())
                .build();
    }

}