package com.lookatme.server.product.entity;

import com.lookatme.server.board.entity.BoardProduct;
import com.lookatme.server.rental.entity.Rental;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NamedEntityGraph(
        name = "product-entity-graph", // product 조회 시 category + brand를 한꺼번에 긁어옴
        attributeNodes = {
                @NamedAttributeNode("category"),
                @NamedAttributeNode("brand")
        }
)
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long productId;

    @Column(nullable = false)
    private String productName;

    private String productImage;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<BoardProduct> BoardProducts = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Rental> rentals = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    public void updateProduct(String productName, String productImage) {
        this.productName = productName;
        this.productImage = productImage;
    }

    public void updateProductV2(String productName, String productImage, Category category, Brand brand) {
        this.productName = productName;
        this.productImage = productImage;
        this.category = category;
        this.brand = brand;
    }

    public String getCategoryName() {
        return category.getName();
    }

    public String getBrandName() {
        return brand.getName();
    }
}
