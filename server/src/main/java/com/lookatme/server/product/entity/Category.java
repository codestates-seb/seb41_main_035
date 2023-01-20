package com.lookatme.server.product.entity;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long categoryId;

    private String name;

    protected Category() {}

    public Category(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();
}
