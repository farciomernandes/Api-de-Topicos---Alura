package br.alura.forum.config.secutiry;

import br.alura.forum.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    @Value("${forum.jwt.expiration}") //Puxa o valor do application.properties;
    private String expiration;

    @Value("${forum.jwt.secret}") //Puxa o valor do application.properties;
    private String secret;

    public String gerarToken(Authentication authentication) {
        Usuario logado = (Usuario) authentication.getPrincipal();
        Date hoje = new Date();
        Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));

        return  Jwts.builder()
                //Qual aplicacao esta criando esse token?
            .setIssuer("API do Fórum Marcio Spring")
                //Para quem é esse token? Converta em string
            .setSubject(logado.getId().toString())
                //Quando esse token foi criado?
            .setIssuedAt(hoje)
                //Qual o tempo de expiração do Token? (Valor somado de hoje com o tempo limite
            .setExpiration(dataExpiracao)
                //secret é a 'senha' para criar o token
                //SignatureAlgorithm.ES256 é um padrao hmac sha256
            .signWith(SignatureAlgorithm.HS256, secret)
                //Transforma tudo isso em uma string
            .compact();
    }
}
