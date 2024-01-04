package com.samsung.framework.service.file;

import com.samsung.framework.common.utils.FileUtil;
import com.samsung.framework.common.utils.StringUtil;
import com.samsung.framework.domain.file.File;
import com.samsung.framework.service.common.ParentService;
import com.samsung.framework.vo.file.FilePublicVO;
import com.samsung.framework.vo.file.FileVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Primary
@Service
@Slf4j
public class FilePublicServiceImpl extends ParentService implements FileService {

    @Override
    public List<FilePublicVO> uploadFile(List<MultipartFile> files) throws Exception {
        return getFileUtil().uploadFiles(files,getPropertiesUtil().getFile().getRootDir() + getPropertiesUtil().getFile().getRealDir());
    }

    public List<FilePublicVO> uploadFile(List<MultipartFile> files, String type) throws Exception {
        return getFileUtil().uploadFiles(files, getPropertiesUtil().getFile().getRootDir() + getPropertiesUtil().getFile().getRealDir() + "/" +type);
    }

    public List<FilePublicVO> saveFile(List<FilePublicVO> files) throws Exception {
        List<FilePublicVO> targetFiles = new ArrayList<>();
        for(FilePublicVO file : files){
            FilePublicVO target = FilePublicVO.builder()
                                .originalName(file.getOriginalName())
                                .name(file.getName())
                                .fileNo(1)
                                .extension(getFileUtil().checkFileType(file.getOriginalName()))
                                .storagePath(file.getStoragePath())
                                .size(file.getSize())
                                .createdBy("hsk9839")
                                .build();
            getCommonMapper().getFileMapper().save(target);
            targetFiles.add(target);
        }
        return targetFiles;
    }

    public List<FilePublicVO> saveFile(List<FilePublicVO> files,String regId) throws Exception {
        int fileCount=1;
        List<FilePublicVO> targetFiles = new ArrayList<>();

        for(FilePublicVO file : files){
            FilePublicVO target;
            if(StringUtil.isEmpty(file.getStoragePath())){ //빈 값인 경우
                String createFileNm = getFileUtil().createFileName(file.getOriginalName());
                target = FilePublicVO.builder()
                        .originalName(file.getOriginalName())
                        .name(file.getName())
                        .fileNo(fileCount)
                        .extension(getFileUtil().checkFileType(file.getOriginalName()))
                        .storagePath(getPropertiesUtil().getFile().getRootDir() + getPropertiesUtil().getFile().getRealDir() +createFileNm)
                        .size(file.getSize())
                        .createdBy(regId)
                        .build();
            }else{
                target = FilePublicVO.builder()
                            .originalName(file.getOriginalName())
                            .name(file.getName())
                            .fileNo(fileCount)
                            .extension(file.getExtension())
                            .storagePath(file.getStoragePath())
                            .size(file.getSize())
                            .createdBy(regId)
                        .build();
            }
            fileCount++;
            getCommonMapper().getFileMapper().save(target);
            targetFiles.add(target);
        }
        return targetFiles;
    }

    public int saveFile(List<FilePublicVO> files, Long seq) throws Exception {
        int insert = 0;
        for(FilePublicVO file : files){
            insert = getCommonMapper().getFileMapper().insert(files);
            if(insert<0) break;
        }
        return insert;
    }

    @Override
    public int deleteFile(String fileName) {
        return 0;
    }

    public int deleteFile(Long seq, String lastId) {
        FilePublicVO srchFile = FilePublicVO.builder()
                .fileSeq(seq)
                .createdBy(lastId)
                .build();

        // 삭제할 File 조회
        FilePublicVO file = getCommonMapper().getFileMapper().getFile(srchFile);
        int iAffectedRows = 0;
        if(StringUtil.isNotEmpty(file)){
            iAffectedRows = getCommonMapper().getFileMapper().deleteFile(srchFile);
        }
        return iAffectedRows;
    }

    @Override
    public int deleteFiles(String lastId, List<Integer> tgtList){
        int iAffectedRows = 0;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("lastId", lastId);
        paramMap.put("seqList", tgtList);
        iAffectedRows = getCommonMapper().getFileMapper().deleteFileList(paramMap);

        return iAffectedRows;
    }


    /**
     * 파일 수정
     * @param files
     * @param lastId
     * @return
     * @throws Exception
     */
    @Override
    public int updateFiles(List<FilePublicVO> files, List<String> attachList, String lastId) throws Exception {

        List<Long> removeList = new ArrayList<>();
        List<Long> targetFiles = new ArrayList<>();
        files.forEach(file->targetFiles.add(file.getFileSeq()));

        attachList.forEach(file-> {
            Long seq = Long.valueOf(file);
            boolean flag = targetFiles.contains(seq);
            if(!flag) removeList.add(seq);
        });
        int delete =0;
        if(!removeList.isEmpty()){
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("lastId",lastId);
            paramMap.put("seqList", removeList);
            delete = getCommonMapper().getFileMapper().deleteFileList(paramMap);
        }

        return delete > 0 ? 1 : 0;
    }

    /**
     * file이름 기준으로 문자열 가져오기
     * @param fileNm
     * @return
     */
    @Override
    public FilePublicVO getFile(String fileNm) {
        FilePublicVO target = FilePublicVO.builder()
                                            .name(fileNm)
                                            .build();

        return getCommonMapper().getFileMapper().getFile(target);
    }

    /**
     * fileSeq 기준으로 directory가져오기
     * @param fileSeq
     * @return FilePublicVO
     */
    public FilePublicVO getFile(Long fileSeq){
        FilePublicVO target = FilePublicVO.builder()
                                            .fileSeq(fileSeq)
                                            .build();
        return getCommonMapper().getFileMapper().getFile(target);
    }
    
    @Override
    public List<FilePublicVO> getFiles(Long entitySeq, String tableName) {
        return null;
    }

    public List<FilePublicVO> getFiles(List<String> attachIdList) {
        FilePublicVO file = FilePublicVO.builder()
                                        .attachList(attachIdList)
                                        .build();

        return getCommonMapper().getFileMapper().getFiles(file);
    }
    @Override
    public List<FilePublicVO> saveFile(List<FilePublicVO> files, String tableName, Long seq) throws Exception {
        return null;
    }

    /**
     * 파일 한개 삽입
     * @param file
     * @param request
     * @param response
     * @throws IOException
     */
    public void downloadFile(FilePublicVO file, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String realPath = FileUtil.getOsRootDir() + file.getStoragePath();
        String fileNmOrg = file.getOriginalName();
        getFileUtil().downloadFile(fileNmOrg ,realPath, request, response);
    }

    /**
     * 파일 여러개 다운로드
     * @param fileList
     * @param reqeust
     * @param response
     * @throws IOException
     */
    @Override
    public void downloadFiles(List<FilePublicVO> fileList, HttpServletRequest reqeust, HttpServletResponse response) throws IOException {
        for(FilePublicVO file: fileList){
           downloadFile(file, reqeust, response);
        }
    }

}
