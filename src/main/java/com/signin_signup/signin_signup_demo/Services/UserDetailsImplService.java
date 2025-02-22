package com.signin_signup.signin_signup_demo.Services;



import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.signin_signup.signin_signup_demo.Repository.UserRepository;
import com.signin_signup.signin_signup_demo.Model.*;

@Service
public class UserDetailsImplService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    EmailServices mailservices;
    @Autowired
    private UserRepository UserRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = UserRepo.findbyUsername(username)
                          .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return UserDetailsImpl.build(user);
    }
    
    public void setEmailverification(User user){
        String token = UUID.randomUUID().toString();
        user.setEmailVal(token);
        UserRepo.save(user);
        String confirmation = "http://localhost:8080/verify?token="+token;
        mailservices.sendToken(confirmation, user.getEmail());
    };

    public void setEmailreset(User user){
        String token = UUID.randomUUID().toString();
        user.setEmailVal(token);
        UserRepo.save(user);
        String confirmation = "http://localhost:8080/reset_pw?token="+token;
        mailservices.sendToken(confirmation, user.getEmail());
    };
   
    public String checkEmailverification(String token){
        User usercheck= UserRepo.findByemailValidationToken(token).orElse(null);
        if(usercheck==null){
            return "Not verified";
        };
        usercheck.setEmailVal(null);
        usercheck.setEnable();
        UserRepo.save(usercheck);
        return "Valid";
    };

    public boolean checkEnable(User user){
        if(user.getEnable()==true){
                return true;
        };
        return false;
    }

    public String checkPasswordReset(String token){
        User usercheck= UserRepo.findByemailValidationToken(token).orElse(null);
        if(usercheck==null){
            return "Not verified";
        };
        usercheck.setEmailVal(null);
        usercheck.setTempDisable();
        UserRepo.save(usercheck);
        return "Valid";
    };

}
