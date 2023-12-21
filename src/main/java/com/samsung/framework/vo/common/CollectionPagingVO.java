package com.samsung.framework.vo.common;

import com.samsung.framework.domain.common.Paging;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Collection;
import java.util.List;

/**
 * Collection 객체 Wrapper
 * <pre>
 * 리스트와 페이징을 같이 리턴하는 객체
 * </pre>
 * @field objects - {@link List}, {@link java.util.Set}, {@link java.util.Queue}
 * @field paging - {@link Paging}
 */
@AllArgsConstructor
@Getter
@Builder
public class CollectionPagingVO {
    private Collection<?> objects;
    private Paging paging;
}
