package com.lookatme.server.product.mapper;

import com.lookatme.server.product.dto.ProductPostDto;
import com.lookatme.server.product.dto.ProductResponseDto;
import com.lookatme.server.product.entity.Product;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-01-25T21:09:11+0900",
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
