package com.lookatme.server.product.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProductPostDto {
    private MultipartFile productImage;
    private String productName;
    private String category;
    private String brand;
    private String size;
    private String link;
    private int price; // 구매 가격
    private int rentalPrice; // 렌탈 가격
    private boolean rental;
}
