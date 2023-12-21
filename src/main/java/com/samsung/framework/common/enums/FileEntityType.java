package com.samsung.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum FileEntityType {


    BOARD("BOARD",new String[] {"01", "02", "03"}, EntityTypeDir.BOARD_DIR); // CODE "003001" , 01 : 기본 , 02: 그 외

    private String targetTable;
    private String[] entityType;
    private String entityDir;


    private static class EntityTypeDir{
        // board
        private static final String BOARD_DIR = "/board";

    }

}
