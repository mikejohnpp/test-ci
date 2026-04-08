package org.social.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "specifications")
public class Specification {

    @Id
    @Column(name = "specificationID", nullable = false, length = 100)
    private String specificationID;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productID", referencedColumnName = "productID")
    private Product product;

    @Column(name = "dimensions")
    private String dimensions;

    @Column(name = "material")
    private String material;

    @Column(name = "original", length = 100)
    private String original;

    @Column(name = "standard")
    private String standard;

    // getter/setter...
}
