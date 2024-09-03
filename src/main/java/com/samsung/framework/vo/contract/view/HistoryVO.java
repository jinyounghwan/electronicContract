package com.samsung.framework.vo.contract.view;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class HistoryVO {
    private String logType;
    private String processStep;
    private String ipAddress;
    private String macAddress;
    private String createdBy;
    private String createdByName;
    private String createdAt;
    private String firstName;
    private String lastName;
}
