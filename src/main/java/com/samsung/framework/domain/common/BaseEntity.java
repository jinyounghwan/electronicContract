package com.samsung.framework.domain.common;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * 테이블 별 데이터 변경 처리 이력 저장을 위한 클래스<br>
 * 각 테이블의 데이터 변경이 일어나는 대상 객체에서 해당 클래스 상속 받아서 사용
 * @implNote 해당 클래스를 상속받는 객체에서 반드시 기본 생성자 생성
 * @apiNote 관리 테이블: ENTITY_LOG, ENTITY_HISTORY
 */
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = {"tableName", "logId1", "logType"})
public class BaseEntity {

    @NotNull(message = "테이블 명은 필수 값 입니다.")
    private String tableName;
    @Setter
    @NotNull(message = "엔티티 logId1은 필수 값 입니다.")
    private String logId1;
    private String logId2;
    private String logType;
    private String logJson;
    private String remark;
    @NotNull(message = "등록 ID는 필수 값 입니다.")
    private String regId;

    public BaseEntity(String tableName, String logId1, String logId2, String logType, String logJson, String remark, String regId) {
        this.tableName = tableName;
        this.logId1 = logId1;
        this.logId2 = logId2;
        this.logType = logType;
        this.logJson = logJson;
        this.remark = remark;
        this.regId = regId;
    }

}
