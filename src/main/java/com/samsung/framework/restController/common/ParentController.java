/*
package com.samsung.framework.restController.common;

import com.utils.common.framework.samsung.CommonUtil;
import com.utils.common.framework.samsung.FileUtil;
import com.utils.common.framework.samsung.MapUtil;
import com.utils.common.framework.samsung.ObjectHandlingUtil;
import com.common.service.framework.samsung.CommonService;
import com.common.vo.framework.samsung.TokenVO;
import com.member.vo.framework.samsung.MemberVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

*/
/**
 * 부모 controller
 * - 모든 controller는 상속받아 사용할 것
 *//*

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

    */
/**
     * 토근 관련 VO
     * @return
     *//*

    public TokenVO getTokenVO() {
        return ObjectHandlingUtil.extractTokenInfo(request);
    }

    */
/**
     * 회원 관련 VO
     * @return
     *//*

    public MemberVO getLoginVO() {
        return ObjectHandlingUtil.extractLoginInfo(request);
    }

}
*/
