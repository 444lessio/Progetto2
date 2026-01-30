package com.hotel_alduina.hotel_management.config;

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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                //Riorse accessibili a tutti
                .requestMatchers("/css/**", "/js/**").permitAll()
                //Pagine pubbliche come la home, il login e la pagina di registrazione
                .requestMatchers("/", "/login", "/registration").permitAll()
                //Accesso esclusivo al gestore
                .requestMatchers("/admin/**").hasRole("GESTORE")
                //Accesso esclusivo allo staff
                .requestMatchers("/staff/**").hasRole("PERSONALE")
                //Accesso clienti
                .requestMatchers("/prenota/**","/stay/**").hasRole("CLIENTE")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
            .loginPage("/login")
            .successHandler((request, response, authentication) -> {
                // Controllo il ruolo e reindirizzo
                var authorities = authentication.getAuthorities();
                if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_GESTORE"))) {
                    response.sendRedirect("/admin/dashboard");
                } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_PERSONALE"))) {
                    response.sendRedirect("/staff/dashboard"); // sostituisci con la home dello staff
                } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"))) {
                    response.sendRedirect("/client/dashboard");
                } else {
                    response.sendRedirect("/"); // fallback
                }
            })
            .permitAll()
        )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );
        return http.build();
    }

    //BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}
