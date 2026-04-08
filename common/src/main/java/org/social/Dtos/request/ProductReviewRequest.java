package org.social.Dtos.request;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class ProductReviewRequest {
    private int page = 0;
    private int size = 10;
    @NotBlank(message = "Cần truyền productId")
    private String productId;
    private String sortByName = "createdAt";
    private String sortOrder = "desc";
    private Integer rating = null;

    // Getters and setters for all fields

    public Pageable toPageable() {
        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortByName);
        return PageRequest.of(page, size, sort);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSortByName() {
        return sortByName;
    }

    public void setSortByName(String sortByName) {
        this.sortByName = sortByName;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
