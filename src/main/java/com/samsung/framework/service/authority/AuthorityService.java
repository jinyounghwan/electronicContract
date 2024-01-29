package com.samsung.framework.service.authority;

import com.samsung.framework.domain.authority.Authority;
import com.samsung.framework.vo.code.CommonCodeVO;
import com.samsung.framework.vo.file.FilePublicVO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface AuthorityService {
    // 메뉴 조회
    List<CommonCodeVO> findMenuList(String deptCd);

    // 부서 목록 조회
    List<CommonCodeVO> findDepartmentList();

    // 개인 권한 저장
    Map<String, Object> saveIndividualAuthority(List<Authority> list);

    // 그룹 권한 저장
    Map<String, Object> saveGroupAuthority(List<Authority> list);
}
