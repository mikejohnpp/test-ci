package org.social.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "voucher")
public class Voucher {
    @Id
    @Column(name = "voucherID", nullable = false)
    private Integer id;

    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @Column(name = "name", length = 100)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "typeID")
    private Vouchertype typeID;

    @Column(name = "voucherPercent")
    private Integer voucherPercent;

    @Column(name = "voucherCash")
    private Integer voucherCash;

    @Column(name = "maximumValue")
    private Integer maximumValue;

    @Column(name = "startDate")
    private LocalDate startDate;

    @Column(name = "endDate")
    private LocalDate endDate;

    @Column(name = "minOrderValue")
    private Integer minOrderValue;

    @ColumnDefault("'discount'")
    @Lob
    @Column(name = "category_voucher")
    private String categoryVoucher;

    @ColumnDefault("100")
    @Column(name = "quantity")
    private Integer quantity;

    @ColumnDefault("50")
    @Column(name = "quantity_used")
    private Integer quantityUsed;

}