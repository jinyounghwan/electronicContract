package com.samsung.framework.mapper.file;

import com.samsung.framework.vo.file.FilePublicVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface FileMapper {
    List<FilePublicVO> getFiles(Object obj);
    FilePublicVO getFile(Object obj);
    int save(Object obj);
    int deleteFile(Object obj);
    int saveExcelFile(Object obj);
    int deleteFileList(Map<String, Object> list);
}
