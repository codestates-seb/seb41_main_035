package com.lookatme.server.common.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PageInfoDto {
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    private PageInfoDto() {

    }

    @Builder
    public PageInfoDto(final int page,
                    final int size,
                    final long totalElements,
                    final int totalPages) {
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }
}
