package com.lookatme.server.rental.mapper;

import com.lookatme.server.rental.dto.RentalPatchDto;
import com.lookatme.server.rental.dto.RentalPostDto;
import com.lookatme.server.rental.dto.RentalResponseDto;
import com.lookatme.server.rental.entity.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface RentalMapper {
    Rental rentalPostToRental(RentalPostDto post);
    Rental rentalPatchToRental(RentalPatchDto patch);
    RentalResponseDto rentalToRentalResponse(Rental rental);
    List<RentalResponseDto> rentalToRentalResponseDtos(List<Rental> rentals);
}
