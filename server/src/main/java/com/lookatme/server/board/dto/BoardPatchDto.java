package com.lookatme.server.board.dto;

import com.lookatme.server.product.dto.ProductPatchDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class BoardPatchDto {
    private String content;
    private MultipartFile userImage;
    private List<ProductPatchDto> products;
}
