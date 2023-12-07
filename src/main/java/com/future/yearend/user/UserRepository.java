package com.future.yearend.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameAndPhoneNum(String username, String phoneNum);

    Optional<User> findByUsername(String username);
}
