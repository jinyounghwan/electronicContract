package com.samsung.framework.mapper.contract.template;

import com.samsung.framework.domain.board.Board;
import com.samsung.framework.domain.board.BoardPublic;
import com.samsung.framework.mapper.common.GeneralMapper;
import com.samsung.framework.vo.board.BoardPublicVO;
import com.samsung.framework.vo.board.BoardReplyVO;
import com.samsung.framework.vo.file.FileVO;
import com.samsung.framework.vo.search.SearchVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ContractTemplateMapper {

    /**
     * 계약서 템플릿 코드 조회
     * @param target
     * @return
     */
    int getContractTemplateCode(int target);
}
