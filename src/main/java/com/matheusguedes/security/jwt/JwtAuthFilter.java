package com.matheusguedes.security.jwt;


import com.matheusguedes.service.implementation.UsuarioServiceImplementation;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private JwtService jwtService;
    private UsuarioServiceImplementation usuarioService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if(authorization != null && authorization.startsWith("Bearer")){
            String token = authorization.split(" ")[1];
            if(!jwtService.isTokenExpired(token)){
                String username = jwtService.obterUsername(token);
                UserDetails user = usuarioService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken usuarioToken = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities()
                );
                usuarioToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usuarioToken);
            }
        }
        filterChain.doFilter(request, response);
    }

}
