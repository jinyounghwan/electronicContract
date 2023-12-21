package com.samsung.framework.service.record;

import com.samsung.framework.vo.common.SelectOptionVO;
import com.samsung.framework.vo.record.RecordVO;
import com.samsung.framework.vo.search.record.RecordSearchVO;

import java.util.List;

public interface RecordService {
    // [검색옵션] 날짜
    List<SelectOptionVO> getSearchDateRangeOptionList();

    // [검색옵션] 키워드
    List<SelectOptionVO> getSearchKeywordTypeList();

    // 이력 전체 total 조회
    int totalCount(RecordSearchVO recordSearchVO);

    // 이력 목록 조회
    List<RecordVO> findAll(RecordSearchVO recordSearchVO);

    // 이력 단 건 조회
    RecordVO findById(long seq);

}
