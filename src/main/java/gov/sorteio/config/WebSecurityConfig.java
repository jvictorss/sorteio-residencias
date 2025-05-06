package gov.sorteio.config;

import gov.sorteio.security.AuthEntryPointJwt;
import gov.sorteio.security.AuthTokenFilter;
import gov.sorteio.service.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    final UserDetails userDetailsService;

    final AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Autowired
    public WebSecurityConfig(UserDetails userDetailsService, AuthEntryPointJwt unauthorizedHandler) {
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public SecurityFilterChain filterAppChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                // The pages does not require login
                .authorizeRequests()
                .antMatchers("/v1/login/**", "/site/**", "/**").permitAll()
                .anyRequest().authenticated()
                .filterSecurityInterceptorOncePerRequest(true)
                .and().exceptionHandling()
                .accessDeniedPage("/v1/login?logout=true")
                // Config for Login Form
                .and().formLogin()
                // Submit URL of login page.
                .loginProcessingUrl("/v1/j_spring_security_check") // Submit URL
                .loginPage("/v1/login")
                .defaultSuccessUrl("/v1/painel",true)
                .permitAll()
                .failureUrl("/v1/login?exception")
                .failureHandler(authenticationFailureHandler())
                .usernameParameter("email")
                .passwordParameter("senha")
                // Config for Logout Page
                .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/v1/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
        http.headers().frameOptions().disable();
        return http.build();
    }
}