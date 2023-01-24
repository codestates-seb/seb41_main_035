package com.lookatme.server.product.entity;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long brandId;

    private String name;

    @OneToMany(mappedBy = "brand")
    private List<Product> products = new ArrayList<>();

    protected Brand() {}

    public Brand(String name) {
        this.name = name;
    }

}
