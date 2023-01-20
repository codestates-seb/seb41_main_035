package com.lookatme.server.product.mapper;

import com.lookatme.server.product.dto.ProductPostDto;
import com.lookatme.server.product.entity.Product;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-01-20T15:02:15+0900",
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
        product.setLink( post.getLink() );
        product.setProductImage( post.getProductImage() );

        return product;
    }
}
