package com.lookatme.server.board.repository;

import com.lookatme.server.board.entity.Board;
import com.lookatme.server.board.entity.BoardProduct;
import com.lookatme.server.product.entity.Category;
import com.lookatme.server.rental.entity.Rental;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardProductRepository extends JpaRepository<BoardProduct, Long> {
    Page<BoardProduct> findByProduct_Category(Category category, Pageable pageable);

    Page<BoardProduct> findByProduct_ProductName(String productName, Pageable pageable);
}
