package com.example.User_Registration_Login_Service.repository;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.User_Registration_Login_Service.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Logger for tracing repository calls
    Logger logger = LoggerFactory.getLogger(UserRepository.class);

    default Optional<User> findByUsernameWithLog(String username) {
        logger.info("Repository call: findByUsername [{}]", username);
        return findByUsername(username);
    }

    default Optional<User> findByEmailWithLog(String email) {
        logger.info("Repository call: findByEmail [{}]", email);
        return findByEmail(email);
    }

    default boolean existsByUsernameWithLog(String username) {
        logger.info("Repository call: existsByUsername [{}]", username);
        return existsByUsername(username);
    }

    default boolean existsByEmailWithLog(String email) {
        logger.info("Repository call: existsByEmail [{}]", email);
        return existsByEmail(email);
    }

    // Original method declarations (still required by Spring Data JPA)
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
