package com.signin_signup.signin_signup_demo.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.signin_signup.signin_signup_demo.Services.UserDetailsImpl;
import com.signin_signup.signin_signup_demo.Services.UserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private static UserDetailsService loaduserService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);


    

    private String parseJwt(HttpServletRequest request){
             String headerAuth = request.getHeader("Authorization");
             if(headerAuth!=null && headerAuth.startsWith("Bearer")){
                return headerAuth.substring(7);
             };
             return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterchain)
    throws IOException, ServletException{try{
                String jwt = parseJwt(request);
                if(jwt!=null && jwtUtil.validateToken(jwt)){
                    String username = jwtUtil.getUsernamefromToken(jwt);
                    UserDetails userDetails = loaduserService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, 
                        userDetails.getAuthorities()
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
    } catch (Exception e) {
        logger.error("Can not set user authentication", e);
    }
    filterchain.doFilter(request, response);
};



}
