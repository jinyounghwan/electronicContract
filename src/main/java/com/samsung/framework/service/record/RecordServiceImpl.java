package com.samsung.framework.service.record;

import com.samsung.framework.common.enums.HttpRequestTypeEnum;
import com.samsung.framework.common.utils.DateUtil;
import com.samsung.framework.common.utils.StringUtil;
import com.samsung.framework.mapper.menu.MenuMapper;
import com.samsung.framework.mapper.record.RecordMapper;
import com.samsung.framework.vo.common.SelectOptionVO;
import com.samsung.framework.vo.menu.MenuVO;
import com.samsung.framework.vo.record.RecordVO;
import com.samsung.framework.vo.search.record.RecordSearchVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
@Slf4j
@RequiredArgsConstructor
@Service
public class RecordServiceImpl implements RecordService {

    private final RecordMapper recordMapper;
    private final MenuMapper menuMapper;

    @Override
    public List<SelectOptionVO> getSearchDateRangeOptionList() {
        var list = new ArrayList<SelectOptionVO>();
        list.add(new SelectOptionVO(0, "ALL", "전체"));
        list.add(new SelectOptionVO(0, "REG_DT", "접속 일시"));

        return list;
    }

    @Override
    public List<SelectOptionVO> getSearchKeywordTypeList() {
        var list = new ArrayList<SelectOptionVO>();
        list.add(new SelectOptionVO(0, "ALL", "전체"));
        list.add(new SelectOptionVO(0, "USER", "회원 ID"));
        list.add(new SelectOptionVO(0, "MENU", "접속 메뉴"));

        return list;
    }

    @Override
    public int totalCount(RecordSearchVO recordSearchVO) {
        if(recordSearchVO == null) {

        }

        return recordMapper.findAllTotalCount(recordSearchVO);
    }

    @Override
    public List<RecordVO> findAll(RecordSearchVO recordSearchVO) {
        int totalCount = this.totalCount(recordSearchVO);
        if(totalCount < 1) {
            return new ArrayList<>();
        }

        List<RecordVO> list = recordMapper.findAll(recordSearchVO);
        List<MenuVO> menuList = menuMapper.findCommonMenuCodeList();
        list.forEach(ele -> {
                ele.setRegDtStr(DateUtil.convertLocalDateTimeToString(ele.getRegDt(), DateUtil.DATETIME_YMDHM_PATTERN));
                ele.setLogTypeKoreanStr(setProcessTypeStringInKorean(ele.getLogType()));
                ele.setLogEtc(StringUtil.NVL(ele.getLogEtc()));
                if (ele.getCodeCd() != null) {
                    Optional<MenuVO> parentMenu = menuList.stream().filter(ml -> ml.getMenuCode().substring(0, 6).equals(ele.getCodeCd().substring(0, 6))).findFirst();
                    parentMenu.ifPresent(pm -> ele.setMenuNm(pm.getName() + " > " + ele.getCodeNm()));
                }else {
                    ele.setMenuNm("");
                }
        });

        return list;
    }

    @Override
    public RecordVO findById(long logSeq) {
        RecordVO rowData = recordMapper.findById(logSeq);
        if(rowData != null) {
            rowData.setLogTypeKoreanStr(setProcessTypeStringInKorean(rowData.getLogType()));
            rowData.setLogEtc(StringUtil.NVL(rowData.getLogEtc()));
            if(rowData.getCodeCd() != null) {
                List<MenuVO> menuList = menuMapper.findCommonMenuCodeList();
                Optional<MenuVO> parentMenu = menuList.stream().filter(ml -> ml.getMenuCode().substring(0, 6).equals(rowData.getCodeCd().substring(0, 6))).findFirst();
                parentMenu.ifPresent(pm -> rowData.setMenuNm(pm.getName() + " > " + rowData.getCodeNm()));
            }else {
                rowData.setMenuNm("");
            }
        }

        return rowData;
    }

    /**
     * 수행 업무 유형 한글 필터
     * @param tgt
     * @return
     */
    private String setProcessTypeStringInKorean(String tgt) {
        return Arrays.stream(HttpRequestTypeEnum.values()).filter(m -> m.toString().equals(tgt)).findAny().orElse(HttpRequestTypeEnum.EMPTY).getDesc();
    }
}
