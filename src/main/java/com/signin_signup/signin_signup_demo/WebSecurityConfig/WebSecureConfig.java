package com.signin_signup.signin_signup_demo.WebSecurityConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.signin_signup.signin_signup_demo.Services.UserDetailsImpl;
import com.signin_signup.signin_signup_demo.Services.UserDetailsImplService;
import com.signin_signup.signin_signup_demo.Services.UserDetailsImplService;
import com.signin_signup.signin_signup_demo.jwt.AuthEntryPoint;
import com.signin_signup.signin_signup_demo.jwt.AuthTokenFilter;

@Configuration
@EnableMethodSecurity
public class WebSecureConfig {
    @Autowired
    private UserDetailsImplService userDetailsService;

    @Bean
    public AuthTokenFilter authenticationTokenFilter(){
        return new AuthTokenFilter();
    };
    
    @Autowired
    private AuthEntryPoint unauthorizedHandle;

    @Bean
    private PasswordEncoder passencode(){
        return new BCryptPasswordEncoder();
    };

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passencode());
        authProvider.setUserDetailsService(userDetailsService);
        return authProvider;
    };

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception{
        return authConfig.getAuthenticationManager();
    };
    





    

      
}
