package com.samsung.framework.service.code;

import com.samsung.framework.common.enums.RequestTypeEnum;
import com.samsung.framework.common.enums.TableNameEnum;
import com.samsung.framework.common.utils.ObjectHandlingUtil;
import com.samsung.framework.domain.code.Code;
import com.samsung.framework.domain.common.Paging;
import com.samsung.framework.service.common.ParentService;
import com.samsung.framework.vo.code.CodePublicVO;
import com.samsung.framework.vo.code.CodeVO;
import com.samsung.framework.vo.code.CommonCodeVO;
import com.samsung.framework.vo.common.SelectOptionVO;
import com.samsung.framework.vo.search.code.CodeSearchVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CodeServiceImpl extends ParentService implements CodeService{

    /**
     * 코드 저장
     * @param code
     * @return
     * @throws Exception
     */
    public Code insertCode(Code code, String regId) throws Exception {

        Code target = Code.builder()
                .code(code.getCode())
                .codeName(code.getCodeName())
                .remark1(code.getRemark1())
                .value1(code.getValue1())
                .remark2(code.getRemark2())
                .value2(code.getValue2())
                .codeOrd(code.getCodeOrd())
                .useYn(code.getUseYn())
                .tableName(TableNameEnum.CODE.name())
                .logId1(code.getCode())
                .logType(RequestTypeEnum.UPDATE.getRequestType())
                .logId2(null)
                .logJson(null)
                .remark(null)
                .regId(regId)
                .build();
        getValidationUtil().parameterValidator(target, Code.class);

        // 기존 코드 존재 유무 조회
        CodeSearchVO codeSearchVO = new CodeSearchVO();
        codeSearchVO.setCode(code.getCode());
        CodeVO searched = (CodeVO) getCommonMapper().getCodeMapper().rowBySearch(codeSearchVO);

        if(searched != null) {
            return null;
        }
        int iAffectedRows = getCommonMapper().getCodeMapper().insert(target);

        return target;
    }

    /**
     * 코드 수정
     * @param code
     * @return
     * @throws Exception
     */
    public int updateCode(Code code, String regId) throws Exception {

        Code target = Code.builder()
                .code(code.getCode())
                .codeName(code.getCodeName())
                .remark1(code.getRemark1())
                .value1(code.getValue1())
                .remark2(code.getRemark2())
                .value2(code.getValue2())
                .codeOrd(code.getCodeOrd())
                .useYn(code.getUseYn())
                .tableName(TableNameEnum.CODE.name())
                .logId1(code.getCode())
                .logType(RequestTypeEnum.UPDATE.getRequestType())
                .logId2(null)
                .logJson(null)
                .remark(null)
                .regId(regId)
                .build();
        getValidationUtil().parameterValidator(target, Code.class);
        int iAffectedRows = getCommonMapper().getCodeMapper().update(target);

        return iAffectedRows;
    }

    /**
     * 코드 순서 일괄변경
     * @param codeList
     * @return
     */
    public Integer updateCodeOrder(List<Code> codeList) {
        int iAffectedRows = 0;
        for (Code code : codeList) {
            iAffectedRows += getCommonMapper().getCodeMapper().updateCodeOrder(code);
        }

        //변경된 내용이 없는 경우 null return(오류 발생)
        return iAffectedRows <= 0 ? null : iAffectedRows;
    }

    /**
     * 코드 페이징 조회
     * @param search
     * @return
     */
    public List<CodeVO> pagingCode(CodeSearchVO search) {

        int totalCount = getCommonMapper().getCodeMapper().pagingCountBySearch(search);
        Paging paging = ObjectHandlingUtil.pagingOperatorBySearch(search, totalCount);
        search.setPaging(paging);

        List<CodeVO> codeList = (List<CodeVO>) getCommonMapper().getCodeMapper().pagingBySearch(search);

        return codeList;
    }

    /**
     * 코드 목록 조회
     * @param search
     * @return
     */
    public List<CodePublicVO> listCode() {
       return null;
    }

    /**
     * 코드 단 건 조회
     * @param search
     * @return
     */
    public CodeVO rowCode(CodeSearchVO search) {
        return (CodeVO) getCommonMapper().getCodeMapper().rowBySearch(search);
    }

    @Override
    public List<SelectOptionVO> commonCodeGroupList() {
        return null;
    }

    @Override
    public List<SelectOptionVO> commonCodeCategoryList(String menu) {
        return null;
    }

    @Override
    public List<CommonCodeVO> findAll(CodeSearchVO codeSearchVO) {
        return null;
    }

    @Override
    public Map<String, Object> updateCommonCode(List<CommonCodeVO> tgt) {
        return null;
    }
}
