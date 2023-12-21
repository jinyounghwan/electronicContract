package com.samsung.framework.common.utils;

import com.samsung.framework.common.enums.ExceptionCodeMsgEnum;
import com.samsung.framework.common.enums.MapKeyStringEnum;
import com.samsung.framework.vo.common.ResultStatusVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Slf4j
@Component
public class MapUtil {

    /**
     * ResponseEntity 응답데이터 Wrapper
     * @param resultStatusVO {@link ResultStatusVO} 응답상태 객체
     * @param mapKeyList {@link List} Map 키값 List 컬렉션
     * @param objects Objects[] 데이터 객체
     * @return {@link Map}
     */
    public Map<String, Object> responseEntityBodyWrapper(ResultStatusVO resultStatusVO, List<String> mapKeyList, Object... objects) {

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(MapKeyStringEnum.RESULT_STATUS.getKeyString(), resultStatusVO);

        //Http Method : POST, PUT, DELETE인 경우만 사용
        if (StringUtil.isEmpty(mapKeyList) && StringUtil.isEmpty(objects)) {
            return resultMap;
        }

        var dataList = Arrays.asList(objects);
        if(mapKeyList.size() != dataList.size()) {
            throw new IllegalArgumentException(ExceptionCodeMsgEnum.NOT_EQUAL_OBJECT_SIZE.getMsg());
        }

        Map<String, Object> dataMap = new HashMap<>();
        IntStream.range(0, dataList.size()).forEach(idx -> dataMap.put(mapKeyList.get(idx), dataList.get(idx)));
        resultMap.put(MapKeyStringEnum.DATA.getKeyString(), dataMap);

        return resultMap;
    }

    /**
     * ResponseEntity 응답데이터 Wrapper (POST, PUT, DELETE시 사용)
     * @param resultStatusVO {@link ResultStatusVO} 응답상태 객체
     * @return {@link Map}
     */
    public Map<String, Object> responseEntityBodyWrapper(ResultStatusVO resultStatusVO) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(MapKeyStringEnum.RESULT_STATUS.getKeyString(), resultStatusVO);

        return this.responseEntityBodyWrapper(resultStatusVO, null, null);
    }
}
