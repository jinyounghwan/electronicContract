package com.samsung.framework.controller;

import com.samsung.framework.common.utils.FileUtil;
import com.samsung.framework.service.file.FilePublicServiceImpl;
import com.samsung.framework.vo.file.FilePublicVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SampleServiceImpl implements SampleService {

    @Autowired
    private FilePublicServiceImpl filePublicService;

    @Override
    public SampleVO getSample() {
        return new SampleVO("00", "singleObject", "email value", new String[]{"workout", "tennis", "baseball"}, "N");
    }

    @Override
    public Collection<SampleVO> getSampleList() {
        List<SampleVO> list = Arrays.asList(
                  new SampleVO("1", "ikjoo1", "ijzone@samsung.co.kr", null, "Y")
                , new SampleVO("2", "ikjoo2", "ijzone@samsung.co.kr", null, "N")
                , new SampleVO("3", "ikjoo3", "ijzone@samsung.co.kr", null, "N")
                , new SampleVO("4", "ikjoo4", "ijzone@samsung.co.kr", null, "Y")
                , new SampleVO("5", "ikjoo5", "ijzone@samsung.co.kr", null, "Y")
        );

        return list;
    }

    @Override
    public SampleVO addSample(SampleVO sampleVO) {
        return null;
    }

    @Override
    public Map<String, Object> saveSignatur(List<MultipartFile> files) {
        try {
            List<FilePublicVO> filePublicVOList =  filePublicService.uploadFile(files);
            filePublicService.saveFile(filePublicVOList);
             filePublicVOList.forEach(data ->log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+data+""));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
