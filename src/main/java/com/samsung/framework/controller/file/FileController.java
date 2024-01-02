package com.samsung.framework.controller.file;

import com.samsung.framework.common.exception.CustomFileException;
import com.samsung.framework.controller.common.ParentController;
import com.samsung.framework.vo.file.FilePublicVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/file")
public class FileController extends ParentController {
    @GetMapping("/download/{fileSeq}")
    public ResponseEntity download(@PathVariable String fileSeq, HttpServletRequest request, HttpServletResponse response) throws IOException {
      FilePublicVO file = getCommonService().getFileServiceImpl().getFile(Long.parseLong(fileSeq));
      getCommonService().getFileServiceImpl().downloadFile(file, request, response);
      HttpHeaders headers = new HttpHeaders();
      headers.setLocation(URI.create("/board/detail/"+fileSeq));
      return new ResponseEntity(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @PostMapping("/upload")
    public ResponseEntity upload(@RequestBody List<MultipartFile> multipartFileList) throws Exception {
        Map<String,Object> result = new HashMap<>();
        List<FilePublicVO> fileList  = getCommonService().getFileServiceImpl().uploadFile(multipartFileList,"PDF");
        int insert = getCommonService().getFileServiceImpl().saveFile(fileList);
        if(insert < 0) {
            result.put("code","204");
            result.put("message","파일 저장에 실패하였습니다.");
            result.put("errMsg", "incomplete");
        } else {
            result.put("code","200");
            result.put("message", "파일 저장에 성공하였습니다.");
        }

        return ResponseEntity.ok(result);
    }
}
