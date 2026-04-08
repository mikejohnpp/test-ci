package com.example.demo.service;

import org.social.Dtos.ProductDetailViewDTO;
import org.social.Dtos.ProductViewDTO;
import org.social.Dtos.SpecificationProductDetailDTO;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.SpecificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SpecificationRepository specificationRepository;

    public List<ProductViewDTO> getListCursor(Integer quantitySell, Integer size) {
        Pageable topSize;
        topSize = PageRequest.of(0, Objects.requireNonNullElse(size, 15));
        return productRepository.findNextProductsByQuantitySellNext(quantitySell, topSize).stream().map(p -> new ProductViewDTO(
                p.getProductID(),
                p.getName(),
                p.getPrice(),
                p.getDiscountDefault(),
                p.getThumbnail(),
                p.getCategoryID().getName(),
                p.getQuanlityStock(),
                p.getQuanlitySell()
        )).toList();
    }

    public Page<ProductViewDTO> getListPaginated(Pageable pageable) {
        var products =  productRepository.findAll(pageable);
        return products.map(p -> new ProductViewDTO(
                p.getProductID(),
                p.getName(),
                p.getPrice(),
                p.getDiscountDefault(),
                p.getThumbnail(),
                p.getCategoryID().getName(),
                p.getQuanlityStock(),
                p.getQuanlitySell()
        ));
    }

    public List<ProductViewDTO> searchProducts(String keyword, Integer size) {
        Pageable topSize;
        topSize = size != null ?  PageRequest.of(0, size) : Pageable.unpaged();
        return productRepository.searchProducts(keyword, topSize).stream().map(p -> new ProductViewDTO(
                p.getProductID(),
                p.getName(),
                p.getPrice(),
                p.getDiscountDefault(),
                p.getThumbnail(),
                p.getCategoryID().getName(),
                p.getQuanlityStock(),
                p.getQuanlitySell()
        )).toList();
    }

    public List<ProductViewDTO> findAll() {
        var product = productRepository.findAll();
        return product.stream().map(p -> new ProductViewDTO(
                p.getProductID(),
                p.getName(),
                p.getPrice(),
                p.getDiscountDefault(),
                p.getThumbnail(),
                p.getCategoryID().getName(),
                p.getQuanlityStock(),
                p.getQuanlitySell()
        )).toList();
    }

    public ProductDetailViewDTO getDetail(String productID) {
        var product = productRepository.findByIdFetchJoin(productID)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm " + productID));

        var spec = product.getSpecification();

        return new ProductDetailViewDTO(
                product.getProductID(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getDiscountDefault(),
                product.getThumbnail(),
                product.getCategoryID(),
                product.getSubimages(),
                product.getQuanlityStock(),
                product.getQuanlitySell(),
                product.getCreateAt(),
                product.getMinStockLevel(),
                spec == null ? null : new SpecificationProductDetailDTO(
                        spec.getSpecificationID(),
                        spec.getDimensions(),
                        spec.getMaterial(),
                        spec.getOriginal(),
                        spec.getStandard(),
                        product.getProductID()
                )
        );
    }
}
