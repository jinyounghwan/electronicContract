package com.samsung.framework.vo.search.account;

import com.samsung.framework.vo.search.SearchVO;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountSearchVO extends SearchVO {
    // 직원 사번
    private int empNo;
    private String deptCode;
    private String userId;
    private String adminId;
    private String userPw;
    private String name;
    private String accountType; // 계정 타입
    private String position;
    private String email; // 권한 임시로 String으로 변경
    private String phone;
    private String useYn;
    private LocalDateTime employedAt;
    private LocalDateTime resignedAt;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;

    public void setSearchVO(SearchVO searchVO){
       this.setSearchKeyword(searchVO.getSearchKeyword());
       this.setSearchKeywordType(searchVO.getSearchKeywordType());
       this.setSearchDateType(searchVO.getSearchDateType());
       this.setStartDt(searchVO.getStartDt());
       this.setEndDt(searchVO.getEndDt());
    }
}
