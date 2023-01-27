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

    public Rental createRental(long memberId, long productId, Board board, String size, int rentalPrice, boolean isRental) {
        Rental rental = Rental.builder()
                .member(findMember(memberId))
                .product(findProduct(productId))
                .board(board)
                .available(isRental)
                .size(size)
                .rentalPrice(rentalPrice).build();
        return rentalRepository.save(rental);
    }


    public Rental createRental(Rental rental) {
        verifyExistRental(rental.getRentalId());
        return rentalRepository.save(rental);
    }

    public Rental updateRental(long boardId, long productId, RentalPatchDto patch) {
        Rental savedRental = findRentalByBoardId(boardId, productId);
        String size = patch.getSize() == null ? "-" : patch.getSize();
        savedRental.updateRental(size, patch.getRentalPrice(), patch.isRental());
        return savedRental;
    }

    public void deleteRental(int rentalId) {
        rentalRepository.delete(findExistedRental(rentalId));
    }

    public void deleteRentals() {
        rentalRepository.deleteAll();
    }

    public Rental findRentalByBoardId(long boardId, long productId) {
        return rentalRepository.findByBoard_BoardIdAndProduct_ProductId(boardId, productId)
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.RENTAL_NOT_FOUND));
    }

    public Rental findRental(long rentalId) {
        return findExistedRental(rentalId);
    }

    public Page<Rental> findRentals(int page, int size) {
        return rentalRepository.findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }

    private void verifyExistRental(long rentalId) {
        rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.RENTAL_ALREADY_EXISTS));
    }

    private Rental findExistedRental(long rentalId) {
        return rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.RENTAL_NOT_FOUND));
    }

    private Member findMember(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private Product findProduct(long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.PRODUCT_NOT_FOUND));
    }
}
