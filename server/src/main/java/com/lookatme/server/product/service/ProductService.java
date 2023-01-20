package com.lookatme.server.product.service;

import com.lookatme.server.product.entity.Product;
import com.lookatme.server.product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product) {
        verifyExistProduct(product.getProductId());

        return productRepository.save(product);
    }

    public Product updateProduct(Product product) {
        Product findProduct = findExistedProduct(product.getProductId());

        Optional.ofNullable(product.getProductName())
                .ifPresent(productName -> findProduct.setProductName(productName));

        Optional.ofNullable(product.getSellingPrice())
                .ifPresent(sellingPrice -> findProduct.setSellingPrice(sellingPrice));

        Optional.ofNullable(product.getLink())
                .ifPresent(link -> findProduct.setLink(link));

        return productRepository.save(findProduct);
    }

    public void deleteProduct(int productId) {

        productRepository.delete(findExistedProduct(productId));
    }

    public void deleteProducts() {

        productRepository.deleteAll();
    }

    public Product findProduct(int productId) {

        return findExistedProduct(productId);
    }

    public Page<Product> findProducts(int page, int size) {

        return productRepository.findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }
    private void verifyExistProduct(int productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isPresent()) {
            throw new RuntimeException("Product_ALREADY_EXIST");
        }
    }

    private Product findExistedProduct(int boardId) {
        Optional<Product> optionalProduct = productRepository.findById(boardId);

        return optionalProduct.orElseThrow(
                () -> new RuntimeException("Product_NOT_FOUND")
        );
    }
}
