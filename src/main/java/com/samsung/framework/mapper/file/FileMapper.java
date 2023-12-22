package com.samsung.framework.mapper.file;

import com.samsung.framework.mapper.common.GeneralMapper;
import com.samsung.framework.vo.file.FilePublicVO;
import com.samsung.framework.vo.file.FileVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface FileMapper extends GeneralMapper {
    List<FilePublicVO> getFiles(Object obj);
    FilePublicVO getFile(Object obj);
    int save(Object obj);
    int deleteFile(Object obj);
    int saveExcelFile(Object obj);
    int deleteFileList(Map<String, Object> list);
}
