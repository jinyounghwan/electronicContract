package com.samsung.framework.service.file;

import com.samsung.framework.domain.file.File;
import com.samsung.framework.vo.file.FilePublicVO;
import com.samsung.framework.vo.file.FileVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {

    // 업로드 파일
    List<FilePublicVO> uploadFile(List<MultipartFile> files) throws Exception;
    
    // 파일 저장
    int saveFile(List<File> files, String tableName, Long seq) throws Exception;

    // 파일 삭제
    int deleteFile(String fileName);
    
    // 파일들 삭제
    int deleteFiles(List<File> files);

    // 파일 업데이트
    int updateFile(List<File> newFiles, String tableName ,Long entitySeq) throws Exception;
    
    // 단 건 파일 조회
    FilePublicVO getFile(String fileNm);

    // 파일 조회
    List<FileVO> getFiles(Long entitySeq, String tableName);

    // 파일 다운로드
    void downloadFiles(List<FilePublicVO> fileList, HttpServletRequest reqeust, HttpServletResponse response) throws IOException;
}
