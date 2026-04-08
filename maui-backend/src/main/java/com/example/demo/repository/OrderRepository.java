package com.example.demo.repository;

import org.social.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("SELECT o FROM Order o WHERE o.userID.id = :userId ORDER BY o.createAt DESC")
    List<Order> findByUserID_Id(@Param("userId") Integer userId);

    @Query("SELECT o FROM Order o WHERE o.id = :orderId")
    Optional<Order> findOrderById(@Param("orderId") Integer orderId);
}