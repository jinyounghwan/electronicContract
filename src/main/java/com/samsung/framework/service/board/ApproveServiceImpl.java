package com.samsung.framework.service.board;

import com.samsung.framework.common.utils.DateUtil;
import com.samsung.framework.service.common.ParentService;
import com.samsung.framework.vo.board.BoardPublicVO;
import com.samsung.framework.vo.common.SelectOptionVO;
import com.samsung.framework.vo.search.board.BoardSearchVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Primary
@Component
public class ApproveServiceImpl extends ParentService implements ApproveService {

    @Override
    public List<BoardPublicVO> findAll(BoardSearchVO boardSearchVO) {
        int totalCount = this.totalCount(boardSearchVO);
        if(totalCount < 1) {
            return new ArrayList<>();
        }

        List<BoardPublicVO> list = getCommonMapper().getApproveMapper().findAll(boardSearchVO);
        list.forEach(ele -> {
            ele.setRegDtStr(DateUtil.convertLocalDateTimeToString(ele.getRegDt(), DateUtil.DATETIME_YMDHM_PATTERN));
            ele.setLastDtStr(DateUtil.convertLocalDateTimeToString(ele.getLastDt(), DateUtil.DATETIME_YMDHM_PATTERN));
            if(!ele.getApproveState().isBlank()) {
                ele.setApproveStateInKorean();
                if(ele.getApproveState().equalsIgnoreCase("HDLE0404")) {
                    ele.setCompleteDtStr(ele.getLastDtStr());
                }else {
                    ele.setCompleteDtStr("-");
                }
            }
        });

        return list;
    }

    @Override
    public List<SelectOptionVO> getSearchStateTypeOptionList() {
        var list = new ArrayList<SelectOptionVO>();
        list.add(new SelectOptionVO(0, "ALL", "전체"));
        list.add(new SelectOptionVO(0, "HDLE0402", "대기"));
        list.add(new SelectOptionVO(0, "HDLE0403", "진행중"));
        list.add(new SelectOptionVO(0, "HDLE0404", "완료"));
        list.add(new SelectOptionVO(0, "HDLE0405", "승인"));
        list.add(new SelectOptionVO(0, "HDLE0406", "반려"));

        return list;
    }

    public int totalCount(BoardSearchVO boardSearchVO) {
        return getCommonMapper().getApproveMapper().findAllTotalCount(boardSearchVO);
    }

    @Override
    public Map<String, Object> deleteBySeq(String lastId, List<Integer> tgtList) {
        var result = new HashMap<String, Object>();
        result.put("code", 200);

        var paramMap = new HashMap<String, Object>();
        paramMap.put("lastId", lastId);
        paramMap.put("boardSeqList", tgtList);

        int deleteCount = getCommonMapper().getBoardMapper().deleteList(paramMap);
        if(deleteCount < 1) {
            result.put("code", 204);
            result.put("message", "삭제할 대상이 없습니다.");
            return result;
        }

        paramMap.put("seqList",tgtList); // 파일 입출력을 위한 seqList Setting
        getCommonMapper().getFileMapper().deleteFileList(paramMap);
        result.put("message", "삭제 되었습니다.");


        return result;
    }

}
