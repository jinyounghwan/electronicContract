package com.samsung.framework.mapper.code;

import com.samsung.framework.domain.code.Code;
import com.samsung.framework.domain.code.CommonCode;
import com.samsung.framework.vo.code.CodePublicVO;
import com.samsung.framework.vo.code.CommonCodeVO;
import com.samsung.framework.vo.common.SelectOptionVO;
import com.samsung.framework.vo.search.SearchVO;
import com.samsung.framework.vo.search.code.CodeSearchVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CodeMapper {

    /**
     * 코드 순서 일괄변경
     * @return
     */
    int updateCodeOrder(Code code);

    /**
     * 사이드바 메뉴 조회
     * @return
     */
    List<CodePublicVO> listCode();

    /**
     * 코드 그룹 조회
     * @return
     */
    List<SelectOptionVO> commonCodeGroupList();

    /**
     * 공통 코드 구분(카테고리)
     * @return
     */
    List<SelectOptionVO> commonCodeCategoryList(String code);

    /**
     * 공통 코드 목록 조회
     * @param codeSearchVO
     * @return
     */
    List<CommonCodeVO> findAll(CodeSearchVO codeSearchVO);

    /**
     * 공통 코드 저장
     * @param commonCode
     * @return
     */
    int saveCommonCode(CommonCode commonCode);
    /**
     * 공통 코드 수정
     * @param commonCode
     * @return
     */
    int updateCommonCode(CommonCode commonCode);

    /**
     * 단건 조회
     * @param search
     * @return
     */
    Object rowBySearch(SearchVO search);

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
     * 페이지 목록 조회  건수
     * @param search
     * @return
     */
    int pagingCountBySearch(SearchVO search);

    /**
     * 페이지 목록 조회
     * @param search
     * @return
     */
    List<?> pagingBySearch(SearchVO search);
}
