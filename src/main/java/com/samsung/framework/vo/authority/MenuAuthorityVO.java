package com.samsung.framework.vo.authority;

import lombok.*;

import java.time.LocalDateTime;

@ToString
@EqualsAndHashCode(of = {"menuSeq", "memberId"})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class MenuAuthorityVO {
    private int authSeq;
    private int menuSeq;
    private String authC;
    private String authR;
    private String authU;
    private String authD;
    private String authF;
    private String grantTo;
    private String displayYn;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
}
