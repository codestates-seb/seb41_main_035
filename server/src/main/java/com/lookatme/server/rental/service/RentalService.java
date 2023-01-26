package com.lookatme.server.rental.service;

import com.lookatme.server.board.entity.Board;
import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.exception.ErrorLogicException;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.repository.MemberRepository;
import com.lookatme.server.product.entity.Product;
import com.lookatme.server.product.repository.ProductRepository;
import com.lookatme.server.rental.dto.RentalPatchDto;
import com.lookatme.server.rental.entity.Rental;
import com.lookatme.server.rental.repository.RentalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
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
                .rental(true)
                .size(size)
                .rentalPrice(rentalPrice).build();
        return rentalRepository.save(rental);
    }


    public Rental createRental(Rental rental) {
        verifyExistRental(rental.getRentalId());
        return rentalRepository.save(rental);
    }

    public Rental updateRental(RentalPatchDto patch) {
        Rental savedRental = findRental(patch.getRentalId());
        savedRental.updateRental(patch.getSize(), patch.getRentalPrice(), patch.isRental());
        return savedRental;
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
        rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.RENTAL_ALREADY_EXISTS));
    }

    private Rental findExistedRental(int rentalId) {
        return rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.RENTAL_NOT_FOUND));
    }

    private Member findMember(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private Product findProduct(int productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.PRODUCT_NOT_FOUND));
    }
}
