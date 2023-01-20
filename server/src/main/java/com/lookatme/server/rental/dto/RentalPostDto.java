package com.lookatme.server.rental.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RentalPostDto {

    private boolean rental;

    private int size;

    private int price;
}
