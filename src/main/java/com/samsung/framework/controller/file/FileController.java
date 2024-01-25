package com.samsung.framework.controller.file;

import com.samsung.framework.service.file.FileService;
import com.samsung.framework.vo.account.AccountVO;
import com.samsung.framework.vo.file.FilePublicVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
public class FileController {

    private final FileService fileService;

    @GetMapping("/download/{fileSeq}")
    public ResponseEntity download(@PathVariable String fileSeq, HttpServletRequest request, HttpServletResponse response) throws IOException {
      FilePublicVO file = fileService.getFile(Long.parseLong(fileSeq));
      fileService.downloadFile(file, request, response);
      HttpHeaders headers = new HttpHeaders();
      headers.setLocation(URI.create("/board/detail/"+fileSeq));
      return new ResponseEntity(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @PostMapping("/upload")
    public ResponseEntity upload(@RequestBody List<MultipartFile> multipartFileList, HttpServletRequest request) throws Exception {
        Map<String,Object> result = new HashMap<>();
        List<FilePublicVO> fileList  = fileService.uploadFile(multipartFileList);
        HttpSession session = request.getSession();
        AccountVO account = (AccountVO) session.getAttribute("loginInfo");
        List<FilePublicVO> saveFileList = fileService.saveFile(fileList, "hsk9839");

        if(saveFileList.isEmpty()) {
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
