package org.social.Dtos;

import java.time.Instant;

public record ProductReviewDTO(
    String productId,
    String userName,
    String avatar,
    Integer rating,
    String comment,
    Instant createdAt
) {}
