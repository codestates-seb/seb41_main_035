package com.lookatme.server.product.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class ProductPostDto {

    private String productName;

    private String category;

    private String brand;

    private String size;

    private int price;

    private String link;

    private boolean isRental;

    // 이미지는 URL을 통해 접근하도록 한다.
    private String image;
}
