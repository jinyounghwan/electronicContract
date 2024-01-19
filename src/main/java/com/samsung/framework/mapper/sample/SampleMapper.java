package com.samsung.framework.mapper.sample;

import com.samsung.framework.vo.search.SearchVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * The interface Sample mapper.
 */
@Mapper
public interface SampleMapper {
    /**
     * Gets sample list total.
     *
     * @param searchVO the search vo
     * @return the sample list total
     */
    int getSampleListTotal(SearchVO searchVO);

    /**
     * Gets sample list.
     *
     * @param searchVO the search vo
     * @return the sample list
     */
    List<Map<String, Object>> getSampleList(SearchVO searchVO);
}
