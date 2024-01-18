package com.samsung.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCodeMsgEnum {

      REQUEST_SUCCESS(200, "요청에 성공 했습니다.")
    , BAD_REQUEST(400, "잘못된 요청 입니다.")
    , REQUEST_FAIL(404, "요청한 페이지를 찾을 수 없습니다.")

    , NO_DATA(1, "조회된 데이터가 없습니다.")
    , CREATE_DATA_FAIL(7, "데이터 생성에 실패 했습니다.")
    , UPDATE_DATA_FAIL(8, "데이터 변경에 실패 했습니다.")
    , DELETE_DATA_FAIL(9, "데이터 삭제에 실패 했습니다.")
    , EXIST_CODE(10, "이미 존재하는 코드 입니다.")

    // 계정
    , INVALID_EMP_NO(1000, "유효하지 않은 임직원 계정 입니다.")

    // 계약서 템플릿
    , NO_TEMPLATE_CODE(2000, "없는 템플릿 코드 입니다.");

    private int code;
    private String msg;
}
