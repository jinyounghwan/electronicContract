package com.samsung.framework.service.common;

import com.samsung.framework.common.utils.*;
import com.samsung.framework.mapper.common.CommonMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Service에서 상속받아서 사용할 것
 */
@Getter
public class ParentService {

    /** Util [[ **/
    @Autowired
    private PropertiesUtil propertiesUtil;
    @Autowired
    private StringUtil stringUtil;
    @Autowired
    private DateUtil dateUtil;
    @Autowired
    private ValidationUtil validationUtil;
    @Autowired
    private FileUtil fileUtil;
    @Autowired
    private ExcelUtil excelUtil;
    /** Util ]] **/

    /** Mapper [[ **/
    @Autowired
    private CommonMapper commonMapper;
    /** Mapper ]] **/

}
