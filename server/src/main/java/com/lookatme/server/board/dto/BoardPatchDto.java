package com.lookatme.server.board.dto;

import com.lookatme.server.product.dto.ProductPatchDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardPatchDto {
    private String content;
    private MultipartFile userImage;
    private List<ProductPatchDto> products;
}
