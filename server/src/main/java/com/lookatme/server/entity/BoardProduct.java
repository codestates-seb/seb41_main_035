package com.lookatme.server.entity;

import com.lookatme.server.audit.BaseTimeEntity;
import com.lookatme.server.board.entity.Board;
import com.lookatme.server.product.entity.Product;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class BoardProduct extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int boardproductId;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    protected BoardProduct() {
    }

    public BoardProduct(Board board, Product product) {
        this.board = board;
        this.product = product;
    }
}
