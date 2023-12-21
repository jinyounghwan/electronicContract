/*
package com.samsung.framework.restController.File;

import com.enums.common.framework.samsung.MapKeyStringEnum;
import com.enums.common.framework.samsung.RequestTypeEnum;
import com.enums.common.framework.samsung.ResultCodeMsgEnum;
import com.utils.common.framework.samsung.ObjectHandlingUtil;
import com.samsung.framework.restController.common.ParentController;
import com.common.domain.framework.samsung.DataObject;
import com.common.vo.framework.samsung.ResultStatusVO;
import com.file.vo.framework.samsung.FileVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/file")
public class RestFileController extends ParentController {
    */
/**
     * 파일 업로드
     * @param files
     * @return 
     * @throws Exception
     *//*

    @PostMapping("/upload")
    public ResponseEntity uploadFile(List<MultipartFile> files,@RequestParam(required = true) String entityId,@RequestParam(required = true) String entityType) throws Exception {
        List<FileVO> fileList = getCommonService().getFileService().uploadFile(files, entityId, entityType);
        ResultStatusVO resultStatusVO = ObjectHandlingUtil.setListResultStatusVO(fileList, ResultCodeMsgEnum.NO_DATA);
        var mapKeyString = Arrays.asList( MapKeyStringEnum.FILE_LIST.getKeyString());

        var resultMap = getMapUtil().responseEntityBodyWrapper(resultStatusVO, mapKeyString, fileList);

        return ResponseEntity.ok(resultMap);
    }


    */
/**
     * 파일 삭제 단 건
     * @param dataObject
     * @return
     *//*

    @PostMapping("/delete")
    public ResponseEntity deleteFile(@RequestBody DataObject dataObject) {
        int deleted = getCommonService().getFileService().deleteFile(dataObject.getData().getFile().getFileName());
        ResultStatusVO resultStatusVO = ObjectHandlingUtil.setDataManipulationResultStatusVO(deleted, RequestTypeEnum.DELETE);

        Map<String, Object> resultMap = getMapUtil().responseEntityBodyWrapper(resultStatusVO);

        return ResponseEntity.ok(resultMap);
    }

    */
/**
     * 파일 리스트 삭제
     * @param dataObject
     * @return
     *//*

    @PostMapping("/deleteList")
    public ResponseEntity deleteFiles(@RequestBody DataObject dataObject){
        int deleted = getCommonService().getFileService().deleteFiles(dataObject.getData().getFileList());
        ResultStatusVO resultStatusVO = ObjectHandlingUtil.setDataManipulationResultStatusVO(deleted, RequestTypeEnum.DELETE);

        Map<String, Object> resultMap = getMapUtil().responseEntityBodyWrapper(resultStatusVO);

        return ResponseEntity.ok(resultMap);
    }

    */
/**
     * 파일 다운로드
     * @param dataObject
     *//*

    @PostMapping(value = "/download")
    public void download(@RequestBody DataObject dataObject, HttpServletRequest request, HttpServletResponse response) throws IOException {
        getCommonService().getFileService().downloadFiles(dataObject.getData().getFileList(),  request, response);
    }

}
*/
