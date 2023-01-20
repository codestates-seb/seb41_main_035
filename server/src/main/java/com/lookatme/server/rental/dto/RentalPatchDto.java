package com.lookatme.server.rental.dto;

import lombok.Getter;

@Getter
public class RentalPatchDto {

    // 수정할 수 있는 내용이 어떤게 있을지 이야기 해보자
    private int rentalId;

    private boolean rental;

    private int size;

    private int price;

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }
}
