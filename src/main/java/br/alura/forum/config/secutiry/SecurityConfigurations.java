package br.alura.forum.config.secutiry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity //Avisa que e classe de segurança e bloqueia por default todos os endpoints
@Configuration //Avisa que a classe e de configuracao
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

    @Autowired
    private AutenticacaoService autenticacaoService;

    //Configurações de autenticação
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //Recebe do formulario do spring para autenticao.
        //Busca se o usuario existe
        //Verifica se a senha é a mesma
        auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());
    }

    //Configurações de Autorização (url, perfil de acesso)
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Essa configuração, ela autoriza metodos request
        //do verbo 'GET' para o endpoint 'topicos e topicos/*'
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET,"/topicos").permitAll()
                .antMatchers(HttpMethod.GET, "/topicos/*").permitAll()
                .antMatchers(HttpMethod.POST, "/auth").permitAll()
                //Avisa que quaisquer outras requisições é preciso o usuário estar autenticado
                .anyRequest().authenticated()
                //desabilita a protecao contra o ataque hacker: cross site request forgery
                .and().csrf().disable()
                //Avisa ao spring para nao criar sessão, pois a politica de autenticacao sera STATELESS
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    //Configurações de recursos estáticos (requisições para arquivo js, css, imagens..)
    @Override
    public void configure(WebSecurity web) throws Exception {
    }

}
