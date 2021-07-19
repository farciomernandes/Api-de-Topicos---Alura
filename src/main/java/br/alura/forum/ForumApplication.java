package br.alura.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSpringDataWebSupport //Permite pegar dos parametros da requisicao, campos de paginacao e ordenacao 'TopicosController'
@EnableCaching //Habilita cache na aplicação
@EnableSwagger2 //Habilita o Swagger para documentação automatica da api
public class ForumApplication extends SpringBootServletInitializer {
    //O SpringBootServletInitializer: Torna possível configurar o Servlet dentro do servidor 'tomcat'

    public static void main(String[] args) {
        SpringApplication.run(ForumApplication.class, args);
    }

    //sobrescreve esse método
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ForumApplication.class);
    }
}
