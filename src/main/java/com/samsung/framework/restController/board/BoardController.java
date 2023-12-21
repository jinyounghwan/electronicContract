/*
package com.samsung.framework.restController.board;

import com.enums.common.framework.samsung.MapKeyStringEnum;
import com.enums.common.framework.samsung.RequestTypeEnum;
import com.enums.common.framework.samsung.ResultCodeMsgEnum;
import com.utils.common.framework.samsung.ObjectHandlingUtil;
import com.samsung.framework.restController.common.ParentController;
import com.common.domain.framework.samsung.DataObject;
import com.common.domain.framework.samsung.SearchObject;
import com.board.vo.framework.samsung.BoardVO;
import com.common.vo.framework.samsung.ResultStatusVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
public class BoardController extends ParentController {

    */
/**
     * 게시판 페이징 조회
     * @param searchObject
     * @return
     *//*

    @PostMapping(value="/paging")
    public ResponseEntity pagingBoard(@RequestBody @Valid SearchObject searchObject){
        List<BoardVO> boardList = getCommonService().getBoardService().pagingBoard(searchObject.getSearch().getBoardSearch());

        ResultStatusVO resultStatusVO = ObjectHandlingUtil.setListResultStatusVO(boardList, ResultCodeMsgEnum.NO_DATA);
        List<String> mapKeyList = Arrays.asList(MapKeyStringEnum.PAGING.getKeyString(), MapKeyStringEnum.BOARD_LIST.getKeyString());
        Map<String,Object> resultMap = getMapUtil().responseEntityBodyWrapper(resultStatusVO, mapKeyList,  searchObject.getSearch().getBoardSearch().getPaging() ,boardList);

        return ResponseEntity.ok(resultMap);
    }

    */
/**
     * 게시판 단 건 조회
     * @param searchObject
     * @return
     *//*

    @PostMapping(value="/row")
    public ResponseEntity rowBoard(@RequestBody @Valid SearchObject searchObject){
        BoardVO board = getCommonService().getBoardService().rowBoard(searchObject.getSearch().getBoardSearch());

        ResultStatusVO resultStatusVO = ObjectHandlingUtil.setSingleObjResultStatusVO(board, ResultCodeMsgEnum.NO_DATA);
        List<String> mapKeyList = Arrays.asList(MapKeyStringEnum.BOARD.getKeyString());
        Map<String, Object> resultMap = getMapUtil().responseEntityBodyWrapper(resultStatusVO, mapKeyList, board);

        return ResponseEntity.ok(resultMap);
    }

    */
/**
     * 게시판 저장
     * @param dataObject
     * @return
     *//*

    @PostMapping(value="/insert")
    public ResponseEntity insertBoard(@RequestBody @Valid DataObject dataObject) throws Exception {
        int inserted = getCommonService().getBoardService().insertBoard(dataObject.getData().getBoard(), dataObject.getData().getFileList(), getLoginVO());

        ResultStatusVO resultStatusVO = ObjectHandlingUtil.setDataManipulationResultStatusVO(inserted, RequestTypeEnum.CREATE);
        Map<String, Object> resultMap = getMapUtil().responseEntityBodyWrapper(resultStatusVO);

        return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
    }

    */
/**
     * 게시판 수정
     * @param dataObject
     * @return
     *//*

    @PostMapping(value="/update")
    public ResponseEntity updateBoard(@RequestBody @Valid DataObject dataObject) throws Exception {
        int inserted = getCommonService().getBoardService().updateBoard(dataObject.getData().getBoard(), dataObject.getData().getFileList(), getLoginVO());

        ResultStatusVO resultStatusVO = ObjectHandlingUtil.setDataManipulationResultStatusVO(inserted, RequestTypeEnum.UPDATE);
        Map<String, Object> resultMap = getMapUtil().responseEntityBodyWrapper(resultStatusVO);

        return ResponseEntity.ok(resultMap);
    }

    */
/**
     * 게시판 삭제
     * @param dataObject
     * @return
     *//*

    @PostMapping(value="/delete")
    public ResponseEntity deleteBoard(@RequestBody @Valid DataObject dataObject){
        int deleted = getCommonService().getBoardService().deleteBoard(dataObject.getData().getBoard(), getLoginVO());

        ResultStatusVO resultStatusVO = ObjectHandlingUtil.setDataManipulationResultStatusVO(deleted,RequestTypeEnum.DELETE);
        Map<String, Object> resultMap = getMapUtil().responseEntityBodyWrapper(resultStatusVO);

        return ResponseEntity.ok(resultMap);
    }

}
*/
