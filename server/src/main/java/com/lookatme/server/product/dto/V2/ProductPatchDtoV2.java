package com.lookatme.server.product.dto.V2;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class ProductPatchDtoV2 {

    private MultipartFile productImage;

    @NotBlank
    private String productName;

    @NotBlank
    private String category;

    @NotBlank
    private String brand;
}
