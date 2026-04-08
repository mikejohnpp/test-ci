package com.example.demo.repository;

import org.social.Dtos.ProductReviewDTO;
import org.social.entity.Product;
import org.social.entity.ProductReview;
import org.social.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Integer> {
    @Query("""
        SELECT p FROM ProductReview p
        WHERE p.productID.productID = :productId
        """)
    Page<ProductReview> findByProductID(@Param("productId") String productId, Pageable pageable);

    @Query("""
            SELECT p FROM ProductReview p
            WHERE p.productID.productID = :productId
            AND (:rating IS NULL OR p.rating = :rating)
            """)
    Page<ProductReview> findByProductID(@Param("productId") String productId,
                                                 @Param("rating") Integer rating,
                                                 Pageable pageable);

    @Query("""
            SELECT AVG(p.rating) FROM ProductReview p
            WHERE p.productID.productID = :productId
            """)
    Double findAverageRating(@Param("productId") String productId);

    @Query("""
           SELECT p.rating, COUNT(p) FROM ProductReview p
           WHERE p.productID.productID = :productId
           GROUP BY p.rating
           """)
    List<Object[]> countRatingByLevel(@Param("productId") String productId);

    @Query("""
            SELECT COUNT(p) FROM ProductReview p
            WHERE p.productID.productID = :productId
            AND p.comment IS NOT NULL AND p.comment <> ''
            """)
    Long countComments(@Param("productId") String productId);

    @Query("""
           SELECT p.productID.productID, p.userID.fullName, p.userID.avatar, p.rating, p.comment, p.createdAt
           FROM ProductReview p WHERE p.productID.productID = :productId
           AND p.comment IS NOT NULL AND p.comment <> ''
           """)
    List<ProductReviewDTO> findTop5Comments(@Param("productId") String productId, Pageable pageable);

    Optional<ProductReview> findByUserIDAndProductID(User user, Product product);

}
