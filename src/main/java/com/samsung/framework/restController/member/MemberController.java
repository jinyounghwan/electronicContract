/*
package com.samsung.framework.restController.member;

import com.enums.common.framework.samsung.MapKeyStringEnum;
import com.enums.common.framework.samsung.RequestTypeEnum;
import com.enums.common.framework.samsung.ResultCodeMsgEnum;
import com.utils.common.framework.samsung.ObjectHandlingUtil;
import com.samsung.framework.restController.common.ParentController;
import com.common.domain.framework.samsung.SearchObject;
import com.common.domain.framework.samsung.TokenObjectVO;
import com.member.domain.framework.samsung.Member;
import com.user.domain.framework.samsung.SignUpRequest;
import com.common.vo.framework.samsung.CollectionPagingVO;
import com.common.vo.framework.samsung.ResultStatusVO;
import com.member.vo.framework.samsung.MemberVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

*/
/**
 * 회원 API
 *//*

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class RestMemberController extends ParentController {

    */
/**
     * 회원 목록 조회 API
     * @param request
     * @param searchObject
     * @return {@link ResponseEntity}
     *//*

    @GetMapping(value = {"", "/"})
    public ResponseEntity memberList(HttpServletRequest request, @RequestBody @Valid SearchObject searchObject) {
        CollectionPagingVO collectionPagingVO = getCommonService().getMemberService().getMemberList(searchObject);
        ResultStatusVO resultStatusVO = ObjectHandlingUtil.setListResultStatusVO(collectionPagingVO.getObjects().stream().toList(), ResultCodeMsgEnum.NO_DATA);
        
        var mapKeyList = Arrays.asList(MapKeyStringEnum.PAGING.getKeyString(), MapKeyStringEnum.MEMBER_LIST.getKeyString());
        var resultMap = getMapUtil().responseEntityBodyWrapper(resultStatusVO, mapKeyList, collectionPagingVO.getPaging(), collectionPagingVO.getObjects());

        return ResponseEntity.ok(resultMap);
    }

    */
/**
     * 회원가입 API
     * @param signUpRequest
     * @return {@link ResponseEntity}
     *//*

    @PostMapping("/api/sign-up")
    public ResponseEntity signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        TokenObjectVO tokenObjectVO = getCommonService().getMemberService().signUp(signUpRequest);
        ResultStatusVO resultStatusVO = ObjectHandlingUtil.setDataManipulationResultStatusVO(tokenObjectVO, RequestTypeEnum.CREATE);

        var mapKeyList = Arrays.asList(MapKeyStringEnum.TOKEN_OBJECT.getKeyString());
        var resultMap = getMapUtil().responseEntityBodyWrapper(resultStatusVO, mapKeyList, tokenObjectVO);

        return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
    }

    */
/**
     * 회원 상세 조회 API
     * @param id
     * @return {@link ResponseEntity}
     *//*

    @GetMapping("{id}")
    public ResponseEntity findMemberById(@PathVariable int id) {
        MemberVO memberVO = getCommonService().getMemberService().findMemberById(id);
        ResultStatusVO resultStatusVO = ObjectHandlingUtil.setSingleObjResultStatusVO(memberVO, ResultCodeMsgEnum.NO_DATA);

        var mapKeyList = Arrays.asList(MapKeyStringEnum.MEMBER.getKeyString());
        var resultMap = getMapUtil().responseEntityBodyWrapper(resultStatusVO, mapKeyList, memberVO);

        return ResponseEntity.ok(resultMap);
    }


//    -----
    @Deprecated
    @PostMapping({"", "/"})
    public ResponseEntity insertMember(@RequestBody @Valid Member member) {
        Member inserted = getCommonService().getMemberService().insertMember(member);
        ResultStatusVO resultStatusVO = ObjectHandlingUtil.setSingleObjResultStatusVO(inserted, ResultCodeMsgEnum.NO_DATA);

        var mapKeyList = Arrays.asList(MapKeyStringEnum.MEMBER.getKeyString());
        var resultMap = getMapUtil().responseEntityBodyWrapper(resultStatusVO, mapKeyList, inserted);

        return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
    }

    @Deprecated
    @PostMapping("request-param-validity")
    public ResponseEntity requestParamTest(@RequestBody @Valid Member member) {
        getCommonService().getMemberService().insertMember(member);
        ResultStatusVO resultStatusVO = ObjectHandlingUtil.setSingleObjResultStatusVO(member, ResultCodeMsgEnum.NO_DATA);
        var mapKeyList = Arrays.asList(MapKeyStringEnum.MEMBER.getKeyString());
        var resultMap = getMapUtil().responseEntityBodyWrapper(resultStatusVO, mapKeyList, member);

        return ResponseEntity.ok(resultMap);
    }

}
*/
