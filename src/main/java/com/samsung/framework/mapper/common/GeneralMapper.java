package com.samsung.framework.mapper.common;

import com.samsung.framework.vo.search.SearchVO;

import java.util.List;

/**
 * Mapper 일반화 인터페이스
 */
public interface GeneralMapper {


    /**
     * 단건 조회
     * @param id
     * @return
     */
    Object rowByKey(Object id);


    /**
     * 단건 조회
     * @param search
     * @return
     */
    Object rowBySearch(SearchVO search);

    /**
     * 목록 조회
     * @param search
     * @return
     */
    List<?> listBySearch(SearchVO search);

    /**
     * 페이지 목록 조회
     * @param search
     * @return
     */
    List<?> pagingBySearch(SearchVO search);

    /**
     * 페이지 목록 조회  건수
     * @param search
     * @return
     */
    int pagingCountBySearch(SearchVO search);

    /**
     * 저장
     * @param obj
     * @return
     */
//    @EntityLog
    int insert(Object obj);

    /**
     * 수정
     * @param obj
     * @return
     */
//    @EntityLog
    int update(Object obj);

    /**
     * merge
     * @param obj
     * @return
     */
    int merge(Object obj);

    /**
     * 삭제
     * @param key
     * @return
     */
//    @EntityLog
    int delete(Object key);
}
