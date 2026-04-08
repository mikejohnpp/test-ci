package com.example.demo.controller;

import org.social.Dtos.ProductDetailViewDTO;
import org.social.Dtos.ProductViewDTO;
import org.social.Dtos.request.ProductGetRequest;
import org.social.config.ResponseApi;
import org.social.handler.ResponseStatus;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("/")
    public ResponseEntity<ResponseApi<List<ProductViewDTO>>> getAllProducts() {
        return new ResponseEntity<>(new ResponseApi<>(ResponseStatus.SUCCESS, productService.findAll()), HttpStatus.OK);
    }

    @GetMapping("/get-list")
    public ResponseEntity<ResponseApi<Page<ProductViewDTO>>> getListPaging(@ModelAttribute ProductGetRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        return new ResponseEntity<>(new ResponseApi<>(ResponseStatus.SUCCESS, productService.getListPaginated(pageable)), HttpStatus.OK);
    }

    @GetMapping("/get-list-cursor")
    public ResponseEntity<ResponseApi<List<ProductViewDTO>>> getListCursor(@ModelAttribute ProductGetRequest request) {
        return new ResponseEntity<>(new ResponseApi<>(ResponseStatus.SUCCESS, productService.getListCursor(request.getQuantitySell(), request.getSize())), HttpStatus.OK);
    }


    @GetMapping("/detail/{id:.+}")
    public ResponseEntity<ResponseApi<ProductDetailViewDTO>> getDetail(@PathVariable("id") String id) {
        return new ResponseEntity<>(new ResponseApi<>(ResponseStatus.SUCCESS, productService.getDetail(id)), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseApi<List<ProductViewDTO>>> searchProducts(@ModelAttribute ProductGetRequest request) {
        return new ResponseEntity<>(new ResponseApi<>(ResponseStatus.SUCCESS, productService.searchProducts(request.getKeyword(), request.getSize())), HttpStatus.OK);
    }


}
