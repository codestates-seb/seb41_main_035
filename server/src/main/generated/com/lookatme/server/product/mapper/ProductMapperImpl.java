package com.lookatme.server.product.mapper;

import com.lookatme.server.product.dto.ProductPatchDto;
import com.lookatme.server.product.dto.ProductPostDto;
import com.lookatme.server.product.dto.ProductResponseDto;
import com.lookatme.server.product.entity.Product;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-01-26T15:16:16+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.16.1 (Eclipse Adoptium)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product productPostToProduct(ProductPostDto post) {
        if ( post == null ) {
            return null;
        }

        Product product = new Product();

        product.setProductName( post.getProductName() );
        product.setPrice( post.getPrice() );
        product.setLink( post.getLink() );

        return product;
    }

    @Override
    public ProductPostDto productPatchToProductPost(ProductPatchDto patchDto) {
        if ( patchDto == null ) {
            return null;
        }

        ProductPostDto productPostDto = new ProductPostDto();

        productPostDto.setProductImage( patchDto.getProductImage() );
        productPostDto.setProductName( patchDto.getProductName() );
        productPostDto.setCategory( patchDto.getCategory() );
        productPostDto.setBrand( patchDto.getBrand() );
        productPostDto.setSize( patchDto.getSize() );
        productPostDto.setLink( patchDto.getLink() );
        productPostDto.setPrice( patchDto.getPrice() );
        productPostDto.setRentalPrice( patchDto.getRentalPrice() );
        productPostDto.setRental( patchDto.isRental() );

        return productPostDto;
    }

    @Override
    public List<ProductResponseDto> ProductsToProductResponseDtos(List<Product> product) {
        if ( product == null ) {
            return null;
        }

        List<ProductResponseDto> list = new ArrayList<ProductResponseDto>( product.size() );
        for ( Product product1 : product ) {
            list.add( productToProductResponseDto( product1 ) );
        }

        return list;
    }

    protected ProductResponseDto productToProductResponseDto(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductResponseDto productResponseDto = new ProductResponseDto();

        productResponseDto.setProductName( product.getProductName() );
        productResponseDto.setProductImage( product.getProductImage() );
        productResponseDto.setLink( product.getLink() );

        return productResponseDto;
    }
}
