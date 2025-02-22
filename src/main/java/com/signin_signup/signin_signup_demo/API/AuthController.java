package com.signin_signup.signin_signup_demo.API;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import com.signin_signup.signin_signup_demo.Repository.RoleRepository;
import com.signin_signup.signin_signup_demo.Repository.UserRepository;
import com.signin_signup.signin_signup_demo.Services.EmailServices;
import com.signin_signup.signin_signup_demo.Services.UserDetailsImpl;
import com.signin_signup.signin_signup_demo.Services.UserDetailsImplService;
import com.signin_signup.signin_signup_demo.jwt.JwtUtil;
import com.signin_signup.signin_signup_demo.payload.request.resetpsRequest;
import com.signin_signup.signin_signup_demo.payload.request.signinRequest;
import com.signin_signup.signin_signup_demo.payload.request.signupRequest;
import com.signin_signup.signin_signup_demo.payload.respone.jwtRespone;
import com.signin_signup.signin_signup_demo.payload.respone.messageRespone;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.signin_signup.signin_signup_demo.payload.respone.jwtRespone;
import com.signin_signup.signin_signup_demo.Model.Role;
import com.signin_signup.signin_signup_demo.Model.User;
import com.signin_signup.signin_signup_demo.Model.eRole;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
      @Autowired
      AuthenticationManager authenticationManager;

      @Autowired
      UserRepository userRepo;

      @Autowired
      RoleRepository roleRepo;

      @Autowired
      PasswordEncoder passEncode;

      @Autowired
      JwtUtil jwtUtil;

      @Autowired
      UserDetailsImplService userDetailsImplService;

      @Autowired
      EmailServices sendMail;



      @PostMapping("/signin")
      public ResponseEntity<?> authenticateUser(@Valid @RequestBody signinRequest SignInRequest) {
        User user = userRepo.findbyUsername(SignInRequest.getUsername()).orElse(null);
        if (userDetailsImplService.checkEnable(user)==false){
          return ResponseEntity.badRequest().body(new messageRespone("Not verified"));
        };
          Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(SignInRequest.getUsername(), SignInRequest.getPassword())
          );
          SecurityContextHolder.getContext().setAuthentication(authentication);
          String token = jwtUtil.JWT_GenerateToke(authentication);
          UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
          sendMail.sendToken(token, userDetails.getEmail());
          return ResponseEntity.ok("Check your email for authentication token");
      };

      @PostMapping("/signup")
      public ResponseEntity<?> signupnewUser(@Valid @RequestBody signupRequest SignUpRequest) {
          if(userRepo.existByUsername(SignUpRequest.getUsername())){
            return ResponseEntity.badRequest().body(new messageRespone("Username is already existed"));
          }

          if(userRepo.existByEmail(SignUpRequest.getEmail())){
            return ResponseEntity.badRequest().body(new messageRespone("Email is already used"));
          }

          User user = new User(SignUpRequest.getUsername(), SignUpRequest.getEmail(), passEncode.encode(SignUpRequest.getPassword()));
          Set<String> roles = SignUpRequest.getRole();
          Set<Role> ROLES = new HashSet<>();
          if(roles.isEmpty()){
            Role userRole = roleRepo.findbyRoleName(eRole.ROLE_USER)
            .orElseThrow(()-> new RuntimeException("Role not found"));
            ROLES.add(userRole);
          } else{
            for (String role : roles) {
                switch (role) {
                    case "ADMIN":
                        Role adminRole = roleRepo.findbyRoleName(eRole.ROLE_ADMIN)
                        .orElseThrow(()-> new RuntimeException("Role not found"));
                        ROLES.add(adminRole);
                        break;

                    case "MODERATOR":
                        Role modRole = roleRepo.findbyRoleName(eRole.ROLE_MODERATOR)
                        .orElseThrow(()-> new RuntimeException("Role not found"));
                        ROLES.add(modRole);
                        break;

                    case "USER":
                        Role user1Role = roleRepo.findbyRoleName(eRole.ROLE_USER)
                        .orElseThrow(()-> new RuntimeException("Role not found"));
                        ROLES.add(user1Role);
                        break;     

                
                    default:
                        Role user2Role = roleRepo.findbyRoleName(eRole.ROLE_USER)
                        .orElseThrow(()-> new RuntimeException("Role not found"));
                        ROLES.add(user2Role);
                        break; 
                }
                
            }
          }
          user.setRoles(ROLES);
          userRepo.save(user);
          userDetailsImplService.setEmailverification(user);
          
          return ResponseEntity.ok(new messageRespone("Sign up success, please verified your email to continue"));
          }
          
        @GetMapping("/verify")
        public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
        return ResponseEntity.ok(userDetailsImplService.checkEmailverification(token));
    };          
          @PostMapping("/forgot_password")
          public ResponseEntity<?> EmailresetPassword(@Valid @RequestBody resetpsRequest request) {
              User user= userRepo.findByEmail(request.getEmail()).orElse(null);
              if(user==null){
                return ResponseEntity.badRequest().body(new messageRespone("Can not find email"));
              };
             
              userDetailsImplService.setEmailverification(user);
              return ResponseEntity.ok(new messageRespone("Please check your email to confirm reset password"));
          }
     
        @GetMapping("/reset_pw")
        public ResponseEntity<?> verifypassword(@RequestParam("token") String token, @RequestParam("newPassword") String newPassword) {
          User user = userRepo.findByemailValidationToken(token).orElse(null);
        if(user!=null && userDetailsImplService.checkPasswordReset(token)=="valid"){
                      user.setPassword(newPassword);
                      userRepo.save(user);
                      return ResponseEntity.ok("reset successfully");
        }
        return ResponseEntity.badRequest().body(new messageRespone("Can not verify"));
       
};};
