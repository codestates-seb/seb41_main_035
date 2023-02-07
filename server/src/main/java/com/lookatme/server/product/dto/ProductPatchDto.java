package com.lookatme.server.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductPatchDto {
    private MultipartFile productImage;
    private String productName;
    private String category;
    private String brand;
    private String size;
    private String link;
    private Long productId;
    private int price;
    private int rentalPrice;
    private boolean rental;
}
