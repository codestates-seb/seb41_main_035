package com.lookatme.server.product.service;

import com.lookatme.server.board.entity.BoardProduct;
import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.exception.ErrorLogicException;
import com.lookatme.server.file.FileDirectory;
import com.lookatme.server.file.FileService;
import com.lookatme.server.product.dto.ProductPatchDto;
import com.lookatme.server.product.dto.ProductPostDto;
import com.lookatme.server.product.entity.Brand;
import com.lookatme.server.product.entity.Category;
import com.lookatme.server.product.entity.Product;
import com.lookatme.server.product.mapper.ProductMapper;
import com.lookatme.server.product.repository.BrandRepository;
import com.lookatme.server.product.repository.CategoryRepository;
import com.lookatme.server.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Transactional
@RequiredArgsConstructor
@Service
public class ProductService {

    private final FileService fileService;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductMapper productMapper;

    /**
     * 상품 생성
     */
    public Product createProduct(ProductPostDto post, String itemImageUrl) {
        Product product = productMapper.productPostToProduct(post);
        Category category = findCategory(post.getCategory());
        Brand brand = findBrand(post.getBrand());

        product.setCategory(category);
        product.setBrand(brand);
        product.setProductImage(itemImageUrl);
        return productRepository.save(product);
    }

    public Product createProduct(ProductPatchDto patch, String itemImageUrl) {
        ProductPostDto productPost = productMapper.productPatchToProductPost(patch);
        return createProduct(productPost, itemImageUrl);
    }

    /**
     * 상품 수정
     */
    public Product updateProduct(ProductPatchDto patch) throws IOException {
        Product savedProduct = findProduct(patch.getProductId());
        String productImageUrl = savedProduct.getProductImage();
        if (patch.getProductImage() != null) { // 상품 사진이 왔을때만 업데이트
            productImageUrl = fileService.upload(patch.getProductImage(), FileDirectory.item);
        }
        savedProduct.updateProduct(
                patch.getProductName(),
                productImageUrl,
                patch.getLink(),
                patch.getPrice()
        );
        savedProduct.setCategory(findCategory(patch.getCategory()));
        savedProduct.setBrand(findBrand(patch.getBrand()));
        return savedProduct;
    }

    public void deleteProduct(int productId) {
        Product product = findExistedProduct(productId);
        for (BoardProduct boardProduct : product.getBoardProducts()) {
            boardProduct.removeProduct();
        }
        productRepository.delete(product);
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
        productRepository.findById(productId)
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.PRODUCT_ALREADY_EXISTS));
    }

    private Product findExistedProduct(int boardId) {
        return productRepository.findById(boardId)
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    private Product findExistedProduct(String productName) {
        return productRepository.findByProductName(productName)
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    /**
     * 카테고리가 있으면 기존것을 가져오고 없으면 새로 생성함
     */
    public Category findCategory(String categoryName) {
        return categoryRepository.findByName(categoryName)
                .orElseGet(() -> createCategory(categoryName));
    }

    public Category createCategory(String categoryName) {
        Category newCategory = new Category(categoryName);
        return categoryRepository.save(newCategory);
    }

    /**
     * 브랜드가 있으면 기존것을 가져오고 없으면 새로 생성함
     */
    public Brand findBrand(String brandName) {
        return brandRepository.findByName(brandName)
                .orElseGet(() -> createBrand(brandName));
    }

    public Brand createBrand(String brandName) {
        Brand newBrand = new Brand(brandName);
        return brandRepository.save(newBrand);
    }
}
