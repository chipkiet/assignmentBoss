package org.example.profilecase5.Config;

import org.example.profilecase5.Service.CustomerUserDetailService;
import org.example.profilecase5.common.CustomAuthenticationEntryPoint;
import org.example.profilecase5.common.CustomAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private CustomerUserDetailService customerUserDetailService;

    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .userDetailsService(customerUserDetailService)
                .authorizeRequests(auth -> auth
                        // Các URL không yêu cầu xác thực
                        .requestMatchers( "/main", "/register", "/registerOwner", "/login", "/css/**", "/js/**", "/images/**").permitAll()
                        // Chỉ OWNER, ADMIN, USER được truy cập các URL tương ứng
                        .requestMatchers("/hosting").hasRole("OWNER")
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/home").hasRole("USER")
                        // Các yêu cầu khác yêu cầu xác thực
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/perform_login")
                        .successHandler(customAuthenticationSuccessHandler)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/login") // Trang login mặc định
                        .successHandler((request, response, authentication) ->
                                response.sendRedirect("/register")) // Redirect sau khi login thành công
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint))
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/profile/update-avatar")
                )
                .logout(logout -> logout
                        .logoutUrl("/perform_logout")  
                        .logoutSuccessUrl("/main")     
                        .deleteCookies("JSESSIONID")   
                        .invalidateHttpSession(true)   
                        .clearAuthentication(true)     
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                );

        return http.build();
    }
}
