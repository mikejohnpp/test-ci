package org.social.Dtos;

import org.social.entity.Category;
import org.social.entity.Subimage;

import java.time.LocalDateTime;
import java.util.List;

public record ProductDetailViewDTO(
        String productID,
        String name,
        Integer price,
        String description,
        Integer discountDefault,
        String thumbnail,
        Category categoryID,
        List<Subimage> subImages,
        Integer quantityStock,
        Integer quantitySell,
        LocalDateTime createAt,
        Integer minStockLevel,
        SpecificationProductDetailDTO specification
) { }
