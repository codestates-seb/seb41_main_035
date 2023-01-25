package com.lookatme.server.product.dto;

import com.lookatme.server.rental.dto.RentalResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BoardProductsResponseDto {

    private long productId;

    private String productName;

    private String productImage;

    private String link;

    private String category;

    private String brand;

    private int price;

    private List<RentalResponseDto> rentals;
}
