package com.samsung.framework.service.test;

import com.samsung.framework.common.utils.CryptoUtil;
import com.samsung.framework.common.utils.JwtUtil;
import com.samsung.framework.domain.common.Paging;
import com.samsung.framework.domain.member.LoginRequest;
import com.samsung.framework.vo.common.PagingVO;
import com.samsung.framework.vo.test.TestVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TestService {

    private final JwtUtil jwtUtil;
    private final CryptoUtil cryptoUtil;

    private Long expiredMs = 1000 * 60 * 60l;

    public List<TestVO> getTest() {
        List list = new ArrayList<>();
        list.add(new TestVO(1234, "test"));

        return list;
    }

    /**
     * Paging
     * @param currentPage
     * @param displayRow
     * @param totalCount
     * @return
     */
    public PagingVO paging(int currentPage, int displayRow, int totalCount) {
        Paging paging = Paging.builder()
                .currentPage(currentPage)
                .displayRow(displayRow)
                .totalCount(totalCount)
                .build();

        return PagingVO.builder()
                .totalCount(paging.getTotalCount())
                .totalPage(paging.getTotalPage())
                .build();
    }

    /**
     * 비밀번호 암호화 테스트
     */
    public void passwordEncode(@Valid LoginRequest loginRequest) {
        String rawPassword = "ijzone";
        String encodedPassword = cryptoUtil.encodePassword(loginRequest.getPassword());
        log.info("encodedPassword: {}", encodedPassword);
//        boolean passwordMatches = cryptoUtil.isPasswordMatches(loginRequest.getPassword(), encodedPassword);
//        log.info("passwordMatches? {}", passwordMatches);
    }

}
