package com.samsung.framework.domain.menu;

import com.samsung.framework.domain.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Menu extends BaseEntity {
    // 메뉴 SEQ
    @JsonProperty("menuSeq")
    private Long menuSeq;
    
    // 부모 메뉴 SEQ
    @JsonProperty("pMenuSeq")
    private Long pMenuSeq;

    //메뉴 유형 01:ADMIN , 02:USER
    private String menuType;

    // 메뉴 ID
    private String menuId;

    // 메뉴 DEPTH
    private Long menuDepth;

    // 메뉴 타이틀
    private String menuTitle;

    // 메뉴 URL
    private String menuUrl;

    // 메뉴 순서
    private Long menuOrd;

    // 노출 여부
    private String displayYn;

    // 사용 여부
    private String useYn;

    @Builder
    public Menu(String tableName, String logId1, String logId2, String logType, String logJson, String remark, String regId, Long menuSeq, Long pMenuSeq, String menuType, String menuId, Long menuDepth, String menuTitle, String menuUrl, Long menuOrd, String displayYn, String useYn){
        super(tableName, logId1, logId2, logType,logJson, remark, regId);
        this.menuSeq = menuSeq;
        this.pMenuSeq = pMenuSeq;
        this.menuType = menuType;
        this.menuId = menuId;
        this.menuDepth = menuDepth;
        this.menuTitle = menuTitle;
        this.menuUrl = menuUrl;
        this.menuOrd = menuOrd;
        this.displayYn = displayYn;
        this.useYn = useYn;
    }
}
