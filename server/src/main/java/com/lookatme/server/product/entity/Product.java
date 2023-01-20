package com.lookatme.server.product.entity;
import com.lookatme.server.entity.BoardProduct;
import com.lookatme.server.rental.entity.Rental;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private int sellingPrice;

    @Column(nullable = false)
    private String link;

    @OneToMany(mappedBy = "product")
    private List<BoardProduct> BoardProducts = new ArrayList<>();

    @OneToMany(fetch=FetchType.LAZY)
    private List<Rental> rental;
}
