/*
package com.samsung.framework.service.file;


import com.enums.common.framework.samsung.FileEntityType;
import com.exception.common.framework.samsung.CustomFileException;
import com.utils.common.framework.samsung.StringUtil;
import com.file.domain.framework.samsung.File;
import com.common.service.framework.samsung.ParentService;
import com.file.vo.framework.samsung.FileVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class FileServiceImpl extends ParentService implements FileService{

    @Override
    public List<FileVO> uploadFile(List<MultipartFile> files) throws Exception {
        return null;
    }


    public int saveFile(List<File> files) throws Exception {
        return 0;
    }

    */
/**
     * 업로드 파일
     * @param files
     * @return
     * @throws Exception
     *//*

    public List<FileVO> uploadFile(List<MultipartFile> files, String entityName, String entityType) throws Exception {
        List<FileVO> fileList= getFileUtil().uploadFiles(files, getPropertiesUtil().getFile().getRootDir() + getPropertiesUtil().getFile().getTempDir());
        saveTempFile(fileList, entityName, entityType);
        return fileList;
    }

    public int saveTempFile(List<FileVO> files, String entityName, String entityType) throws CustomFileException {
        int iAffectedRows = 0;

        FileEntityType fileEntityType = getFileUtil().getFileEntityType(entityName).get();
        // 테이블 별 "01", "02" , "03" 형식 확인
        boolean correctType = false;
        for(String type : fileEntityType.getEntityType()){
            if(StringUtil.equals(type, entityType)) {
                correctType = true;
                break;
            }
        }
        if(!correctType) {
            throw new CustomFileException();
        }


        for(FileVO file: files){
            File target = File.builder()
                            .fileName(file.getFileName())
                            .entityName(entityName)
                            .entityType(entityType)
                            .originFileName(file.getOriginFileName())
                            .fileSize(file.getFileSize())
                            .build();
            iAffectedRows = getCommonMapper().getFileMapper().saveTempFile(target);

            if(iAffectedRows<=0) break;
        }
        return iAffectedRows;
    }



    */
/**
     * 파일 저장 (파일 업데이트 신규 파일 인 경우 사용)
     * @param files
     * @param tableName
     * @param seq
     * @return
     *//*

    public int saveFile(List<File> files, String tableName,Long seq) throws Exception {
        int iAffectedRows=0;
        for (File file : files) {

            // temp db 조회
            FileVO tempFile = getTempFile(file.getFileName());

            FileEntityType fileEntityType = getFileUtil().getFileEntityType(tableName).get();
            String realPath = getPropertiesUtil().getFile().getRootDir() + getPropertiesUtil().getFile().getRealDir() + fileEntityType.getEntityDir();
            log.info(realPath);

            File target = File.builder()
                    .entityName(tableName)
                    .entitySeq(seq)
                    .entityType(tempFile.getEntityType())
                    .filePath(realPath)
                    .fileName(tempFile.getFileName())
                    .originFileName(tempFile.getOriginFileName())
                    .fileType(getFileUtil().checkFileType(file.getFileName()))
                    .fileSize(tempFile.getFileSize())
                    .build();

            iAffectedRows = getCommonMapper().getFileMapper().save(target);

            if (iAffectedRows <= 0) break;
            getFileUtil().moveFile(file.getFileName(), getPropertiesUtil().getFile().getRootDir() + getPropertiesUtil().getFile().getTempDir() , realPath);
        }

        return iAffectedRows;
    }
    */
/**
     * 파일 삭제
     * @param fileName
     * @return
     *//*

    public int deleteFile(String fileName){
        File srchFile = File.builder()
                        .fileName(fileName)
                        .build();

        // 삭제할 File 조회
        FileVO file = getCommonMapper().getFileMapper().getFile(srchFile);
        int iAffectedRows = getCommonMapper().getFileMapper().deleteFile(file);

        // 실제 Directory 삭제
        if(StringUtil.isNotEmpty(file)) {
            getFileUtil().deleteFile( file.getFilePath()+"/"+ file.getFileName());
        }
        return iAffectedRows;
    }

    */
/**
     * 파일 리스트 삭제
     * @param files
     * @return
     *//*

    public int deleteFiles(List<File> files){
        int iAffectedRows = 0;

        for(File file : files){
           iAffectedRows = deleteFile(file.getFileName());
        }

        return iAffectedRows;
    }

    */
/**
     * 파일 업데이트
     * @param newFiles
     * @param tableName
     * @param entitySeq
     * @return
     * @throws Exception
     *//*

    public int updateFile(List<File> newFiles, String tableName ,Long entitySeq) throws Exception {
        List<FileVO> originFiles = getFiles(entitySeq, tableName);

        // 제거할 파일 리스트
        List<FileVO> originList = new ArrayList<>();


        // 원본 FileVO를 originList 안에 넣어 준다.
        for(FileVO file: originFiles){
            originList.add(file);
        }

        List<File> newFileList = new ArrayList<>();
        for(File file : newFiles){
            // 새로운 파일리스트에 있는 경우 originList에서 제거
            newFileList.add(file);
            for(int index = originList.size()-1; index>=0; index--){
                String originFileName = originList.get(index).getFileName();
                if(originFileName.equals(file.getFileName())) {
                    newFileList.remove(file);
                    originList.remove(index);
                }
            }
        }

        int delete = 0;
        // 수정 시 기존 파일이 없으면 삭제
        if(StringUtil.isNotEmpty(originList)){
            for(FileVO file : originList){
                delete = deleteFile(file.getFileName());
                if(delete<=0) return delete;
            }
        }

        int insert = 0;
        if(StringUtil.isNotEmpty(newFileList))  insert = saveFile(newFileList, tableName, entitySeq);

        return delete > 0 && insert > 0 ? 1 : 0;
    }

    */
/**
     * 단 건 File 조회
     * @param file
     * @return FileVO 객체
     *//*

    public FileVO getFile(File file) { return getCommonMapper().getFileMapper().getFile(file); }

    */
/**
     * 단 건 TempFile 조회
     *//*

    public FileVO getTempFile(String fileName) {return getCommonMapper().getFileMapper().getTempFile(fileName);}

    */
/**
     * 해당 seq에 물린 File 목록 조회
     * @param entitySeq
     * @return FileVO List
     *//*

    public List<FileVO> getFiles(Long entitySeq, String tableName){
        File file = File.builder()
                        .entitySeq(entitySeq)
                        .entityName(tableName)
                        .build();

        return getCommonMapper().getFileMapper().getFiles(file);
    }

    */
/**
     * 파일 목록 다운로드
     * @param fileList
     * @param response
     * @param reqeust
     * @throws IOException
     *//*

    public void downloadFiles(List<File> fileList, HttpServletRequest reqeust, HttpServletResponse response) throws IOException {
        for(File file : fileList){
           FileVO targetFile = getCommonMapper().getFileMapper().getFile(file);
           String realPath = getFileUtil().getOsRootDir() + targetFile.getFilePath()+ "/" + targetFile.getFileName();
           String originFileName = targetFile.getOriginFileName();
           getFileUtil().downloadFile(originFileName, realPath, reqeust, response);
        }
    }
}
*/
