package com.grupoSI.crudApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Encripta as senhas usando BCrypt antes de salvar no banco
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Permite acesso público para CSS, JS, Imagens, tela de registro e login
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/register", "/login").permitAll()
                        // Qualquer outra rota o user precisa estar autenticado
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // Define a rota de login
                        .defaultSuccessUrl("/funcionarios", true) // Para onde o user vai após login
                        .usernameParameter("email") // Garante que o Spring vai usar o campo 'email' como o login
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout") // Para onde vai o user depois de sair
                        .permitAll()
                );

        return http.build();
    }
}