package com.lookatme.server.rental.controller;

import com.lookatme.server.common.dto.MultiResponseDto;
import com.lookatme.server.board.dto.BoardPatchDto;
import com.lookatme.server.board.entity.Board;
import com.lookatme.server.board.mapper.BoardMapper;
import com.lookatme.server.board.service.BoardService;
import com.lookatme.server.rental.dto.RentalPatchDto;
import com.lookatme.server.rental.entity.Rental;
import com.lookatme.server.rental.mapper.RentalMapper;
import com.lookatme.server.rental.service.RentalService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/rentals")
public class RentalController {
    private final RentalService rentalService;

    private final RentalMapper rentalMapper;

    public RentalController(RentalService rentalService, RentalMapper rentalMapper) {
        this.rentalService = rentalService;
        this.rentalMapper = rentalMapper;
    }

//    @PostMapping
//    public ResponseEntity postBoard(@Valid @RequestBody BoardPostDto post){
//        Board createBoard = boardService.crea
//
//        BoardResponseDto boardResponseDto = boardMapper.BOARD(createBoard);
//
//        return new ResponseEntity<>(boardMapper.boardToBoardResponse(createBoard), HttpStatus.CREATED);
//    }

    @PatchMapping("/rental-Id")
    public ResponseEntity patchRental(@PathVariable("rental-Id") int rentalId,
                                     @Valid @RequestBody RentalPatchDto patch) {
        Rental rental = rentalService.updateRental(rentalMapper.rentalPatchToRental(patch));

        return new ResponseEntity<>(rentalMapper.rentalToRentalResponse(rental), HttpStatus.OK);
    }

    @GetMapping("/rental-Id")
    public ResponseEntity getRental(@PathVariable("rental-Id") int rentalId) {
        Rental rental = rentalService.findRental(rentalId);

        return new ResponseEntity<>(rentalMapper.rentalToRentalResponse(rental), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity geRentals(@Positive @RequestParam int page,
                                    @Positive @RequestParam int size) {
        Page<Rental> findRentals = rentalService.findRentals(page -1, size);
        // ?
        List<Rental> rentals = findRentals.getContent();

        return new ResponseEntity<>(
                new MultiResponseDto<>(rentalMapper.rentalToRentalResponseDtos(rentals), findRentals), HttpStatus.OK);

    }

    @DeleteMapping("/{rental-Id}")
    public ResponseEntity deleteRental(@PathVariable("rental-Id") int rentalId) {
        rentalService.deleteRental(rentalId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity deleteRental() {
        rentalService.deleteRentals();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

