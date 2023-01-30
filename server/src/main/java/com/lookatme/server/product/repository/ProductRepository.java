package com.lookatme.server.product.repository;

import com.lookatme.server.board.entity.Board;
import com.lookatme.server.product.entity.Product;
import com.lookatme.server.rental.entity.Rental;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByProductName(String productName);

    boolean existsByProductName(String productName);

//    Page<Product> findByProduct_Rentals(Rental rental, Pageable pageable);

    @EntityGraph(value = "product-entity-graph")
    Optional<Product> findByProductId(long productId);

    @EntityGraph(value = "product-entity-graph")
    Page<Product> findAll(Pageable pageable);

    @EntityGraph(value = "product-entity-graph")
    @Query("select p from Product p left join BoardProduct bp on p.productId = bp.product.productId where bp.board.boardId=:boardId")
    Page<Product> findByBoardId(@Param("boardId") long boardId, Pageable pageable);
}
