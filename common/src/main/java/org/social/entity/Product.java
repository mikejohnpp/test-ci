package org.social.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @Column(name = "productID", nullable = false, length = 100)
    private String productID;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "discountDefault")
    private Integer discountDefault;

    @Lob
    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @ColumnDefault("'LV-BLV'")
    @JoinColumn(name = "categoryID")
    private Category categoryID;

    @OneToMany(mappedBy = "productID", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Subimage> subimages;


    @Column(name = "quanlityStock", nullable = false)
    private Integer quanlityStock;

    @Column(name = "quanlitySell", nullable = false)
    private Integer quanlitySell;

    @Lob
    @Column(name = "thumbnail", nullable = false)
    private String thumbnail;

    @Column(name = "createAt", nullable = false, columnDefinition = "DATE")
    private LocalDateTime createAt;

    @ColumnDefault("50")
    @Column(name = "minStockLevel")
    private Integer minStockLevel;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Specification specification;

    public Specification getSpecification() {
        return specification;
    };

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getDiscountDefault() {
        return discountDefault;
    }

    public void setDiscountDefault(Integer discountDefault) {
        this.discountDefault = discountDefault;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Category categoryID) {
        this.categoryID = categoryID;
    }

    public Integer getQuanlityStock() {
        return quanlityStock;
    }

    public void setQuanlityStock(Integer quanlityStock) {
        this.quanlityStock = quanlityStock;
    }

    public Integer getQuanlitySell() {
        return quanlitySell;
    }

    public void setQuanlitySell(Integer quanlitySell) {
        this.quanlitySell = quanlitySell;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public Integer getMinStockLevel() {
        return minStockLevel;
    }

    public void setMinStockLevel(Integer minStockLevel) {
        this.minStockLevel = minStockLevel;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productID='" + productID + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", discountDefault=" + discountDefault +
                ", description='" + description + '\'' +
                ", categoryID=" + categoryID +
                ", quanlityStock=" + quanlityStock +
                ", quanlitySell=" + quanlitySell +
                ", thumbnail='" + thumbnail + '\'' +
                ", createAt=" + createAt +
                ", minStockLevel=" + minStockLevel +
                '}';
    }

    public List<Subimage> getSubimages() {
        return subimages;
    }

    public void setSubimages(List<Subimage> subimages) {
        this.subimages = subimages;
    }
}