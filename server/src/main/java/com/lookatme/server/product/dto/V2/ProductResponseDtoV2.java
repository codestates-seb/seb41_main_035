package com.lookatme.server.product.dto.V2;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductResponseDtoV2 {
    private long productId;
    private String productName;
    private String productImage;
    private String category;
    private String brand;
}
