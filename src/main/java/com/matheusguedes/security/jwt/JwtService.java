package com.matheusguedes.security.jwt;

import com.matheusguedes.domain.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

@Service
public class JwtService {

    @Value("${security.jwt.expiration-time}")
    private String minutosParaExpirarToken;

    @Value("${security.jwt.key}")
    private String chaveAssinatura;

    public String gerarToken(Usuario usuario){
        long expiracaoString = Long.parseLong(minutosParaExpirarToken);
        LocalDateTime dateTimeExpiracao = LocalDateTime.now().plusMinutes(expiracaoString);
        Date data = Date.from(dateTimeExpiracao.atZone(ZoneId.systemDefault()).toInstant());

        HashMap<String, Object> claims = new HashMap<>();
        claims.put("username", usuario.getUsername());
        claims.put("admin", usuario.isAdmin());

        return Jwts.builder()
                .setExpiration(data)
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, chaveAssinatura)
                .compact();
    }

    private Claims obterClaims(String token) throws ExpiredJwtException {
        return Jwts.parser()
                .setSigningKey(chaveAssinatura)
                .parseClaimsJws(token)
                .getBody();
    }

    public String obterUsername(String token) throws ExpiredJwtException {
        return obterClaims(token).get("username").toString();
    }

    public boolean isTokenExpired(String token){
        try {
            Claims claims = obterClaims(token);
            Date dateExpiracao = claims.getExpiration();
            LocalDateTime dateTimeExpiracao = dateExpiracao.toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDateTime();
            return !LocalDateTime.now().isAfter(dateTimeExpiracao);
        } catch (Exception ex){
            return false;
        }
    }

}