package com.samsung.framework.controller.file;

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

import java.io.IOException;
import java.net.URI;

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

}
