package com.gongyeon.gongyeon.models.pagination;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaginationResponse {
    private int totalRow = 0;
    private int currentPage = defaultPage;
    private int pageSize = defaultPageSize;
    private int totalPages = (int)Math.ceil(totalRow / (double) pageSize);

    private static final int defaultPage = 1;
    private static final int defaultPageSize = 10;

    public PaginationResponse(int totalRow) {
        this.totalRow = totalRow;
    }

    public PaginationResponse(int totalRow, PaginationRequest request) {
        this.totalRow = totalRow;
        this.currentPage = request.getPage();
        this.pageSize = request.getPageSize();
    }
}