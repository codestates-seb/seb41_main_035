package com.lookatme.server.rental.repository;

import com.lookatme.server.rental.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    Optional<Rental> findById(long rentalId);

    Optional<Rental> findByBoard_BoardIdAndProduct_ProductId(long boardId, long productId);

}
