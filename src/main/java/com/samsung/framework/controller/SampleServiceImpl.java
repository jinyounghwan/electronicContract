package com.samsung.framework.controller;

import com.samsung.framework.common.utils.FileUtil;
import com.samsung.framework.mapper.sample.SampleMapper;
import com.samsung.framework.service.file.FileService;
import com.samsung.framework.vo.account.AccountVO;
import com.samsung.framework.vo.file.FilePublicVO;
import com.samsung.framework.vo.search.SearchVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class SampleServiceImpl implements SampleService {
    private final SampleMapper sampleMapper;

    private FileService fileService;

    @Override
    public SampleVO getSample() {
        return new SampleVO("00", "singleObject", "email value", new String[]{"workout", "tennis", "baseball"}, "N");
    }


    @Override
    public SampleVO addSample(SampleVO sampleVO) {
        return null;
    }

    @Override
    public Map<String, Object> saveSignatur(List<MultipartFile> files) {
        try {
            List<FilePublicVO> filePublicVOList =  fileService.uploadFile(files);

            fileService.saveFile(filePublicVOList, "");
             filePublicVOList.forEach(data ->log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+data+""));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public int getSampleListTotal(SearchVO searchVO) {
        return sampleMapper.getSampleListTotal(searchVO);
    }

    @Override
    public List<Map<String, Object>> getSampleList(SearchVO searchVO) {
        return sampleMapper.getSampleList(searchVO);
    }


}
