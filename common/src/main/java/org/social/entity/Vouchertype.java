package org.social.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "vouchertype")
public class Vouchertype {
    @Id
    @Column(name = "typeID", nullable = false)
    private Integer id;

    @Column(name = "name")
    private String name;

}