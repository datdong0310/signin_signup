package com.signin_signup.signin_signup_demo.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class resetpsRequest {
  @NotBlank
  @Email
  private String email;
  private String password;
  public void setEmail(String Email){
    this.email=Email;
}
  
  public String getEmail(){
        return email;
    }

    public void setPassword(String ps){
        this.password=ps;
    }
      
      public String getps(){
            return password;
        }
   
}
