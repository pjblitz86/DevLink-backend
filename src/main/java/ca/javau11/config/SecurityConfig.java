package ca.javau11.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import ca.javau11.utils.JwtFilter;
import io.swagger.v3.oas.models.PathItem.HttpMethod;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Disable CSRF for simplicity (enable it for production)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/user/{id}", "/register", "/login").permitAll()  // Allow access to registration and login
                .anyRequest().authenticated()  // Require authentication for all other requests
            )
            .formLogin(form -> form.disable())  // Disable form login if you are using JWT or other methods
            .httpBasic();  // Enable HTTP basic authentication (can be replaced with JWT)

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Password encoder bean
    }
    
    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilterRegistrationBean() {
        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtFilter());
        registrationBean.addUrlPatterns("/api/*"); // Adjust the URL pattern as needed
        registrationBean.setOrder(1); // Set the filter order if needed
        return registrationBean;
    }
    
}