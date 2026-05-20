package com.sigdea.config;

import com.sigdea.service.UserDetailsServiceImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {

        this.userDetailsService = userDetailsService;
    }

    // PASSWORD
    @Bean
    public PasswordEncoder passwordEncoder() {

        return NoOpPasswordEncoder.getInstance();
    }

    // AUTH
    @Bean

    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authProvider =
                new DaoAuthenticationProvider(userDetailsService);

        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
    // SECURITY
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {

        http

                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(

                                "/",
                                "/login",
                                "/recuperar",

                                "/css/**",
                                "/js/**",
                                "/imagen/**",

                                "/usuarios",
                                "/nuevoUsuario",
                                "/guardarUsuario",
                                "/editarUsuario/**",
                                "/actualizarUsuario",
                                "/eliminarUsuario/**"

                        ).permitAll()

                        .anyRequest().authenticated()
                )

                .authenticationProvider(authenticationProvider())

                .formLogin(form -> form

                        .loginPage("/login")

                        .loginProcessingUrl("/login")

                        .usernameParameter("username")

                        .passwordParameter("password")

                        .defaultSuccessUrl("/directora", true)

                        .permitAll()
                )

                .logout(logout -> logout

                        .logoutSuccessUrl("/login?logout")

                        .permitAll()
                );

        return http.build();
    }
}