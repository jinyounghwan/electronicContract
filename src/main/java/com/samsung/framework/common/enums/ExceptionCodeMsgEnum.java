package com.samsung.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionCodeMsgEnum {

    // SERVER
      SERVER_ERROR(999, "SERVER ERROR")
    , SERVER_DATA_ERROR(998, "서버 데이터 에러")
    , NOT_EQUAL_OBJECT_SIZE(997, "비교 객체의 사이즈가 불일치 합니다.")
    , INVALID_NUMBER_FORMAT(996, "잘못된 숫자 형식 입니다.")
    , SQL_ERROR(995, "SQL 에러")
    , RUNTIME_ERROR(994, "RUNTIME 에러")
    , CLASS_NOT_FOUND(993, "클래스를 찾을 수 없습니다.")
    , INVALID_METHOD_NAMING(992, "유효하지 않은 메서드 명 입니다.")

    // REQUEST
    , NULL_POINTER(991, "NULL POINTER")
    , INVALID_ARGUMENT_EXISTS(990, "유효하지 않은 인자 값이 존재합니다.")
    , INVALID_PARAMETER_BINDING(989, "유효하지 않은 매개변수 값 입니다.")
    , UNSUPPORTED_ENCODING(988, "지원하지 않는 인코딩 방식 입니다.")
    , JSON_ERROR(987, "JSON Error")

    // ACCOUNT
    , ACCOUNT_DUPLICATE(987, "이미 있는 회원 아이디 입니다.")
    , LOGIN_REQUIRED(986, "로그인이 필요한 API 입니다.")
    , PASSWORD_VALUE_NOT_PROVIDED(985, "비밀번호 값이 없습니다.")
    , INVALID_PASSWORD(984, "비밀번호가 맞지 않습니다.")
    , ACCOUNT_NOT_EXISTS(983, "없는 계정 정보 입니다.")

    // TOKEN
    , EXPIRED_TOKEN(982, "만료된 토큰 값 입니다.")
    , NO_TOKEN(981, "토큰 값이 없습니다.")
    , INVALID_TOKEN(980, "유효하지 않은 토큰 입니다.")
    , INVALID_TOKEN_SIGNATURE(979, "유효하지 않은 토큰 서명 정보 입니다.")
    , MALFORMED_TOKEN(978, "정상적인 토큰 값이 아닙니다.")

    // AUTHENTICATION
    , NO_AUTHORITY(977, "접근 권한이 없습니다.")
    , INVALID_AUTH(976, "유효하지 않은 인증 정보 입니다.")
    , NO_MENU_AUTHORITY(975, "메뉴 접근 권한이 없습니다.")

    // FILE
    , INVALID_FILE(974, "유효하지 않은 파일 형식입니다.")
    ;

    private int code;
    private String msg;
}
