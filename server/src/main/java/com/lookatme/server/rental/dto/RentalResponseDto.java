package com.lookatme.server.rental.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RentalResponseDto {
    private long rentalId;
    private String size;
    private int rentalPrice;
    private boolean available;
}
