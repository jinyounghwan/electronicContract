package com.samsung.framework.common.utils;

import com.samsung.framework.common.enums.FileEntityType;
import com.samsung.framework.common.enums.FileTypeEnum;
import com.samsung.framework.vo.file.FilePublicVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class FileUtil {

    /**
     * 다중 파일 업로드
     * @param multipartFiles - 파일 객체 리스트
     * @return  리스트 FileVO 객체 리턴
     */
    public List<FilePublicVO> uploadFiles(final List<MultipartFile> multipartFiles, String filePath) throws Exception {
        List<FilePublicVO> files = new ArrayList<>();
        for(MultipartFile multipartFile:multipartFiles){
            if(multipartFile.isEmpty()){
                continue;
            }
            files.add(uploadFile(multipartFile,filePath));
        }
        return files;
    }

    /**
     * 단일 파일 업로드
     * @param multipartFile
     * @return 단일 FileVO 객체 리턴
     */
    public FilePublicVO uploadFile(MultipartFile multipartFile, String fileDir) throws Exception {
       String fileName = createFileName(multipartFile.getOriginalFilename());
       String nowDay = DateUtil.getUtcNowDateFormat("yyMM");
       String uploadPath = getUploadPath(nowDay, getOsRootDir()+fileDir) + "/" + fileName;

       File uploadFile = new File(uploadPath);

       try{
           multipartFile.transferTo(uploadFile);
       } catch(Exception e){
           log.error(e.getMessage());
       }

       return FilePublicVO.builder()
                    .filePath(fileDir+ "/"+nowDay+"/"+fileName)
                    .fileNm(fileName)
                    .fileSize(String.valueOf(multipartFile.getSize()))
                    .fileNmOrg(multipartFile.getOriginalFilename())
                    .build();
    }

    /**
     * 파일 이름 생성
     * @return 파일 이름 생성
     */
    public static String createFileName(String fileName) throws Exception {
        return (DateUtil.getUtcNowDateFormat("yyMMdd") + StringUtil.getRandomStringByUUID() + getFileExtension(fileName)).replaceAll(" ","");
    }

    /**
     * 파일 업로드 임시 경로 (Temp)
     * @param addPath -> 폴더 Path
     * @return 업로드 경로
     */
    private String getUploadPath(final String addPath, String filePath){
        return makeDirectories(filePath+ "/" + addPath);
    }

    /**
     * 업로드 폴더(디렉터리) 생성
     * @param path 업로드 경로
     * @return 업로드 경로
     */
    static String makeDirectories(final String path){
        File dir = new File(path);
        if(!dir.exists()){
            if(dir.mkdirs()) {
                log.info("폴더 생성 성공");
            } else{
                log.info("폴더 생성 실패");
            }
        }
        return path;
    }

    /**
     *
     * 업로드 파일 확장자 타입
     * @param fileName
     * @return String 파일 타입
     */
    public String checkFileType(final String fileName){
        String extension = fileName.substring(fileName.indexOf(".")+1).toLowerCase();
        FileTypeEnum fileType=null;
        switch (extension) {
            // 이미지 파일 확장자
            case "jpg", "jfif", "jpeg", "png", "gif" -> fileType=FileTypeEnum.IMAGE;
            // 동영상 파일 확장자
            case "mp4", "mov", "wmv", "avi" -> fileType=FileTypeEnum.VIDEO;
            //엑셀 파일 확장자
            case "xls", "xlsx" -> fileType=FileTypeEnum.EXCEL;
            //워드 파일 확장자
            case "doc", "docx" -> fileType=FileTypeEnum.WORD;
            // 한글 파일 확장자
            case "hwp" -> fileType=FileTypeEnum.HANGEUL;
            // 그외 다른 파일
            default -> fileType= FileTypeEnum.OTHER;
        }
        return fileType.getFileType();
    }

    /**
     * 다중 파일 이동
     * @param fileNames 파일 이름 리스트
     * @param fromFilePath 이동 해야 할 파일 Path
     * @param toFilePath 이동 후 파일 Path
     */
    public void moveFiles(final List<String> fileNames, String fromFilePath, String toFilePath) throws Exception {
        for(String  fileName : fileNames){
            moveFile(fileName, fromFilePath, toFilePath);
        }
    }

    /**
     * 단일 파일 이동
     * @param fileName 파일 이름
     * @param fromFilePath 이동 해야 할 파일 Path
     * @param toFilePath 이동 후 파일 Path
     */
    public void moveFile(final String fileName, String fromFilePath, String toFilePath) throws Exception {
        String nowDay = DateUtil.getUtcNowDateFormat("yyMM");
        String beforeFilePath = getUploadPath(nowDay, getOsRootDir()+ fromFilePath)+ "/" + fileName;
        String afterFilePath = makeDirectories(getOsRootDir() + toFilePath)+ "/" + fileName;

        Path file = Paths.get(beforeFilePath);
        Path moveFile = Paths.get(afterFilePath);

        try{
            Path newFile = Files.move(file, moveFile);
            log.info("newFiles : { " + newFile + " }");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 단 건 Directory 파일 삭제
     * @param filePath
     */
    public void deleteFile(String filePath){
         String realPath = getOsRootDir()+filePath;
        try{
            File file = new File(realPath);

            if(file.delete()){
                log.info("파일 삭제 성공 : { " + realPath + " } ");
            } else {
                log.info("파일 삭제 실패 : { " + realPath + " } ");
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 파일 확장자 이름
     * @param fileName
     * @return 파일 확장자 이름
     */
    public static String getFileExtension(String fileName){
        int index = fileName.indexOf(".");
        return fileName.substring(index);
    }

    /**
     * 알맞은 파일 테이블 형식을 반환
     * @param entityName
     * @return
     */
    public Optional<FileEntityType> getFileEntityType(String entityName){
        Optional<FileEntityType> any = Arrays.stream(FileEntityType.values())
                                            .filter(v -> v.getTargetTable().equals(entityName))
                                            .findAny();
        return any;
    }

    /**
     * OS별 Root Directory 설정
     * @return rootDirectory
     */
    public static String getOsRootDir(){
        String os = System.getProperty("os.name").toLowerCase();
        String fileRootPath = "";
        if(os.contains("win")) {
            fileRootPath = "C:" ;
        }else if(os.contains("nix") || os.contains("nux") || os.contains("aix")){
            fileRootPath = "/home";
        }

        return fileRootPath;
    }

    /**
     * 파일 다운로드
     * @param originFileName
     * @param filePath
     * @param response
     * @param request
     * @return
     * @throws IOException
     */
    public void downloadFile(String originFileName, String filePath, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            File file = new File(filePath);

            log.info("Download FileName : {}", originFileName);

            MediaType mediaType = getMediaType(filePath);
            log.info("File mediaType : {}", mediaType);

            // User-Agent : 어떤 운영체제로 어떤 브라우저에 접근하는지 확인함.
            String userAgent = request.getHeader("User-Agent");
            log.info("userAgent info : {}", userAgent);

            String fileName = createDownloadFileName(userAgent, originFileName);

            log.info("인코드 파일명 => 디코드로 전환 : {}", URLDecoder.decode(fileName));

            log.info("다운로드 파일명 : {} ", fileName);


            response.setHeader("Content-Type", String.valueOf(getMediaType(filePath)));
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\";", fileName));
            //response.setHeader("Content-Disposition", "attachment; filename*=utf-8''" + fileName + ";");

            InputStream is = new FileInputStream(file);
            OutputStream out = response.getOutputStream();

            FileCopyUtils.copy(is, out);

            is.close();
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 미디어 타입 가져옴
     * @param path
     * @return
     * @throws IOException
     */
    public MediaType getMediaType(String path) throws IOException {
       // 파일 이름에서 확장자 추출
       String fileExtension = Files.probeContentType(Paths.get(path));
       
       // Content-Type 헤더 설정
       MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM; // 기본 설정
        if(fileExtension != null) {
            mediaType = MediaType.parseMediaType(fileExtension);
        }

        return mediaType;
    }

    /**
     * 파일을 바이트 배열 Array로 읽어온다.
     * @param filePath
     * @return
     * @throws IOException
     */
    public byte[] readFileToByteArray(String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        byte[] fileBytes = new byte[(int)file.length()];

        // 파일 내용을 바이트 배열로 읽기
        fis.read(fileBytes);

        // 파일 스트림 닫기
        fis.close();

        return fileBytes;
    }

    /**
     * 다운로드 할 파일명 UTF-8 or ISO_8859로 변경
     * @param userAgent
     * @param originFileName
     * @return
     */
    public String createDownloadFileName(String userAgent, String originFileName){
        String encodedFileName = null;

        if (userAgent != null && userAgent.contains("MSIE")) {
            // Internet Explorer에서는 URLEncoder로 한글 파일 이름을 인코딩
            encodedFileName = UriUtils.encode(originFileName, StandardCharsets.UTF_8).replace("\\+", "%20");
        } else if (userAgent != null && (userAgent.contains("Chrome") || userAgent.contains("Opera"))) {
            // Chrome 또는 Opera에서는 파일 이름을 그대로 사용하고 한글 부분을 UTF-8로 인코딩
            encodedFileName = new String(originFileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        } else {
            // 다른 브라우저에서는 UTF-8로 인코딩
            encodedFileName = UriUtils.encode(originFileName, StandardCharsets.UTF_8);
        }

        return encodedFileName;
    }

    public static Long getFileSize(String filePath){
        File f = new File(filePath);

        return f.length();
    }
}
