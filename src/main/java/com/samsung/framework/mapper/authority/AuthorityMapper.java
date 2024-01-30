package com.samsung.framework.mapper.authority;

import com.samsung.framework.vo.authority.MenuAuthorityVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AuthorityMapper {

    /**
     * Bulk 사용자 메뉴 권한 저장
     * @param authorityList
     * @return
     */
    int saveMenuAuthList(List<MenuAuthorityVO> authorityList);

}
