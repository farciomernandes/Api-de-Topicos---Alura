package br.alura.forum.config.secutiry;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity //Avisa que e classe de segurança e bloqueia por default todos os endpoints
@Configuration //Avisa que a classe e de configuracao
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

    //Configurações de autenticação
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    }

    //Configurações de Autorização (url, perfil de acesso)
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Essa configuração, ela autoriza metodos request
        //do verbo 'GET' para o endpoint 'topicos e topicos/*'
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET,"/topicos").permitAll()
                .antMatchers(HttpMethod.GET, "/topicos/*").permitAll()
                //Avisa que quaisquer outras requisições é preciso o usuário estar autenticado
                .anyRequest().authenticated()
                //O Spring abre um formulario de login caso queira uma requisicao autenticada
                .and().formLogin();
    }

    //Configurações de recursos estáticos (requisições para arquivo js, css, imagens..)
    @Override
    public void configure(WebSecurity web) throws Exception {
    }
}
