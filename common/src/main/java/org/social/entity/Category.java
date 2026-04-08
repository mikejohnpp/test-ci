package org.social.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "category")
public class Category {
    @Id
    @Column(name = "categoryID", nullable = false, length = 50)
    private String categoryID;

    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "thumbnail", nullable = false)
    private String thumbnail;

}