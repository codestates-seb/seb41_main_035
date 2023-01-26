package com.lookatme.server.board.entity;

import com.lookatme.server.audit.BaseTimeEntity;
import com.lookatme.server.board.entity.Board;
import com.lookatme.server.product.entity.Product;
import com.lookatme.server.rental.entity.Rental;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
public class BoardProduct extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int boardproductId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;

    protected BoardProduct() {
    }

    public BoardProduct(Board board, Product product) {
        this.board = board;
        this.product = product;
    }

    public int getProductId() {
        return product.getProductId();
    }

    public String getProductName() {
        return product.getProductName();
    }

    public String getLink() {
        return product.getLink();
    }

    public int getPrice() {
        return product.getPrice();
    }

    public String getProductImage() {
        return product.getProductImage();
    }

    public String getCategory() {
        return product.getCategory().getName();
    }

    public String getBrand() {
        return product.getBrand().getName();
    }

    public Rental getRental() {
        return product.getRentals().stream()
                .filter(rental -> rental.getBoard().equals(board))
                .findFirst()
                .orElse(null);
    }

    public void addProduct() {
        this.getBoard().getBoardProducts().add(this);
    }

    // 상품을 삭제하면 BoardProduct도 삭제하되 게시글은 삭제하면 안됨
    public void removeProduct() {
        this.getBoard().getBoardProducts().remove(this);
    }
}
