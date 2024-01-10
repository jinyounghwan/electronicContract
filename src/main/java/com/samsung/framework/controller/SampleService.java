package com.samsung.framework.controller;

import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface SampleService {

    SampleVO getSample();
    Collection<SampleVO> getSampleList();

    SampleVO addSample(SampleVO sampleVO);

    Map<String, Object> saveSignatur(List<MultipartFile> files);
}
