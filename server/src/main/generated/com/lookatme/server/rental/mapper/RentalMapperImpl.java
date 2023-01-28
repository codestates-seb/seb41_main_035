package com.lookatme.server.rental.mapper;

import com.lookatme.server.rental.dto.RentalPatchDto;
import com.lookatme.server.rental.dto.RentalPostDto;
import com.lookatme.server.rental.dto.RentalResponseDto;
import com.lookatme.server.rental.entity.Rental;
import com.lookatme.server.rental.entity.Rental.RentalBuilder;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-01-28T23:57:46+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.16.1 (Eclipse Adoptium)"
)
@Component
public class RentalMapperImpl implements RentalMapper {

    @Override
    public Rental rentalPostToRental(RentalPostDto post) {
        if ( post == null ) {
            return null;
        }

        RentalBuilder rental = Rental.builder();

        rental.size( String.valueOf( post.getSize() ) );

        return rental.build();
    }

    @Override
    public Rental rentalPatchToRental(RentalPatchDto patch) {
        if ( patch == null ) {
            return null;
        }

        RentalBuilder rental = Rental.builder();

        rental.size( patch.getSize() );
        rental.rentalPrice( patch.getRentalPrice() );

        return rental.build();
    }

    @Override
    public RentalResponseDto rentalToRentalResponse(Rental rental) {
        if ( rental == null ) {
            return null;
        }

        RentalResponseDto rentalResponseDto = new RentalResponseDto();

        rentalResponseDto.setRentalId( rental.getRentalId() );
        rentalResponseDto.setSize( rental.getSize() );
        rentalResponseDto.setRentalPrice( rental.getRentalPrice() );
        rentalResponseDto.setAvailable( rental.isAvailable() );

        return rentalResponseDto;
    }

    @Override
    public List<RentalResponseDto> rentalToRentalResponseDtos(List<Rental> rentals) {
        if ( rentals == null ) {
            return null;
        }

        List<RentalResponseDto> list = new ArrayList<RentalResponseDto>( rentals.size() );
        for ( Rental rental : rentals ) {
            list.add( rentalToRentalResponse( rental ) );
        }

        return list;
    }
}
