package com.samsung.framework.service.member;

import com.samsung.framework.common.enums.ExceptionCodeMsgEnum;
import com.samsung.framework.common.exception.CustomLoginException;
import com.samsung.framework.common.utils.ObjectHandlingUtil;
import com.samsung.framework.common.utils.TokenFactory;
import com.samsung.framework.common.utils.ValidationUtil;
import com.samsung.framework.domain.common.Paging;
import com.samsung.framework.domain.common.SearchObject;
import com.samsung.framework.domain.common.TokenObjectVO;
import com.samsung.framework.domain.member.LoginRequest;
import com.samsung.framework.domain.member.Member;
import com.samsung.framework.domain.user.SignUpRequest;
import com.samsung.framework.domain.user.User;
import com.samsung.framework.service.common.ParentService;
import com.samsung.framework.vo.common.CollectionPagingVO;
import com.samsung.framework.vo.member.MemberVO;
import com.samsung.framework.vo.search.SearchVO;
import com.samsung.framework.vo.user.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
@Primary
public class MemberServiceImpl extends ParentService implements MemberService{
    // TODO: IJ NPE 위험코드 Optional로 변경
    private final ValidationUtil validationUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenFactory tokenFactory;

    /**
     * 멤버 목록 조회
     * @param searchObject {@link SearchObject}
     * @return {@link CollectionPagingVO}
     */
    public CollectionPagingVO getMemberList(SearchObject searchObject) {

        int totalCount = getCommonMapper().getMemberMapper().memberListCount();
        CollectionPagingVO collectionPagingVO = null;
        if(totalCount > 0) {
            int totalSearchCount = getCommonMapper().getMemberMapper().memberListSearchCount(searchObject.getSearch());
            Paging paging = ObjectHandlingUtil.pagingOperator(searchObject, totalSearchCount);
            SearchVO searchVO = new SearchVO();
            searchVO.setPaging(paging);
            List<MemberVO> memberList = getCommonMapper().getMemberMapper().getMemberList(searchVO);
            collectionPagingVO = CollectionPagingVO.builder()
                    .objects(memberList)
                    .paging(paging)
                    .build();
        }else {
            collectionPagingVO = CollectionPagingVO.builder()
                    .objects(new ArrayList<>())
                    .paging(Paging.builder().currentPage(1).build())
                    .build();
        }

        return collectionPagingVO;
    }

    /**
     * 멤버 상세 조회
     * @param id int {@link Integer}
     * @return {@link MemberVO}
     */
    public MemberVO findMemberById(int id){
        MemberVO memberVO = getCommonMapper().getMemberMapper().findMemberById(id);

        if(memberVO == null) {
            return null;
        }

        MemberVO target = MemberVO.builder()
                .userSeq(memberVO.getUserSeq())
                .userId(memberVO.getUserId())
                .deptCd(memberVO.getDeptCd())
                .authority(memberVO.getDeptCd())
                .userNm(memberVO.getUserNm())
                .userEmail(memberVO.getUserEmail())
                .build();

        return target;
    }

    /**
     * 멤버 상세 조회
     * @param userName {@link String}
     * @return {@link MemberVO}
     */
    public MemberVO findMemberByUserName(String userName) {
        return getCommonMapper().getMemberMapper().findMemberByUserName(userName);
    }

    /** TODO: 멤버등록 RestContorller 버전
     * 멤버 등록
     * @param member {@link Member}
     * @return {@link Member}
     */
    public Member insertMember(Member member) {
        var target = Member.builder()
                .memberId(member.getMemberId())
//                .password(bCryptPasswordEncoder.encode(member.getPassword()))
                .memberName(member.getMemberName())
                .email(member.getEmail())
                .build();

        validationUtil.parameterValidator(target, Member.class);
        getCommonMapper().getMemberMapper().insertMember(target);

        return target;
    }


    /**
     * 사용자 등록(w/ token)
     * @param signUpRequest {@link SignUpRequest}
     * @return {@link TokenObjectVO}
     */
    public TokenObjectVO signUp(SignUpRequest signUpRequest) {
        var target = User.builder()
                .empNo(signUpRequest.getEmpNo())
                .deptCode(signUpRequest.getDeptCode())
                .userId(signUpRequest.getUserId())
                .userPw(bCryptPasswordEncoder.encode(signUpRequest.getUserPw()))
//                .password(bCryptPasswordEncoder.encode(signUpRequest.getPassword()))
                .name(signUpRequest.getName())
                .accountType(signUpRequest.getAccountType())
                .position(signUpRequest.getPosition())
                .email(signUpRequest.getEmail())
                .phone(signUpRequest.getPhone())
                .build();

        int inserted = getCommonMapper().getMemberMapper().insert(target);
        if(inserted > 0) { // TOKEN 추후 제거
            TokenObjectVO tokenObjectVO = tokenFactory.createJWT(signUpRequest.getUserId(), signUpRequest.getUserPw(), "ROLE_USER");
            return tokenObjectVO;
        }

        return new TokenObjectVO();
    }

    /**
     * 로그인 정보 조회
     * @param loginRequest {@link LoginRequest}
     * @return {@link MemberVO}
     */
    public MemberVO getLoginInfo(LoginRequest loginRequest) {

        MemberVO target = getCommonMapper().getMemberMapper().getLoginInfo(loginRequest.getUserId());
        if(target == null) {
            try {
                throw new CustomLoginException(ExceptionCodeMsgEnum.ACCOUNT_NOT_EXISTS.getCode(), ExceptionCodeMsgEnum.ACCOUNT_NOT_EXISTS.getMsg());
            } catch (CustomLoginException e) {
                throw new RuntimeException(e);
            }
        }

        if (loginRequest.getPassword().equals(target.getUserPwd())) {
            return MemberVO.builder()
                    .userId(target.getUserId())
                    .userNm(target.getUserNm())
                    .userEmail(target.getUserEmail())
                    .authority(target.getDeptCd())
                    .deptCd(target.getDeptCd())
                    /*.tableName(TableNameEnum.LOGIN.name())
                      .logId1(String.valueOf(target.getMemberSeq()))
                      .logType(RequestTypeEnum.LOGIN.getRequestType())
                      .logId2(null)
                      .logJson(null)
                      .remark(null)
                      .regId(target.getUserId())*/
                    .build();
        }else {
            try {
                throw new CustomLoginException(ExceptionCodeMsgEnum.INVALID_PASSWORD.getCode(), ExceptionCodeMsgEnum.INVALID_PASSWORD.getMsg());
            } catch (CustomLoginException e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * password 변경하기
     * @param member
     * @return
     */
    @Override
    public Map<String, Object> updatePwd(MemberVO member) {
        HashMap<String, Object> result = new HashMap<>();
        Member target = Member.builder()
                                .memberId(member.getUserId())
//                                .password(bCryptPasswordEncoder.encode(member.getUserPwd()))
                                .build();
        int update= getCommonMapper().getMemberMapper().updatePwd(target);

        if(update > 0) {
            result.put("result","success");
        } else {
            result.put("result", "fail");
        }

        return result;
    }

    @Override
    public List<UserVO> findAllUsers(SearchVO searchVO) {
        List<UserVO> list = getCommonMapper().getMemberMapper().findAllUsers(searchVO);

        return list;
    }


}
