package com.samsung.framework.controller;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
public class SampleServiceImpl implements SampleService {

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
}
