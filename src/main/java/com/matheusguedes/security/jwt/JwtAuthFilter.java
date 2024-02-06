package com.matheusguedes.security.jwt;

import com.matheusguedes.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer")) {
            var token = authorization.split(" ")[1];
            if (!jwtService.isTokenExpired(token)) {
                var user = usuarioService.loadUserByUsername(jwtService.obterUsername(token));
                var usuarioToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                usuarioToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usuarioToken);
            }
        }
        filterChain.doFilter(request, response);
    }

}