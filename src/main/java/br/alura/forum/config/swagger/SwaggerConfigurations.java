package br.alura.forum.config.swagger;

import br.alura.forum.model.Usuario;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;

@Configuration //configurações de como o swagger deve analisar a API
public class SwaggerConfigurations {

    @Bean
    public Docket forumApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                //basePackage: A partir de qual pasta deve fazer a documentação
                .apis(RequestHandlerSelectors.basePackage("br.alura.forum"))
                //paths: Quais endpoints e endereços ele deve fazer uma analise
                .paths(PathSelectors.ant(("/**")))
                .build()
                //Ignorar todas as url que trabalham com a classe Usuario
                //porque na classe tem a senha, e ela pode começar a aparecer
                .ignoredParameterTypes(Usuario.class)
                //adiciona um parametro a todos os endpoints
                .globalOperationParameters(
                        Arrays.asList(
                                new ParameterBuilder()
                                        //o nome do parametro e descrições
                                        .name("Authorization")
                                        .description("Header para Token JWT")
                                        .modelRef(new ModelRef("string"))
                                        .parameterType("header")
                                        .required(false)
                                        .build()));
    }
}
