package com.lookatme.server.product.controller;

import com.lookatme.server.common.dto.MultiResponseDto;
import com.lookatme.server.product.dto.V2.ProductPatchDtoV2;
import com.lookatme.server.product.dto.V2.ProductPostDtoV2;
import com.lookatme.server.product.dto.V2.ProductResponseDtoV2;
import com.lookatme.server.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/products")
@RestController
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid ProductPostDtoV2 post) {
        ProductResponseDtoV2 product = productService.createProductV2(post);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> searchProduct(@PathVariable long productId) {
        return new ResponseEntity<>(
                productService.findProductV2(productId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> searchProducts(@RequestParam(name = "tab", defaultValue = "none") String tab,
                                            @RequestParam(name = "keyword", required = false) String keyword,
                                            @RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        Page<ProductResponseDtoV2> productList = productService.findProductsV2(tab, keyword, page - 1, size);
        return new ResponseEntity<>(
                new MultiResponseDto<>(productList.getContent(), productList),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable long productId,
                                           @Valid ProductPatchDtoV2 patch) {
        return new ResponseEntity<>(
                productService.updateProductV2(productId, patch),
                HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable long productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
