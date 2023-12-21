package com.samsung.framework.domain.authority;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@EqualsAndHashCode(of = {"menuSeq", "menuCd"}, callSuper = false)
@NoArgsConstructor
@Getter
public class Authority {
    @Setter
    private long menuSeq;
    @Setter
    private String menuCd;
    private int menuOrd;
    @Setter
    private String authC;
    @Setter
    private String authR;
    @Setter
    private String authU;
    @Setter
    private String authD;
    @Setter
    private String authF;
    @Setter
    private String userId;
    @Setter
    private String displayUseYn;
    private String useYn;
    private String delyn;
    private LocalDateTime regDt;
    private String regId;
    private LocalDateTime lastDt;
    private String lastId;

    // 권한 대상 사용자 ID
    private List<String> userIdList;

    @Builder
    public Authority(long menuSeq, String menuCd, int menuOrd, String authC, String authR, String authU, String authD, String authF, String userId, String displayUseYn, String useYn, String delyn, LocalDateTime regDt, String regId, LocalDateTime lastDt, String lastId, List<String> userIdList) {
        this.menuSeq = menuSeq;
        this.menuCd = menuCd;
        this.menuOrd = menuOrd;
        this.authC = authC;
        this.authR = authR;
        this.authU = authU;
        this.authD = authD;
        this.authF = authF;
        this.userId = userId;
        this.displayUseYn = displayUseYn;
        this.useYn = useYn;
        this.delyn = delyn;
        this.regDt = regDt;
        this.regId = regId;
        this.lastDt = lastDt;
        this.lastId = lastId;
        this.userIdList = userIdList;
    }
}
