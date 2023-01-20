package com.lookatme.server.product.mapper;

import com.lookatme.server.board.dto.BoardResponseDto;
import com.lookatme.server.board.entity.Board;
import com.lookatme.server.product.dto.ProductPatchDto;
import com.lookatme.server.product.dto.ProductPostDto;
import com.lookatme.server.product.dto.ProductResponseDto;
import com.lookatme.server.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
    Product productPostTOProduct(ProductPostDto post);
    Product productPatchToProduct(ProductPatchDto patch);
    ProductResponseDto productToProductResponseDto(Product product);
    List<ProductResponseDto> ProductsToProductResponseDtos(List<Product> product);
}
