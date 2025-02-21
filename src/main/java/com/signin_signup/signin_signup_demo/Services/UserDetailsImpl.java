package com.signin_signup.signin_signup_demo.Services;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.signin_signup.signin_signup_demo.Model.*;

public class UserDetailsImpl implements UserDetails{
    private static final Integer serialVersionUID = 1;
    private Integer id;
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    public Collection<? extends GrantedAuthority> authorities;
    
    public UserDetailsImpl(String username, String password, String email, Collection<? extends GrantedAuthority> authorities){
        this.username=username;
        this.password=password;
        this.email=email;
        this.authorities=authorities;
    }

    public static UserDetailsImpl build(User user){
        List<GrantedAuthority> authorities = user.getRoles().stream().
        map(role -> new SimpleGrantedAuthority(role.getRoles().name())).collect(Collectors.toList());
        return new UserDetailsImpl(
             user.getUsename(),
             user.getPassword(),
             user.getEmail(),
             authorities
            );

    };

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      return authorities;
    }
    
    public Integer getId() {
        return id;
    }
    
    public String getEmail() {
        return email;
    }
    
      @Override
      public String getPassword() {
        return password;
      }
    
      @Override
      public String getUsername() {
        return username;
      }
    
      @Override
      public boolean isAccountNonExpired() {
        return true;
      }
    
      @Override
      public boolean isAccountNonLocked() {
        return true;
      }
    
      @Override
      public boolean isCredentialsNonExpired() {
        return true;
      }
    
      @Override
      public boolean isEnabled() {
        return true;
      }
    
      @Override
      public boolean equals(Object o) {
        if (this == o)
          return true;
        if (o == null || getClass() != o.getClass())
          return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
      }
}
