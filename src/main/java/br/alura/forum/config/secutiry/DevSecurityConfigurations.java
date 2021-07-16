package br.alura.forum.config.secutiry;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity //Avisa que e classe de segurança e bloqueia por default todos os endpoints
@Configuration //Avisa que a classe e de configuracao
@Profile("dev") //Avisa que essas configurações so são validas se o profile for "dev" desenvolvimento
public class DevSecurityConfigurations extends WebSecurityConfigurerAdapter {

    //Configurações de Autorização (url, perfil de acesso)
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Essa configuração, ela autoriza metodos request
        //do verbo 'GET' para o endpoint 'topicos e topicos/*'
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET,"/**").permitAll()
                //desabilita a protecao contra o ataque hacker: cross site request forgery
                .and().csrf().disable();
    }

    //Configurações de recursos estáticos (requisições para arquivo js, css, imagens..)
    @Override
    public void configure(WebSecurity web) throws Exception {
        //Swagger tem que ser configurado aqui
        web.ignoring()
                .antMatchers("/**.html", "/v2/api-docs", "/webjars/**", "/configuration/**", "/swagger-resources/**");
    }

}
