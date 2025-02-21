package com.signin_signup.signin_signup_demo.Model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "user",
       uniqueConstraints = {@UniqueConstraint(columnNames = "username"),
                           @UniqueConstraint(columnNames = "email")
    }
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private Integer user_id;

    private String username;

    private String email;

    private String password;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", 
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns =  @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
   
    public User(){
    };
    

    public User(String username, String email, String password){
        this.username=username;
        this.email=email;
        this.password=password;
    }

    public Integer getUserId() {
        return user_id;
       }
    
      
       public void setUserId(Integer id) {
        this.user_id = id;
    };

    public String getUsename() {
        return username;
       }
    
      
    public void setUsername(String name) {
        this.username = name;
    };

    public String getPassword()  {
        return password;
       }
    
      
    public void setPassword(String pw) {
        this.password = pw;
    };

    public String getEmail() {
        return email;
       }
    
      
    public void setEmail(String em) {
        this.email = em;
    };

    public Set<Role> getRoles() {
        return roles;
      }
    
      public void setRoles(Set<Role> roles) {
        this.roles = roles;
      }

    
}
