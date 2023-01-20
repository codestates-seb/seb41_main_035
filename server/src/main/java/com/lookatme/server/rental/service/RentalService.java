package com.lookatme.server.rental.service;

import com.lookatme.server.rental.entity.Rental;
import com.lookatme.server.rental.repository.RentalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RentalService {
    private final RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public Rental createRental(Rental rental) {
        verifyExistRental(rental.getRentalId());

        return rentalRepository.save(rental);
    }

    public Rental updateRental(Rental rental) {
        Rental findRental = findExistedRental(rental.getRentalId());

        Optional.ofNullable(rental.isRental())
                .ifPresent(rental -> findRental.setRental(rental));

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
}
