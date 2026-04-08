package org.social.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_status")
public class OrderStatus {
    @Id
    @Column(name = "orderStatusID", nullable = false)
    private Integer id;

    @Column(name = "orderStatusName", nullable = false, length = 50)
    private String orderStatusName;

}