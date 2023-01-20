package com.lookatme.server.rental.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RentalResponseDto {

    private int rentalId;

    private boolean rental;

    private int size;

    private int price;

    private String createdAt;

    private String modifiedAt;
}
