package com.lookatme.server.common.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class MultiResponseDto<T> {
    private List<T> data;
    private PageInfoDto pageInfoDto;

    private MultiResponseDto() {

    }

    public MultiResponseDto(final List<T> data,
                            final Page page) {
        this.data = data;
        this.pageInfoDto = PageInfoDto.builder()
                .page(page.getNumber() + 1)
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

}
