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
    private int price;
    private int rentalId;
    private int productId;
    private int rentalPrice;
    private boolean rental;
}
