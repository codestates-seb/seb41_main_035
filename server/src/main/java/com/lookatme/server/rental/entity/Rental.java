package com.lookatme.server.rental.entity;

import com.lookatme.server.audit.BaseTimeEntity;
import com.lookatme.server.board.entity.Board;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.product.entity.Product;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rentals")
public class Rental extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long rentalId;

    @Column(nullable = false)
    private String size;

    @Column(nullable = false)
    private int rentalPrice;

    private boolean available = true; // 렌탈 가능 유무

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "product_id",
            foreignKey = @ForeignKey(name = "FK_product")
    )
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "member_id",
            foreignKey = @ForeignKey(name = "FK_member")
    )
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "board_id",
            foreignKey = @ForeignKey(name = "FK_board")
    )
    private Board board;

    public void updateRental(String size, int rentalPrice, boolean available) {
        this.size = size;
        this.rentalPrice = rentalPrice;
        this.available = available;
    }
}
