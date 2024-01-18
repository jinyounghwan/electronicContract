package com.samsung.framework.mapper.record;

import com.samsung.framework.vo.record.RecordVO;
import com.samsung.framework.vo.search.SearchVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RecordMapper {

    /**
     * 이력 내역 total
     * @param searchVO
     * @return
     */
    int findAllTotalCount(SearchVO searchVO);

    /**
     * 이력 목록 조회
     * @param searchVO
     * @return
     */
    List<RecordVO> findAll(SearchVO searchVO);

    /**
     * 이력 단 건 조회
     * @param logSeq
     * @return
     */
    RecordVO findById(long logSeq);


}
