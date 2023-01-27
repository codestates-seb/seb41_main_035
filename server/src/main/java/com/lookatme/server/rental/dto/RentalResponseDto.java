package com.lookatme.server.rental.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RentalResponseDto {
    private int rentalId;
    private String size;
    private int rentalPrice;
    private boolean available;
}
