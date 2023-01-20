package com.lookatme.server.product.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class ProductPostDto {

    private String productName;

    private String size;

    private int price;

    private String link;

    private boolean isRental;

    private String category;

    private String brand;

    private String productImage;
}
