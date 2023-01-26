package com.lookatme.server.product.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProductPatchDto {
    private MultipartFile productImage;
    private String productName;
    private String category;
    private String brand;
    private String link;
    private String size;
    private Integer rentalId;
    private Integer productId;
    private int price;
    private int rentalPrice;
    private boolean rental;
}
