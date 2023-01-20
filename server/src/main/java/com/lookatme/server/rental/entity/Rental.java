package com.lookatme.server.rental.entity;

import com.lookatme.server.audit.BaseTimeEntity;
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
    private int rentalId;

    @Column(nullable = false)
    private boolean rental;

    @Column(nullable = false)
    private int size;

    @Column(nullable = false)
    private int rentalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_Id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_Id")
    private Member member;
}
