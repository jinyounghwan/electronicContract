package com.samsung.framework.controller.common;


import com.samsung.framework.common.utils.CommonUtil;
import com.samsung.framework.common.utils.FileUtil;
import com.samsung.framework.common.utils.MapUtil;
import com.samsung.framework.common.utils.ObjectHandlingUtil;
import com.samsung.framework.service.common.CommonService;
import com.samsung.framework.vo.account.AccountVO;
import com.samsung.framework.vo.common.TokenVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
public class ParentController {

    @Autowired
    private CommonService commonService;
    @Autowired
    private MapUtil mapUtil;
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private FileUtil fileUtil;

    /**
     * 토근 관련 VO
     * @return
     */
    public TokenVO getTokenVO() {
        return ObjectHandlingUtil.extractTokenInfo(request);
    }

    /**
     * 회원 관련 VO
     * @return
     */
    public AccountVO getLoginVO() {
        return ObjectHandlingUtil.extractLoginInfo(request);
    }
}
