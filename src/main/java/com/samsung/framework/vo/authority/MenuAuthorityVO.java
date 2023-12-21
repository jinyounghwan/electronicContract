package com.samsung.framework.vo.authority;

import lombok.*;

@ToString
@EqualsAndHashCode(of = {"menuSeq", "memberId"})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class MenuAuthorityVO {
    private int menuSeq;
    private String memberId;
    private String deptCode;
    private String authLevel;
    private boolean read;
    private boolean create;
    private boolean update;
    private boolean delete;
    private boolean download;

    public void authLevelDivider() {
        if(!authLevel.isBlank()) {
            this.read = authLevelYn(authLevel.substring(0, 1));
            this.create = authLevelYn(authLevel.substring(1, 2));
            this.update = authLevelYn(authLevel.substring(2, 3));
            this.delete = authLevelYn(authLevel.substring(3, 4));
            this.download = authLevelYn(authLevel.substring(4, 5));
        }
    }

    private boolean authLevelYn(String authValue) {
        if(authValue.equals("1")) {
            return true;
        }
        return false;
    }
}
