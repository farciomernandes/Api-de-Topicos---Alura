package br.alura.forum.config.secutiry;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//OncePerRequestFilter = Diz que essa classe é um filtro para ser adicionado
// no 'config' do SpringSecutiry em SecurityConfiguration.
//Essa classe vai funcionar como um middleware
public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {

    //Antes de ir para a requisicao ele verifica se o token do cabeçario ta OK e autentica o usuario
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = recuperarToken(request);
        System.out.println("SACA SÓ:  " + token);

        //linha que manda o spring seguir normalmente para a requisicao que chamou
        filterChain.doFilter(request, response);
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
