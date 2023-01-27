package com.lookatme.server.product.mapper;

import com.lookatme.server.product.dto.ProductPatchDto;
import com.lookatme.server.product.dto.ProductPostDto;
import com.lookatme.server.product.dto.ProductResponseDto;
import com.lookatme.server.product.dto.V2.ProductPostDtoV2;
import com.lookatme.server.product.dto.V2.ProductResponseDtoV2;
import com.lookatme.server.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mappings({
            @Mapping(target = "category", ignore = true),
            @Mapping(target = "brand", ignore = true),
            @Mapping(target = "rentals", ignore = true),
            @Mapping(target = "productImage", ignore = true)
    })
    Product productPostToProduct(ProductPostDto post);

    ProductPostDto productPatchToProductPost(ProductPatchDto patchDto);
//    Product productPatchToProduct(ProductPatchDto patch);
//    ProductResponseDto productToProductResponseDto(Product product);
    List<ProductResponseDto> ProductsToProductResponseDtos(List<Product> product);


    @Mappings({
            @Mapping(target = "category", ignore = true),
            @Mapping(target = "brand", ignore = true),
            @Mapping(target = "productImage", ignore = true)
    })
    Product productPostV2ToProduct(ProductPostDtoV2 post);

    @Mappings({
            @Mapping(target = "category", source = "categoryName"),
            @Mapping(target = "brand", source = "brandName")
    })
    ProductResponseDtoV2 productToProductResponseV2(Product product);

    @Mappings({
            @Mapping(target = "category", source = "categoryName"),
            @Mapping(target = "brand", source = "brandName")
    })
    List<ProductResponseDtoV2> productListToProductResponseListV2(List<Product> productList);
}
