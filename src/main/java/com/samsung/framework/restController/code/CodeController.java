/*
package com.samsung.framework.restController.code;

import com.enums.common.framework.samsung.MapKeyStringEnum;
import com.enums.common.framework.samsung.RequestTypeEnum;
import com.enums.common.framework.samsung.ResultCodeMsgEnum;
import com.utils.common.framework.samsung.ObjectHandlingUtil;
import com.samsung.framework.restController.common.ParentController;
import com.code.domain.framework.samsung.Code;
import com.common.domain.framework.samsung.DataObject;
import com.common.domain.framework.samsung.SearchObject;
import com.code.vo.framework.samsung.CodeVO;
import com.common.vo.framework.samsung.ResultStatusVO;
import com.member.vo.framework.samsung.MemberVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/code")
public class CodeController extends ParentController {


    */
/**
     * 코드 페이징 조회
     * @param searchObject
     * @return
     *//*

    @PostMapping("/paging")
    public ResponseEntity pagingCode(@RequestBody @Valid SearchObject searchObject) {

        List<CodeVO> codeList = getCommonService().getCodeService().pagingCode(searchObject.getSearch().getCodeSearch());

        ResultStatusVO resultStatusVO = ObjectHandlingUtil.setListResultStatusVO(codeList, ResultCodeMsgEnum.NO_DATA);
        List<String> mapKeyList = Arrays.asList(MapKeyStringEnum.PAGING.getKeyString(), MapKeyStringEnum.CODE_LIST.getKeyString());
        Map<String, Object> resultMap = getMapUtil().responseEntityBodyWrapper(resultStatusVO, mapKeyList, searchObject.getSearch().getCodeSearch().getPaging(), codeList);

        return ResponseEntity.ok(resultMap);
    }


    */
/**
     * 코드 목록 조회
     * @param searchObject
     * @return
     *//*

    @PostMapping("/list")
    public ResponseEntity listCode(@RequestBody @Valid SearchObject searchObject) {
        List<CodeVO> codeList = getCommonService().getCodeService().listCode(searchObject.getSearch().getCodeSearch());

        ResultStatusVO resultStatusVO = ObjectHandlingUtil.setListResultStatusVO(codeList, ResultCodeMsgEnum.NO_DATA);
        List<String> mapKeyList = Arrays.asList(MapKeyStringEnum.CODE_LIST.getKeyString());
        Map<String, Object> resultMap = getMapUtil().responseEntityBodyWrapper(resultStatusVO, mapKeyList, codeList);

        return ResponseEntity.ok(resultMap);
    }


    */
/**
     * 코드 한 건 조회
     * @param searchObject
     * @return
     *//*

    @PostMapping("/row")
    public ResponseEntity rowCode(@RequestBody @Valid SearchObject searchObject) {
        CodeVO code = getCommonService().getCodeService().rowCode(searchObject.getSearch().getCodeSearch());

        ResultStatusVO resultStatusVO = ObjectHandlingUtil.setSingleObjResultStatusVO(code, ResultCodeMsgEnum.NO_DATA);
        List<String> mapKeyList = Arrays.asList(MapKeyStringEnum.CODE.getKeyString());
        Map<String, Object> resultMap = getMapUtil().responseEntityBodyWrapper(resultStatusVO, mapKeyList, code);

        return ResponseEntity.ok(resultMap);
    }


    */
/**
     * 코드 저장
     * @param dataObject
     * @return
     *//*

    @PostMapping("/insert")
    public ResponseEntity insertCode(@RequestBody @Valid DataObject dataObject, HttpServletRequest request) throws Exception {
        MemberVO memberVO = ObjectHandlingUtil.extractLoginInfo(request);
        Code inserted =  getCommonService().getCodeService().insertCode(dataObject.getData().getCode(), memberVO.getMemberId());
        ResultStatusVO resultStatusVO = ObjectHandlingUtil.setSingleObjResultStatusVO(inserted, ResultCodeMsgEnum.EXIST_CODE);
        Map<String, Object> resultMap = getMapUtil().responseEntityBodyWrapper(resultStatusVO);

        return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
    }

    */
/**
     * 코드 수정
     * @param dataObject
     * @return
     *//*

    @PostMapping("/update")
    public ResponseEntity updateCode(@RequestBody @Valid DataObject dataObject, HttpServletRequest request) throws Exception {
        MemberVO memberVO = ObjectHandlingUtil.extractLoginInfo(request);
        int inserted =  getCommonService().getCodeService().updateCode(dataObject.getData().getCode(), memberVO.getMemberId());
        ResultStatusVO resultStatusVO = ObjectHandlingUtil.setDataManipulationResultStatusVO(inserted, RequestTypeEnum.UPDATE);
        Map<String, Object> resultMap = getMapUtil().responseEntityBodyWrapper(resultStatusVO);

        return ResponseEntity.ok(resultMap);
    }

    */
/**
     * 코드 순서 일괄변경
     * @param dataObject
     * @return
     *//*

    @PostMapping("/updateCodeOrder")
    public ResponseEntity updateCodeOrder(@RequestBody @Valid DataObject dataObject) throws Exception {

        Integer iAffectedRows =  getCommonService().getCodeService().updateCodeOrder(dataObject.getData().getCodeList());

        ResultStatusVO resultStatusVO = ObjectHandlingUtil.setDataManipulationResultStatusVO(iAffectedRows, RequestTypeEnum.UPDATE);
        Map<String, Object> resultMap = getMapUtil().responseEntityBodyWrapper(resultStatusVO);

        return ResponseEntity.ok(resultMap);
    }






}
*/
