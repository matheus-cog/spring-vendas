package com.matheusguedes.config;

import com.matheusguedes.security.jwt.JwtAuthFilter;
import com.matheusguedes.security.jwt.JwtService;
import com.matheusguedes.service.implementation.UsuarioServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioServiceImplementation usuarioService;

    @Bean
    public OncePerRequestFilter jwtFilter(){
        return new JwtAuthFilter(jwtService, usuarioService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                    .antMatchers("/clientes/**").hasAnyRole("USER", "ADMIN")
                    .antMatchers("/pedidos/**").hasAnyRole("USER", "ADMIN")
                    .antMatchers("/produtos/**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.POST, "/usuarios/**").permitAll()
                    .antMatchers("/auth/**").permitAll()
                    // Permitir todas as rotas do Swagger
                    .antMatchers("/v2/api-docs").permitAll()
                    .antMatchers("/configuration/ui").permitAll()
                    .antMatchers("/swagger-resources/**").permitAll()
                    .antMatchers("/configuration/security").permitAll()
                    .antMatchers("/swagger-ui.html").permitAll()
                    .antMatchers("/swagger-ui/**").permitAll()
                    .antMatchers("/webjars/**").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Remoção de sessões
                .and()
                    .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
        ;
    }

}
