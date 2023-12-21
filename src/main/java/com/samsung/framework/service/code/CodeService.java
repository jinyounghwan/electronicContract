package com.samsung.framework.service.code;

import com.samsung.framework.vo.code.CodePublicVO;
import com.samsung.framework.vo.code.CommonCodeVO;
import com.samsung.framework.vo.common.SelectOptionVO;
import com.samsung.framework.vo.search.code.CodeSearchVO;

import java.util.List;
import java.util.Map;

public interface CodeService {
   // 사이드바 메뉴 조회
   List<CodePublicVO> listCode();

   // 공통 코드 그룹
   List<SelectOptionVO> commonCodeGroupList();

   // 공통 코드 구분(카테고리)
   List<SelectOptionVO> commonCodeCategoryList(String code);

   // 공통 코드 조회
   List<CommonCodeVO> findAll(CodeSearchVO codeSearchVO);

   // 공통 코드 수정
   Map<String, Object> updateCommonCode(List<CommonCodeVO> tgt);
}
