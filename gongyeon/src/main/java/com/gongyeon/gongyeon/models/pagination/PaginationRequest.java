package com.gongyeon.gongyeon.models.pagination;

import com.gongyeon.gongyeon.enums.HttpStatusEnum;
import com.gongyeon.gongyeon.exception.GongYeonException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaginationRequest {
    private int page;
    private int pageSize;

    public int getOffset() {
        if (page < 1) throw new GongYeonException(HttpStatusEnum.BAD_REQUEST, "Page Must Be Over Than 0");
        return (page - 1) * pageSize;
    }
}