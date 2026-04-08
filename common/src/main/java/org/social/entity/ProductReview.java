package org.social.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "product_reviews")
public class ProductReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reviewID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userID", nullable = false)
    private User userID;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "productID", nullable = false)
    private Product productID;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Lob
    @Column(name = "comment", nullable = false)
    private String comment;

    @ColumnDefault("0")
    @Column(name = "phoneNumber", nullable = false)
    private Integer phoneNumber;

    @ColumnDefault("current_timestamp()")
    @Column(name = "createdAt", nullable = false)
    private Instant createdAt;

    @Column(name = "fullname", length = 50)
    private String fullname;

    @ColumnDefault("0")
    @Column(name = "hidden")
    private Integer hidden;

}