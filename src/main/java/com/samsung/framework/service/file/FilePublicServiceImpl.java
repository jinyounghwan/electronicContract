package com.samsung.framework.service.file;

import com.samsung.framework.common.utils.FileUtil;
import com.samsung.framework.common.utils.StringUtil;
import com.samsung.framework.mapper.file.FileMapper;
import com.samsung.framework.vo.file.FilePublicVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Primary
@RequiredArgsConstructor
@Service
@Slf4j
public class FilePublicServiceImpl implements FileService {

    private final FileUtil fileUtil;
    private final FileMapper fileMapper;

    @Value("${properties.file.rootDir}")
    private String getRootDir;
    @Value("${properties.file.realDir}")
    private String getRealDir;

    /**
     * 일반 파일 업로드 시 사용
     * @param files
     * @return
     * @throws Exception
     */
    @Override
    public List<FilePublicVO> uploadFile(List<MultipartFile> files) throws Exception {
        return fileUtil.uploadFiles(files,getRootDir + getRealDir);
    }

    /**
     * type으로 들어 온 변수의 값으로 폴더를 생성 후, 실제 directory 업로드
     * @param files
     * @param type
     * @return
     * @throws Exception
     */
    public List<FilePublicVO> uploadFile(List<MultipartFile> files, String type) throws Exception {
        return fileUtil.uploadFiles(files, getRootDir + getRealDir + "/" +type);
    }

    /**
     * DB Table에 파일 관련 정보 저장
     * @param files
     * @return
     * @throws Exception
     */
    @Override
    public List<FilePublicVO> saveFile(List<FilePublicVO> files) throws Exception {
        List<FilePublicVO> targetFiles = new ArrayList<>();
        for(FilePublicVO file : files){
            FilePublicVO target = FilePublicVO.builder()
                                .originalName(file.getOriginalName())
                                .name(file.getName())
                                .fileNo(1)
                                .extension(fileUtil.checkFileType(file.getOriginalName()))
                                .storagePath(FileUtil.seperateOs(file.getStoragePath()))
                                .size(file.getSize())
                                .createdBy("hsk9839")
                                .build();
            fileMapper.save(target);
            targetFiles.add(target);
        }
        return targetFiles;
    }

    /**
     * 파일 등록자를 regId로 넘겨받은 후, 해당 값을 테이블에 저장
     * @param files
     * @param regId
     * @return
     * @throws Exception
     */
    public List<FilePublicVO> saveFile(List<FilePublicVO> files,String regId) throws Exception {
        int fileCount=1;
        List<FilePublicVO> targetFiles = new ArrayList<>();

        for(FilePublicVO file : files){
            FilePublicVO target;
            if(StringUtil.isEmpty(file.getStoragePath())){ //빈 값인 경우
                String createFileNm = fileUtil.createFileName(file.getOriginalName());
                target = FilePublicVO.builder()
                        .originalName(file.getOriginalName())
                        .name(file.getName())
                        .fileNo(fileCount)
                        .extension(fileUtil.checkFileType(file.getOriginalName()))
                        .storagePath(getRootDir + getRealDir +createFileNm)
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
            fileMapper.save(target);
            targetFiles.add(target);
        }
        return targetFiles;
    }


    @Override
    public int deleteFiles(String lastId, List<Integer> tgtList){
        int iAffectedRows = 0;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("lastId", lastId);
        paramMap.put("seqList", tgtList);
        iAffectedRows = fileMapper.deleteFileList(paramMap);

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
            delete = fileMapper.deleteFileList(paramMap);
        }

        return delete > 0 ? 1 : 0;
    }

    /**
     * file이름 기준으로 파일 정보 가져오기
     * @param fileNm
     * @return
     */
    @Override
    public FilePublicVO getFile(String fileNm) {
        FilePublicVO target = FilePublicVO.builder()
                                            .name(fileNm)
                                            .build();

        return fileMapper.getFile(target);
    }

    /**
     * fileSeq 기준으로 파일 정보 가져오기
     * @param fileSeq
     * @return FilePublicVO
     */
    public FilePublicVO getFile(Long fileSeq){
        FilePublicVO target = FilePublicVO.builder()
                                            .fileSeq(fileSeq)
                                            .build();
        return fileMapper.getFile(target);
    }

    @Override
    public List<FilePublicVO> getFiles(List<String> attachIdList) {
        FilePublicVO file = FilePublicVO.builder()
                                        .attachList(attachIdList)
                                        .build();

        return fileMapper.getFiles(file);
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
        fileUtil.downloadFile(fileNmOrg ,realPath, request, response);
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
