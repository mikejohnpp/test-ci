package org.social.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @Column(name = "orderID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "userID", nullable = false)
    private User userID;

    @Column(name = "fullname", nullable = false, length = 100)
    private String fullname;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "phoneNumber", nullable = false)
    private String phoneNumber;

    @ColumnDefault("''")
    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "paymentID", nullable = false)
    private Integer paymentID;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orderStatusID", nullable = false)
    private OrderStatus orderStatusID;

    @Column(name = "totalPrice", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "createAt")
    private LocalDate createAt;

    @Column(name = "deliveredDate")
    private LocalDate deliveredDate;

    @Lob
    @Column(name = "Note")
    private String note;

    @ColumnDefault("0")
    @Column(name = "fee")
    private Integer fee;

    @ColumnDefault("0")
    @Column(name = "discount")
    private Integer discount;

}