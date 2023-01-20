package com.lookatme.server.product.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class ProductPostDto {
    private String productName;

    private int size;

    private int price; // 상품 구매 가격

    private String link;

    private boolean rental;

    private int rentalPrice; // 렌탈 가격

    private String category;

    private String brand;

    private String productImage;
}
