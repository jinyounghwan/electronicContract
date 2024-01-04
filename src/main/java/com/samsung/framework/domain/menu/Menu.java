package com.samsung.framework.domain.menu;

import com.samsung.framework.domain.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Menu extends BaseEntity {
    // 메뉴 SEQ
//    @JsonProperty("menuSeq")
    private Long menuSeq;
    
    // 메뉴 코드
    private String menuCode;

    // 메뉴 명
    private String name;

    // 메뉴 순서
    private int ord;

    // 노출 여부
    private String displayYn;

    // 사용 여부
    private String useYn;

    // 생성자
    private String createdBy;

    // 생성일
    private LocalDateTime createdAt;

    // 수정자
    private String updatedBy;

    // 수정일
    private LocalDateTime updatedAt;

    // 부모 메뉴 SEQ
//    @JsonProperty("pMenuSeq")
//    private Long pMenuSeq;

    @Builder
    public Menu(String tableName, String logId1, String logId2, String logType, String logJson, String remark, String regId, Long menuSeq, String menuCode, String name, int ord, String displayYn, String useYn, String createdBy, LocalDateTime createdAt,String updatedBy, LocalDateTime updatedAt){
        super(tableName, logId1, logId2, logType,logJson, remark, regId);
        this.menuSeq = menuSeq;
        this.menuCode = menuCode;
        this.name = name;
        this.ord = ord;
        this.displayYn = displayYn;
        this.useYn = useYn;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedBy = updatedBy;
        this.updatedAt = updatedAt;
//      this.pMenuSeq = pMenuSeq;
    }
}
