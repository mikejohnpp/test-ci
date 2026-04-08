package com.example.demo.service;

import org.social.Dtos.ProductReviewDTO;
import org.social.Dtos.SummaryReviewDTO;
import org.social.Dtos.request.ProductReviewRequest;
import org.social.Dtos.request.ReviewRequest;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ProductReviewRepository;
import com.example.demo.repository.UserRepository;
import org.social.entity.Product;
import org.social.entity.ProductReview;
import org.social.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class ProductReviewService {
    @Autowired
    private ProductReviewRepository productReviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public Page<ProductReviewDTO> findAll(ProductReviewRequest productReviewRequest) {
        Pageable pageable = productReviewRequest.toPageable();
        var productReviews = productReviewRepository.findByProductID(productReviewRequest.getProductId(), productReviewRequest.getRating(), pageable);
        return productReviews.map(productReview -> new ProductReviewDTO(
                productReview.getProductID().getProductID(),
                productReview.getUserID().getFullName(),
                productReview.getUserID().getAvatar(),
                productReview.getRating(),
                productReview.getComment(),
                productReview.getCreatedAt()
        ));
    }

    public SummaryReviewDTO summaryReviews(ProductReviewRequest productReviewRequest) {
        productReviewRequest.setPage(0);
        productReviewRequest.setSize(5);
        Pageable pageable = productReviewRequest.toPageable();
        double avgRating = ((Number) Objects.requireNonNullElse(productReviewRepository.findAverageRating(productReviewRequest.getProductId()), 0)).doubleValue();
        List<Object[]> starCounts = productReviewRepository.countRatingByLevel(productReviewRequest.getProductId());
        long totalComments = ((Number) Objects.requireNonNullElse(productReviewRepository.countComments(productReviewRequest.getProductId()), 0)).longValue();
        List<ProductReviewDTO> comments = productReviewRepository.findTop5Comments(productReviewRequest.getProductId(), pageable);

        Map<Integer, Integer> starCount = new HashMap<>();
        for (Object[] row : starCounts) {
            try{
                Integer rating = row[0] != null ? ((Number) row[0]).intValue() : 0;
                Integer count = row[1] != null ? ((Number) row[1]).intValue() : 0;
                starCount.put(rating, count);
            } catch (NumberFormatException ignored){ }
        }

        for (int i = 1; i <= 5; i++) {
            starCount.putIfAbsent(i, 0);
        }

        boolean hasComment = false;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated() && authentication.getPrincipal() != null && !"anonymousUser".equals(authentication.getPrincipal())) {

            org.springframework.security.core.userdetails.User u =
                    (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

            User user = userRepository.findByEmail(u.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));

            Product product = productRepository.findById(productReviewRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Sản phẩm không tồn tại"));

            Optional<ProductReview> existingReview = productReviewRepository.findByUserIDAndProductID(user, product);
            if (existingReview.isPresent()) {
                hasComment = true;
            }
        }

        return new SummaryReviewDTO(avgRating, totalComments, starCount,comments, hasComment);
    }

    public boolean postComment(ReviewRequest reviewRequest) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            org.springframework.security.core.userdetails.User u = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

            User user  =  userRepository.findByEmail(u.getUsername())
                    .orElseThrow(() -> {
                        return new ResourceNotFoundException("Người dùng không tồn tại");
                    });

            Product product = productRepository.findById(reviewRequest.getProductId())
                    .orElseThrow(() -> {
                        return new ResourceNotFoundException("Người dùng không tồn tại");
                    });

            Optional<ProductReview> existingReview = productReviewRepository.findByUserIDAndProductID(user, product);
            if (existingReview.isPresent()) {
                return false;
            }

            ProductReview review = new ProductReview();
            review.setProductID(product);
            review.setUserID(user);
            review.setRating(reviewRequest.getRating());
            review.setComment(reviewRequest.getComment());
            review.setCreatedAt(Instant.now());


            review.setHidden(0);
            review.setPhoneNumber(0);
            review.setFullname(user.getFullName());

            productReviewRepository.save(review);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
