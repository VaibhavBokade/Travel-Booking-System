package com.example.Travel.Booking.System.repository;

import com.example.Travel.Booking.System.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByPassword(String password);

    @Query("select u.password from User u where u.email = :email")
    String getPassword(@Param("email") String email);

    @Query("select u from User u where u.email = :email and u.password = :password")
    User validateUser(@Param("email") String email, @Param("password") String password);

    User findByEmail(String email);

}
