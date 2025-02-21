package com.signin_signup.signin_signup_demo.Services;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.signin_signup.signin_signup_demo.Repository.UserRepository;
import com.signin_signup.signin_signup_demo.Model.*;

@Service
public class UserDetailsImplService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private UserRepository UserRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = UserRepo.findbyUsername(username)
                          .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return UserDetailsImpl.build(user);
    }

}
