package org.social.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "subimages")
public class Subimage {
    @Id
    @Column(name = "subImagesID", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "createDate")
    private LocalDate createDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "productID", nullable = false)
    @JsonBackReference
    private Product productID;

}