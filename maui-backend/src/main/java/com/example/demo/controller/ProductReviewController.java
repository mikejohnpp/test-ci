package com.example.demo.controller;

import org.social.Dtos.ProductReviewDTO;
import org.social.Dtos.SummaryReviewDTO;
import org.social.Dtos.request.ProductReviewRequest;
import org.social.Dtos.request.ReviewRequest;
import org.social.config.ResponseApi;
import org.social.handler.ResponseStatus;
import com.example.demo.service.ProductReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product-reviews")
public class ProductReviewController {
    @Autowired
    private ProductReviewService productReviewService;

    @GetMapping("/get-list")
    public ResponseEntity<ResponseApi<Page<ProductReviewDTO>>> getList(@ModelAttribute @Validated ProductReviewRequest productReviewRequest) {
        return new ResponseEntity<>(new ResponseApi<>(ResponseStatus.SUCCESS, productReviewService.findAll(productReviewRequest)), HttpStatus.OK);
    }

    @GetMapping("/summary-review")
    public ResponseEntity<ResponseApi<SummaryReviewDTO>> getSummaryReview(@ModelAttribute @Validated ProductReviewRequest productReviewRequest) {
        return new ResponseEntity<>(new ResponseApi<>(ResponseStatus.SUCCESS, productReviewService.summaryReviews(productReviewRequest)), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<ResponseApi<?>> postComment(@RequestBody @Valid ReviewRequest reviewRequest) {
        try {
            boolean response = productReviewService.postComment(reviewRequest);
            if (response) {
                return new ResponseEntity<>(
                        new ResponseApi<>(ResponseStatus.CREATED, null),
                        HttpStatus.CREATED
                );
            } else {
                return new ResponseEntity<>(
                        new ResponseApi<>(ResponseStatus.INTERNAL_SERVER_ERROR, null),
                        HttpStatus.INTERNAL_SERVER_ERROR
                );
            }

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(
                    new ResponseApi<>(ResponseStatus.BAD_REQUEST, null),
                    HttpStatus.BAD_REQUEST
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ResponseApi<>(ResponseStatus.INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
