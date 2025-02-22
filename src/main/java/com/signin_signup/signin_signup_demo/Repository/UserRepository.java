package com.signin_signup.signin_signup_demo.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.signin_signup.signin_signup_demo.Model.User;
import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findbyUsername(String username);
    Boolean existByUsername(String username);
    Boolean existByEmail(String email);
    Optional<User> findByemailValidationToken(String token);
};
