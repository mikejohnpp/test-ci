package org.social.Dtos;

public record ProductViewDTO(
        String productID,
        String name,
        Integer price,
        Integer discountDefault,
        String thumbnail,
        String categoryID,
        Integer quantityStock,
        Integer quantitySell
) {}