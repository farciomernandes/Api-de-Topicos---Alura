package br.alura.forum.config.secutiry;

import br.alura.forum.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity //Avisa que e classe de segurança e bloqueia por default todos os endpoints
@Configuration //Avisa que a classe e de configuracao
@Profile(value={"prod", "test"}) //Avisa que essas configurações so são validas se o profile for "prod" produção
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

    @Autowired
    private AutenticacaoService autenticacaoService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Bean //Avisa que esse metodo devolve uma instancia de si mesmo
    protected AuthenticationManager authenticationManager() throws Exception{
        return super.authenticationManager();
    }

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
                .antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                .antMatchers(HttpMethod.POST, "/auth").permitAll()
                //hasRole: Só pode chamar essa requisicao quem tem o perfil "MODERADOR"
                .antMatchers(HttpMethod.DELETE, "/topicos/*").hasRole("MODERADOR")
                //Avisa que quaisquer outras requisições é preciso o usuário estar autenticado
                .anyRequest().authenticated()
                //desabilita a protecao contra o ataque hacker: cross site request forgery
                .and().csrf().disable()
                //Avisa ao spring para nao criar sessão, pois a politica de autenticacao sera STATELESS
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //Avisa pra rodar a classe 'AutenticacaoViaToken..' antes de seguir a execucao
                .and().addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, usuarioRepository), UsernamePasswordAuthenticationFilter.class);
    }

    //Configurações de recursos estáticos (requisições para arquivo js, css, imagens..)
    @Override
    public void configure(WebSecurity web) throws Exception {
        //Swagger tem que ser configurado aqui
        web.ignoring()
                .antMatchers("/**.html", "/v2/api-docs", "/webjars/**", "/configuration/**", "/swagger-resources/**");
    }

}
