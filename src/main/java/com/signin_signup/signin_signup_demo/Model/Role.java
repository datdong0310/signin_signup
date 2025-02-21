package com.signin_signup.signin_signup_demo.Model;

import jakarta.persistence.*;

@Entity
@Table
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private Integer role_Id;

    @Enumerated(EnumType.STRING)
    private eRole RoleName;

    public Role() {

    }
  
    public Role(eRole name) {
      this.RoleName = name;
    }
  
    public Integer getRoleId() {
    return role_Id;
   }

  
   public void setRoleId(Integer id) {
    this.role_Id = id;
   }


    public eRole getRoles() {
    return RoleName;
   }

  public void setRoles(eRole name) {
     this.RoleName = name;
   }



}
