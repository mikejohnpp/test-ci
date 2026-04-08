package org.social.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @Column(name = "paymentID", nullable = false)
    private Integer id;

    @Column(name = "paymentName", nullable = false, length = 50)
    private String paymentName;

}