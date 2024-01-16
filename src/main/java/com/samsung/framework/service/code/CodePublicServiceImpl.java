package com.samsung.framework.service.code;

import com.samsung.framework.domain.code.CommonCode;
import com.samsung.framework.mapper.code.CodeMapper;
import com.samsung.framework.vo.code.CodePublicVO;
import com.samsung.framework.vo.code.CommonCodeVO;
import com.samsung.framework.vo.common.SelectOptionVO;
import com.samsung.framework.vo.search.code.CodeSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
@Primary
public class CodePublicServiceImpl implements CodeService {

    private final CodeMapper codeMapper;

    // 사이드바 메뉴 조회
    @Override
    public List<CodePublicVO> listCode() {
        return codeMapper.listCode();
    }

    @Override
    public List<SelectOptionVO> commonCodeGroupList() {
        List<SelectOptionVO> list = codeMapper.commonCodeGroupList();
        return list;
    }

    @Override
    public List<SelectOptionVO> commonCodeCategoryList(String code) {
        List<SelectOptionVO> list = codeMapper.commonCodeCategoryList(code);
        return list;
    }

    @Override
    public List<CommonCodeVO> findAll(CodeSearchVO codeSearchVO) {
        List<CommonCodeVO> list = codeMapper.findAll(codeSearchVO);
        return list;
    }

    @Override
    public Map<String, Object> updateCommonCode(List<CommonCodeVO> tgt) {
        var result = new HashMap<String, Object>();
        result.put("code", 200);
        result.put("message", "수정 되었습니다.");

        if(tgt.size() < 1) {
            result.put("code", 204);
            result.put("message", "수정할 대상이 없습니다.");
            return result;
        }

        List<CommonCode> addList = new ArrayList<>();
        List<CommonCode> updateList = new ArrayList<>();
        tgt.forEach(row -> {
            if(row.getCodeCd().equalsIgnoreCase("FIXED") || row.getCodeCd().equalsIgnoreCase("ADD")) {
                if(!row.getCodeNm().isBlank()) {
                    addList.add(CommonCode.builder()
                            .codeCd(row.getCodeCd())
                            .codeNm(row.getCodeNm())
                            .codeDesc(row.getCodeDesc())
                            .codeOrd(row.getCodeOrd())
                            .useYn(row.getUseYn())
                            .delYn(row.getDelYn())
                            .regId("ijzone")
                            .build()
                    );
                }
            }else {
                updateList.add(CommonCode.builder()
                        .codeSeq(row.getCodeSeq())
                        .codeCd(row.getCodeCd())
                        .codeNm(row.getCodeNm())
                        .codeDesc(row.getCodeDesc())
                        .codeOrd(row.getCodeOrd())
                        .useYn(row.getUseYn())
                        .delYn(row.getDelYn())
                        .lastId("ijzone")
                        .build()
                );
            }

        });

        AtomicInteger updateCount = new AtomicInteger(0);
        addList.forEach(row -> {
            updateCount.addAndGet(codeMapper.saveCommonCode(row));
        });
        updateList.forEach(row -> {
            updateCount.addAndGet(codeMapper.updateCommonCode(row));
        });

        if(updateCount.get() < 1) {
            result.put("code", 204);
            result.put("message", "수정할 대상이 없습니다.");
            return result;
        }

        return result;
    }
}
