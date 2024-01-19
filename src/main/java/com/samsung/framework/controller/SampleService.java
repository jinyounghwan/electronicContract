package com.samsung.framework.controller;

import com.samsung.framework.vo.search.SearchVO;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface SampleService {

    SampleVO getSample();
    SampleVO addSample(SampleVO sampleVO);

    Map<String, Object> saveSignatur(List<MultipartFile> files);

    int getSampleListTotal(SearchVO searchVO);

    List<Map<String, Object>> getSampleList(SearchVO searchVO);
}
