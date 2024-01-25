package com.samsung.framework.vo.account.ghr;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GhtAccountVO {
    // 사번
    int empNo;
    // 이름
    String name;
    // 팀
    String team;
    // 그룹
    String group;
    // 파트
    String part;
    // 하위 파트
    String subPart;
    // 조직 레벨 코드
    String orgLevelCode;
    // 부서 코드
    String departmentCode;
    // 부서 명
    String department;
    // 레벨 코드
    String levelCode;
    // 레벨
    String level;
    // 페이밴드
    String payBand;
    String jBand;
    // 업무
    String job;
    // 임직원 유형
    String emplType;
    // 임직원 고용 상태
    String sEmplType;
    // 직무
    String duty;
    // 코스트 센터 코드
    String costCenter;
    // 코스트 센터 명
    String costCenterName;
    // 채용일
    LocalDateTime hireDate;
    // 근무 기간(월)
    int companyService;
    // 수습 직원 여부
    String probationer;
    // 수습 종료일
    LocalDateTime probationEndDate;
    // 퇴사일
    LocalDateTime resignationDate;
    // 교대 근무 코드
    String shift;
    // 국적
    String nationality;
    // 교대 근무 명
    String shiftName;
    // 출생지
    String birthPlace;
}
