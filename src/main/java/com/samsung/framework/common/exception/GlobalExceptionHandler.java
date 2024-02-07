package com.samsung.framework.common.exception;

import com.fasterxml.jackson.core.JacksonException;
import com.samsung.framework.common.enums.ExceptionCodeMsgEnum;
import com.samsung.framework.vo.common.ResultStatusVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerErrorException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 전역 Exception<br/>
 * 1. 애플리케이션 실행 중 예외가 발생하는 경우에 해당 exception 클래스 호출
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private ResultStatusVO resultStatusVO = new ResultStatusVO();

    /**
     * @param ex
     * @return ResponseEntity
     * @implNote @RequestBody, @RequestPart Exception handling
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ResultStatusVO> handleMethodArgumentNotValidException(Model model , MethodArgumentNotValidException ex) {
        this.printBindingErrorLog(ex);
        resultStatusVO = new ResultStatusVO(ExceptionCodeMsgEnum.INVALID_ARGUMENT_EXISTS.getCode(), ExceptionCodeMsgEnum.INVALID_ARGUMENT_EXISTS.getMsg(), null, this.gatherBindingErrors(ex));
        model.addAttribute("status" , resultStatusVO);
        return ResponseEntity.badRequest().body(resultStatusVO);
    }

    /**
     * @param ex
     * @return ResponseEntity
     * @implNote @ModelAttribute Exception handling
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ResultStatusVO> handleBindException(Model model , BindException ex) {
        this.printBindingErrorLog(ex);
        resultStatusVO = new ResultStatusVO(ExceptionCodeMsgEnum.INVALID_ARGUMENT_EXISTS.getCode(), ExceptionCodeMsgEnum.INVALID_ARGUMENT_EXISTS.getMsg(), null, this.gatherBindingErrors(ex));
        model.addAttribute("status" , resultStatusVO);
        return ResponseEntity.internalServerError().body(resultStatusVO);
    }

    /**
     *
     * @param ex
     * @return ResponseEntity
     * @implNote Argument error handling
     */
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ResultStatusVO> handleIllegalArgumentException( Model model ,IllegalArgumentException ex) {
        this.printRuntimeErrorLog(ex);
        resultStatusVO = new ResultStatusVO(ExceptionCodeMsgEnum.INVALID_ARGUMENT_EXISTS.getCode(), ExceptionCodeMsgEnum.INVALID_ARGUMENT_EXISTS.getMsg(), ex.getMessage(), null);
        model.addAttribute("status" , resultStatusVO);
        return ResponseEntity.badRequest().body(resultStatusVO);
    }

    /**
     *
     * @param ex
     * @return ResponseEntity
     * @implNote NumberFormat error handling
     */
    @ExceptionHandler(NumberFormatException.class)
    protected ResponseEntity<ResultStatusVO> handleNumberFormatException(Model model , NumberFormatException ex) {
        this.printRuntimeErrorLog(ex);
        resultStatusVO = new ResultStatusVO(ExceptionCodeMsgEnum.INVALID_NUMBER_FORMAT.getCode(), ExceptionCodeMsgEnum.INVALID_NUMBER_FORMAT.getMsg(), ex.getMessage(), null);
        model.addAttribute("status" , resultStatusVO);
        return ResponseEntity.badRequest().body(resultStatusVO);
    }

    /**
     *
     * @param ex
     * @return ResponseEntity
     * @implNote Null pointer error handling
     */
//    @ExceptionHandler(NullPointerException.class)
//    protected String handleNullPointerException(NullPointerException ex) {
//        this.printRuntimeErrorLog(ex);
//        resultStatusVO = new ResultStatusVO(ExceptionCodeMsgEnum.NULL_POINTER.getCode(), ExceptionCodeMsgEnum.NULL_POINTER.getMsg(), ex.getMessage(), null);
//        return "error/error";
//    }
    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<ResultStatusVO> handleNullPointerException(NullPointerException ex , Model model) {
        this.printRuntimeErrorLog(ex);
        resultStatusVO = new ResultStatusVO(ExceptionCodeMsgEnum.NULL_POINTER.getCode(), ExceptionCodeMsgEnum.NULL_POINTER.getMsg(), ex.getMessage(), null);
        model.addAttribute("status" , resultStatusVO);
        return ResponseEntity.internalServerError().body(resultStatusVO);
    }



    /**
     *
     * @param ex
     * @return ResponseEntity
     * @implNote Class not found error handling
     */
    @ExceptionHandler(ClassNotFoundException.class)
    protected ResponseEntity<ResultStatusVO> handleClassNotFoundException(Model model ,ClassNotFoundException ex) {
        log.error("[handleClassNotFoundException] => {}", ex.getMessage());
        resultStatusVO = new ResultStatusVO(ExceptionCodeMsgEnum.CLASS_NOT_FOUND.getCode(), ExceptionCodeMsgEnum.CLASS_NOT_FOUND.getMsg(), ex.getMessage(), null);
        model.addAttribute("status" , resultStatusVO);
        return ResponseEntity.internalServerError().body(resultStatusVO);
    }

    /**
     * @param ex
     * @return ResponseEntity
     * @implNote HttpMethod error handling
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ResultStatusVO> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.error("[handleHttpRequestMethodNotSupportedException] => {}", ex.getMessage());
        resultStatusVO = new ResultStatusVO(ex.getBody().getStatus(), ex.getBody().getDetail(), ex.getMessage(), null);
        return ResponseEntity.badRequest().body(resultStatusVO);
    }

    /**
     * @param ex
     * @return ResponseEntity
     * @implNote SQL error handling
     */
    @ExceptionHandler(SQLException.class)
    protected ResponseEntity handleSQLException(Model model , SQLException ex ) {
        this.printSqlErrorLog(ex);
        resultStatusVO = new ResultStatusVO(ExceptionCodeMsgEnum.SQL_ERROR.getCode(), ExceptionCodeMsgEnum.SQL_ERROR.getMsg(), ex.getMessage(), null);
        model.addAttribute("status" , resultStatusVO);
        return ResponseEntity.internalServerError().body(resultStatusVO);
    }

    /**
     * @param ex
     * @return ResponseEntity
     * @implNote Encoding error handling
     */
    @ExceptionHandler(UnsupportedEncodingException.class)
    protected ResponseEntity<ResultStatusVO> handleUnsupportedEncodingException(Model model , UnsupportedEncodingException ex) {
        this.printIOExceptionLog(ex);
        resultStatusVO = new ResultStatusVO(ExceptionCodeMsgEnum.UNSUPPORTED_ENCODING.getCode(), ExceptionCodeMsgEnum.UNSUPPORTED_ENCODING.getMsg(), ex.getMessage(), null);
        model.addAttribute("status" , resultStatusVO);
        return ResponseEntity.badRequest().body(resultStatusVO);
    }

    /**
     * @param ex
     * @return ResponseEntity
     * @implNote Login related handling
     */
    @ExceptionHandler(CustomLoginException.class)
    protected ResponseEntity<ResultStatusVO> handleCustomLoginException(CustomLoginException ex) {
        this.printGeneralSecurityExceptionLog(ex);
        resultStatusVO = new ResultStatusVO(ex.getCode(), ex.getMsg(), ex.getMessage(), null);
        if(ex.getCode() == 979) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultStatusVO);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultStatusVO);
    }

    /**
     * @param ex
     * @return ResponseEntity
     * @implNote Authority error handling
     */
//    @ExceptionHandler(AccessDeniedException.class)
//    protected String handleAccessDeniedException(AccessDeniedException ex) {
//        this.printRuntimeErrorLog(ex);
//        resultStatusVO = new ResultStatusVO(ExceptionCodeMsgEnum.NO_AUTHORITY.getCode(), ExceptionCodeMsgEnum.NO_AUTHORITY.getMsg(), ex.getMessage(), null);
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultStatusVO);
//    }

    /**
     * @param ex
     * @return ResponseEntity
     * @implNote json error handling
     */
    @ExceptionHandler(JacksonException.class)
    protected ResponseEntity<ResultStatusVO> handleJacksonException(JacksonException ex) {
        this.printJacksonException(ex);
        resultStatusVO = new ResultStatusVO(ExceptionCodeMsgEnum.JSON_ERROR.getCode(), ExceptionCodeMsgEnum.JSON_ERROR.getMsg(), ex.getMessage(), null);
        return ResponseEntity.badRequest().body(resultStatusVO);
    }

    /**
     * @param ex
     * @return ResponseEntity
     * @implNote Server error handling
     */
    @ExceptionHandler(ServerErrorException.class)
    protected ResponseEntity<ResultStatusVO> handleServerErrorException(ServerErrorException ex) {
        this.printRuntimeErrorLog(ex);
        resultStatusVO = new ResultStatusVO(ExceptionCodeMsgEnum.SERVER_ERROR.getCode(), ExceptionCodeMsgEnum.SERVER_ERROR.getMsg(), ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultStatusVO);
    }

    /**
     * @param ex
     * @return ResponseEntity
     * @implNote File error handling
     */
    @ExceptionHandler(CustomFileException.class)
    protected ResponseEntity<ResultStatusVO> handleCustomFileException(CustomFileException ex) {
        this.printIOExceptionLog(ex);
        resultStatusVO = new ResultStatusVO(ex.getCode(), ex.getMessage(), ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultStatusVO);
    }

    // ----------------------------------------------------------------------------------------------------
    private <T extends BindException> List<FieldError> gatherBindingErrors(T ex) {
        List<FieldError> errList = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(c -> errList.add((FieldError) c));
        return errList;
    }

    private <T extends BindException> void printBindingErrorLog(T ex) {
        log.error("[printBindingErrorLog] => {}", ex.getBindingResult().getAllErrors());
        log.error("GlobalErrorCount => {}", ex.getBindingResult().getGlobalErrorCount());
        log.error("ErrorCount => {}", ex.getBindingResult().getErrorCount());
        log.error("FieldErrorCount => {}", ex.getBindingResult().getFieldErrorCount());
        log.error("{}", ((MethodArgumentNotValidException) ex).getBody());
    }

    private <T extends RuntimeException> void printRuntimeErrorLog(T ex) {
        log.error("[printRuntimeErrorLog] ", ex);
    }

    private <T extends SQLException> void printSqlErrorLog(T ex) {
        log.error("[printSqlErrorLog] ", ex);
    }

    private <T extends IOException> void printIOExceptionLog(T ex) {
        log.error("[printIOExceptionLog] ", ex);
    }

    private <T extends GeneralSecurityException> void printGeneralSecurityExceptionLog(T ex) {
        log.error("[printGeneralSecurityExceptionLog] ", ex);
    }

    private <T extends JacksonException> void printJacksonException(T ex) {
        log.error("[printJacksonException] ", ex);
    }

//    private <T extends AuthenticationException> void printAuthenticationException(T ex) {
//        log.error("[printAuthenticationExceptionLog] ", ex);
//    }
}
