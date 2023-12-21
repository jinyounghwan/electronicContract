/*
package com.samsung.framework.restController.login;

import com.enums.common.framework.samsung.MapKeyStringEnum;
import com.enums.common.framework.samsung.ResultCodeMsgEnum;
import com.utils.common.framework.samsung.ObjectHandlingUtil;
import com.samsung.framework.restController.common.ParentController;
import com.common.domain.framework.samsung.TokenObjectVO;
import com.member.domain.framework.samsung.LoginRequest;
import com.common.vo.framework.samsung.ResultStatusVO;
import com.member.vo.framework.samsung.MemberVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/login")
public class RestLoginController extends ParentController {

    */
/**
     * 로그인 요청 API
     * @param loginRequest
     * @return
     *//*

    @PostMapping({"", "/"})
    public ResponseEntity login(@RequestBody @Valid LoginRequest loginRequest) {
        MemberVO loginInfo = getCommonService().getMemberService().getLoginInfo(loginRequest);
        Map<String, Object> resultMap;

        if(loginInfo != null) {
            TokenObjectVO tokenObjectVO = getCommonUtil().getTokenFactory().createJWT(loginInfo.getMemberId(), null, loginInfo.getAuthority());
            ResultStatusVO resultStatusVO = ObjectHandlingUtil.setSingleObjResultStatusVO(tokenObjectVO, ResultCodeMsgEnum.NO_DATA);
            var mapKeyList = Arrays.asList(MapKeyStringEnum.TOKEN_OBJECT.getKeyString());
            resultMap = getMapUtil().responseEntityBodyWrapper(resultStatusVO, mapKeyList, tokenObjectVO);
        }else  {
            resultMap = getMapUtil().responseEntityBodyWrapper(new ResultStatusVO(ResultCodeMsgEnum.NOT_LOGGED_IN.getCode(), ResultCodeMsgEnum.NOT_LOGGED_IN.getMsg()));
        }

        return ResponseEntity.ok(resultMap);
    }

    // TODO: IJ OAuth 로직 보류
    @PostMapping("/oauth2/google")
    public ResponseEntity oauth2Google() {

        return null;
    }

}
*/
