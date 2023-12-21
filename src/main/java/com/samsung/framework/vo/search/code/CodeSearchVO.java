package com.samsung.framework.vo.search.code;

import com.samsung.framework.vo.search.SearchVO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeSearchVO extends SearchVO {

    /**
     * 1. 코드 8자리에서 앞 4자리(부모코드)로 자식코드 목록 검색시 사용
     * 2. 0000인 경우는 뒤에 4자리 0000인 그룹코드 목록 검색
     */
    private String parentCode;

    /**
     * 8자리 코드 (ex: 00010001)
     */
    private String code;

    /**
     * 코드명
     */
    private String codeName;

    /**
     * 코드 설명1
     */
    private String remark1;


}
