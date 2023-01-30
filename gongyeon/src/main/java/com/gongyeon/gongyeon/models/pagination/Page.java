package com.gongyeon.gongyeon.models.pagination;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class Page<T> {
    private List<T> list;
    private PaginationResponse pagination;

    public Page(List<T> newList) {
        this.list = newList;
        this.pagination = new PaginationResponse();
    }
}