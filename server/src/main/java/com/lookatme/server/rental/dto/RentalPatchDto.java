package com.lookatme.server.rental.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RentalPatchDto {
    private int rentalPrice;
    private String size;
    private boolean rental;
}
