/*
package com.samsung.framework.restController.utils;

import com.enums.common.framework.samsung.MapKeyStringEnum;
import com.enums.common.framework.samsung.ResultCodeMsgEnum;
import com.utils.common.framework.samsung.ObjectHandlingUtil;
import com.samsung.framework.restController.common.ParentController;
import com.common.domain.framework.samsung.TokenObjectVO;
import com.member.domain.framework.samsung.LoginRequest;
import com.common.vo.framework.samsung.ResultStatusVO;
import com.common.vo.framework.samsung.TokenVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

*/
/**
 * 토큰 API
 *//*

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/token")
public class TokenController extends ParentController {

    */
/**
     * 토큰(JWT) 생성 API
     * @param loginRequest
     * @return {@link ResponseEntity}
     *//*

    @PostMapping(value = {"", "/"})
    public ResponseEntity createJWT(@RequestBody @Valid LoginRequest loginRequest) {
        TokenObjectVO tokenObjectVO = getCommonUtil().getTokenFactory().createJWT(loginRequest.getUserName(), loginRequest.getPassword(), loginRequest.getAuthority());
        ResultStatusVO resultStatusVO = ObjectHandlingUtil.setSingleObjResultStatusVO(tokenObjectVO, ResultCodeMsgEnum.NO_DATA);
        var mapKeyList = Arrays.asList(MapKeyStringEnum.TOKEN_OBJECT.getKeyString());
        Map<String, Object> resultMap = getMapUtil().responseEntityBodyWrapper(resultStatusVO, mapKeyList, tokenObjectVO);

        return ResponseEntity.ok(resultMap);
    }

    */
/**
     * 인증 정보 확인 API
     * @param authentication
     * @return {@link ResponseEntity}
     *//*

    @PostMapping("/authentication-info")
    public ResponseEntity getAuthentication(HttpServletRequest request, Authentication authentication) {
        log.info("Authentication: userName => {}", authentication.getName());
        authentication.getAuthorities().forEach(a -> log.info("권한: {}", a.getAuthority()));
        TokenVO tokenVO = ObjectHandlingUtil.extractTokenInfo(request);
        return ResponseEntity.ok(tokenVO.toString());
    }

    */
/**
     * 토큰(JWT) 재발행 API
     * @param request
     * @param authentication
     * @return {@link ResponseEntity}
     *//*

    @PostMapping("/reissue-token")
    public ResponseEntity modifyToken(HttpServletRequest request, Authentication authentication) {
        log.info(authentication.toString());
        String reissueYn = (String) request.getAttribute(MapKeyStringEnum.TOKEN_REISSUE.getKeyString());

        TokenObjectVO tokenObjectVO = null;
        HttpStatus status = HttpStatus.OK;
        if(reissueYn.equalsIgnoreCase("N")) {
            status = HttpStatus.BAD_REQUEST;
        } else {
            Set<String> authoritySet = Set.of(authentication.getAuthorities().stream().toArray()[0].toString());
            tokenObjectVO = getCommonUtil().getTokenFactory().createJWT(authentication.getPrincipal().toString(), null, authoritySet);
        }

        ResultStatusVO resultStatusVO = ObjectHandlingUtil.setSingleObjResultStatusVO(tokenObjectVO, ResultCodeMsgEnum.INVALID_HEADER);
        var mapKeyList = Arrays.asList(MapKeyStringEnum.TOKEN_OBJECT.getKeyString());
        Map<String, Object> resultMap = getMapUtil().responseEntityBodyWrapper(resultStatusVO, mapKeyList, tokenObjectVO);

        return ResponseEntity.status(status).body(resultMap);
    }
}
*/
