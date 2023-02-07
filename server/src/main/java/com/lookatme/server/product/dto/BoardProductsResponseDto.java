package com.lookatme.server.product.dto;

import com.lookatme.server.rental.dto.RentalResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardProductsResponseDto {

    private long productId;

    private String productName;

    private String productImage;

    private String link;

    private String category;

    private String brand;

    private int price;

    private RentalResponseDto rental;
}
