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

    public List<FilePublicVO> uploadFile(List<MultipartFile> files) throws Exception {
        return getFileUtil().uploadFiles(files,getPropertiesUtil().getFile().getRootDir() + getPropertiesUtil().getFile().getRealDir());
    }

    public int saveFile(List<FilePublicVO> files, Long seq, String regId) throws Exception {
        int insert=0;
        Long fileCount=1L;

        for(FilePublicVO file : files){
            FilePublicVO target;
            if(StringUtil.isEmpty(file.getFilePath())){ //빈 값인 경우
                String createFileNm = getFileUtil().createFileName(file.getFileNmOrg());
                target = FilePublicVO.builder()
                        .filePath(getPropertiesUtil().getFile().getRootDir() + getPropertiesUtil().getFile().getRealDir() +createFileNm)
                        .fileNo(fileCount)
                        .fileSize(file.getFileSize())
                        .fileNm(createFileNm)
                        .fileNmOrg(file.getFileNmOrg())
                        .targetId(String.valueOf(seq))
                        .delYn("N")
                        .regId(regId)
                        .build();
            }else{
                target = FilePublicVO.builder()
                        .filePath(file.getFilePath())
                        .fileNo(fileCount)
                        .fileSize(file.getFileSize())
                        .fileNm(file.getFileNm())
                        .fileNmOrg(file.getFileNmOrg())
                        .targetId(String.valueOf(seq))
                        .delYn("N")
                        .regId(regId)
                        .build();
            }
            fileCount++;
            insert = getCommonMapper().getFileMapper().save(target);
           if(insert<0) break;
        }
        return insert;
    }

    public int saveFile(List<FilePublicVO> files, Long seq) throws Exception {
        for(FilePublicVO file : files){
            getCommonMapper().getFileMapper().insert(files);
        }
        return 0;
    }
    @Override
    public int deleteFile(String fileName) {
        return 0;
    }

    public int deleteFile(String fileName, String lastId) {
        FilePublicVO srchFile = FilePublicVO.builder()
                .fileNm(fileName)
                .lastId(lastId)
                .build();

        // 삭제할 File 조회
        FilePublicVO file = getCommonMapper().getFileMapper().getFile(srchFile);
        int iAffectedRows = 0;
        if(StringUtil.isNotEmpty(file)){
            iAffectedRows = getCommonMapper().getFileMapper().deleteFile(srchFile);
        }
        return iAffectedRows;
    }


    public int deleteFiles(String lastId, List<Integer> tgtList){
        int iAffectedRows = 0;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("lastId", lastId);
        paramMap.put("seqList", tgtList);
        iAffectedRows = getCommonMapper().getFileMapper().deleteFileList(paramMap);

        return iAffectedRows;
    }

    @Override
    public int deleteFiles(List<File> files) {
        int iAffectedRows = 0;

        for(File file : files){
            iAffectedRows = deleteFile(file.getFileNm());
        }

        return iAffectedRows;
    }

    @Override
    public int updateFile(List<FilePublicVO> newFiles, String tableName, Long entitySeq) throws Exception {
        return 0;
    }

    /**
     * 파일 수정
     * @param files
     * @param seq
     * @param lastId
     * @return
     * @throws Exception
     */
    public int updateFile(List<FilePublicVO> files, Long seq, String lastId) throws Exception {
        List<FilePublicVO> originFiles = getFiles(seq);

        //삭제할 리스트
        List<FilePublicVO> originList = new ArrayList<>();

        // 원본 FileVO를 originList 안에 넣어 준다.
        for(FilePublicVO file: originFiles){
            originList.add(file);
        }

        List<FilePublicVO> newFileList = new ArrayList<>();
        //원본 FilePublicVO를 origin 리스트 안에 넣어 준다.
        for(FilePublicVO file : files){
            newFileList.add(file);
            for(int index = originList.size()-1; index >= 0; index--) {
                String originFileName = originList.get(index).getFileNmOrg();
                if (originFileName.equals(file.getFileNmOrg())) {
                    newFileList.remove(file); // 새로운 리스트에 원본 리스트와 같다면 삭제
                    originList.remove(index);
                }
            }
        }

        int delete = 0;
        // 수정 시 기존 파일이 없으면 삭제
        if(StringUtil.isNotEmpty(originList)){
            for(FilePublicVO file : originList){
                delete = deleteFile(file.getFileNm(), lastId);
                if(delete<=0) return delete;
            }
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
                                            .fileNm(fileNm)
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
    public List<FileVO> getFiles(Long entitySeq, String tableName) {
        return null;
    }

    public List<FilePublicVO> getFiles(Long seq) {
        FilePublicVO file = FilePublicVO.builder()
                .targetId(String.valueOf(seq))
                .build();

        return getCommonMapper().getFileMapper().getFiles(file);
    }
    @Override
    public int saveFile(List<FilePublicVO> files, String tableName, Long seq) throws Exception {
        return 0;
    }

    /**
     * 파일 한개 삽입
     * @param file
     * @param request
     * @param response
     * @throws IOException
     */
    public void downloadFile(FilePublicVO file, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String realPath = FileUtil.getOsRootDir() + file.getFilePath();
        String fileNmOrg = file.getFileNmOrg();
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
