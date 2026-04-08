package com.example.demo.repository;

import org.social.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {
}
