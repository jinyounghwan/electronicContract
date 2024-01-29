package com.samsung.framework.domain.account.ghr;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GhrAccount {
    int empNo;
    String name;
    String team;
    String group;
    String part;
    String subPart;
    String orgLevelCode;
    String departmentCode;
    String department;
    String levelCode;
    String level;
    String jBand;
    String job;
    String emplType;
    String sEmplType;
    String duty;
    String costCenter;
    String costCenterName;
    LocalDateTime hireDate;
    int companyService;
    String probationer;
    LocalDateTime probationEndDate;
    LocalDateTime resignationDate;
    String shift;
    String nationality;
    String shiftName;
    String birthPlace;
}
