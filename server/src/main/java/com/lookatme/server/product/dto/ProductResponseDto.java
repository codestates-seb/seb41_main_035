package com.lookatme.server.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseDto {

    private String productName;

    private String size;

    private int price;

    private String link;

    private boolean rental;

    // 이미지는 URL을 통해 접근하도록 한다.
    private String image;
}
