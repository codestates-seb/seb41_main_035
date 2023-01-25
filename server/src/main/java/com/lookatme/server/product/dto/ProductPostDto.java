package com.lookatme.server.product.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;

@Getter
@Setter
public class ProductPostDto {

    private MultipartFile productImage;

    private String productName;

    private String size;

    private int price; // 상품 구매 가격

    private String link;

    private boolean rental;

    private int rentalPrice; // 렌탈 가격

    private String category;

    private String brand;
}
