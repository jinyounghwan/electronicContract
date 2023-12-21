package com.samsung.framework.vo.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class PagingVO {

    private int totalCount;
    private int totalPage;
}
