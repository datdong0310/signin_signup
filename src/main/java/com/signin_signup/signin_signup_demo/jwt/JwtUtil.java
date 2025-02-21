package com.signin_signup.signin_signup_demo.jwt;



import java.security.Key;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.signin_signup.signin_signup_demo.Services.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    private final String JWT_secretkey = "datdong";
    private final long JWT_expiration = 480000;

    public String JWT_GenerateToke(Authentication authentication){
        UserDetailsImpl userPricipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
        .setSubject(userPricipal.getUsername())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 3000*60*60))
        .signWith(key(),SignatureAlgorithm.HS256)
        .compact();
    }

    private Key key(){
        return Keys.hmacShaKeyFor(JWT_secretkey.getBytes());
    }

    public String getUsernamefromToken(String Token){
        return Jwts.parserBuilder().setSigningKey(key()).build()
                  .parseClaimsJws(Token).getBody().getSubject();
    }

    public boolean validateToken(String Token){
        try{
            Jwts.parserBuilder().setSigningKey(key()).build().parse(Token);
            return true;
        } catch(MalformedJwtException e){
            logger.error("Invalid Token", e.getMessage());
            return false;
        } catch(ExpiredJwtException e){
            logger.error("Expired Token", e.getMessage());
        } catch(UnsupportedJwtException e){
            logger.error("Unsupported Token", e.getMessage());
            return false;
        }
        return false;
    }



}


