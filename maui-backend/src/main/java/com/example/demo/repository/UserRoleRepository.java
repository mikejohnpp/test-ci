package com.example.demo.repository;

import org.social.entity.User;
import org.social.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole , Integer> {
    List<UserRole> findByUserID(User userID);
}
