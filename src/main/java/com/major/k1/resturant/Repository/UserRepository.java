package com.major.k1.resturant.Repository;

import com.major.k1.resturant.Entites.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<User,Long> {
    User findByEmail (String email);
    Optional<User> findByUsername (String email);
    Optional<User> findById(Long id);
}
