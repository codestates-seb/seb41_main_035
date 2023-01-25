package com.lookatme.server.rental.service;

import com.lookatme.server.board.entity.Board;
import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.exception.ErrorLogicException;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.repository.MemberRepository;
import com.lookatme.server.product.entity.Product;
import com.lookatme.server.product.repository.ProductRepository;
import com.lookatme.server.rental.entity.Rental;
import com.lookatme.server.rental.repository.RentalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public RentalService(RentalRepository rentalRepository, MemberRepository memberRepository, ProductRepository productRepository) {
        this.rentalRepository = rentalRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    public Rental createRental(long memberId, int productId, Board board, String size, int rentalPrice) {
        Rental rental = Rental.builder()
                .member(findMember(memberId))
                .product(findProduct(productId))
                .board(board)
                .rental(false)
                .size(size)
                .rentalPrice(rentalPrice).build();

        return rentalRepository.save(rental);
    }


    public Rental createRental(Rental rental) {
        verifyExistRental(rental.getRentalId());
        return rentalRepository.save(rental);
    }

    public Rental updateRental(Rental rental) {
        Rental findRental = findExistedRental(rental.getRentalId());

        Optional.ofNullable(rental.isRental())
                .ifPresent(flag -> findRental.setRental(flag));

        Optional.ofNullable(rental.getSize())
                .ifPresent(size -> findRental.setSize(size));

        Optional.ofNullable(rental.getRentalPrice())
                .ifPresent(rentalPrice -> findRental.setRentalPrice(rentalPrice));

        return rentalRepository.save(findRental);
    }

    public void deleteRental(int rentalId) {

        rentalRepository.delete(findExistedRental(rentalId));
    }

    public void deleteRentals() {

        rentalRepository.deleteAll();
    }

    public Rental findRental(int rentalId) {

        return findExistedRental(rentalId);
    }

    public Page<Rental> findRentals(int page, int size) {
        return rentalRepository.findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }

    private void verifyExistRental(int rentalId) {
        Optional<Rental> optionalPost = rentalRepository.findById(rentalId);

        if (optionalPost.isPresent()) {
            throw new RuntimeException("Rental_ALREADY_EXIST");
        }
    }

    private Rental findExistedRental(int rentalId) {
        Optional<Rental> optionalPost = rentalRepository.findById(rentalId);

        return optionalPost.orElseThrow(
                () -> new RuntimeException("Rental_NOT_FOUND")
        );
    }

    private Member findMember(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private Product findProduct(int productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException());
    }
}
