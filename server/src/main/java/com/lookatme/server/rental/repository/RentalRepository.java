package com.lookatme.server.rental.repository;

import com.lookatme.server.rental.entity.Rental;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    Optional<Rental> findById(long rentalId);
    Page<Rental> findByAvailableTrue(Pageable pageable);
    Optional<Rental> findByBoard_BoardIdAndProduct_ProductId(long boardId, long productId);

    List<Rental> findByMember_MemberId(long memberId);
}
