package com.signin_signup.signin_signup_demo.Repository;

import java.util.Optional;
import com.signin_signup.signin_signup_demo.Model.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findbyRoleName(eRole RoleName);

}
