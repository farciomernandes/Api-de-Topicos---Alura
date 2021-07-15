package br.alura.forum.config.secutiry;

import br.alura.forum.model.Usuario;
import br.alura.forum.repository.UsuarioRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

//OncePerRequestFilter = Diz que essa classe é um filtro para ser adicionado
// no 'config' do SpringSecutiry em SecurityConfiguration.
//Essa classe vai funcionar como um middleware
public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {


    private TokenService tokenService;
    private UsuarioRepository usuarioRepository;

    public AutenticacaoViaTokenFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    //Antes de ir para a requisicao ele verifica se o token do cabeçario ta OK e autentica o usuario
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = recuperarToken(request);
        boolean valido = tokenService.isTokenValido(token);
        System.out.println(valido);
        if(valido){
            autenticarCliente(token);
        }
        //linha que manda o spring seguir normalmente para a requisicao que chamou
        filterChain.doFilter(request, response);
    }
    private void autenticarCliente(String token) {
        //Retira o id do usuario atraves do token
        Long idUsuario = tokenService.getIdUsuario(token);
        //Busca o usuario pelo id recuperado
        Usuario usuario = usuarioRepository.findById(idUsuario).get();
        //Cria variavel de autenticacao do tipo que o Spring aceita
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
        //Autentica o usuario para essa requisicao
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String recuperarToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if(token == null || token.isEmpty() || !token.startsWith("Bearer ")){
            return null;
        }

        //Pega do 7 ate o fim
        return token.substring(7, token.length());

    }
}
