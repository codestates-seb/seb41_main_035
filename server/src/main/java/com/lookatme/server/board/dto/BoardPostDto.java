package com.lookatme.server.board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardPostDto {

    private String content;

    private String userImage;

    private String productName;

    private int sellingPrice;

    private String link;

    private boolean rental;

    private String size;

    private int rentalPrice;

}
