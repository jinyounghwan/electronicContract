/*
package com.samsung.framework.restController.utils;

import com.samsung.framework.restController.common.ParentController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/cookie")
public class CookieController extends ParentController {

    */
/**
     * 쿠키 세팅
     * @param request
     * @param response
     * @return {@link ResponseEntity}
     *//*

    @GetMapping("/set-cookie")
    public ResponseEntity setCookie(HttpServletRequest request, HttpServletResponse response) {
        getCommonUtil().getCookieFactory().setCookie(response, "localhost", "/", "res-cookie1", "cookie-test1", true, false, 0);
        getCommonUtil().getCookieFactory().setCookie(response, "localhost", "/", "res-cookie2", "cookie-test2", true, false, 0);
        return ResponseEntity.ok().build();
    }

    */
/**
     * 쿠키 정보 조회
     * @param request
     * @param response
     * @return {@link ResponseEntity}
     *//*

    @GetMapping("/get-cookie")
    public ResponseEntity getCookie(HttpServletRequest request, HttpServletResponse response) {
        getCommonUtil().getCookieFactory().getCookieList(request);

        return ResponseEntity.ok().build();
    }

    */
/**
     * 쿠키 만료
     * @param request
     * @param response
     * @param key
     * @return {@link ResponseEntity}
     *//*

    @GetMapping("/remove/{key}")
    public ResponseEntity removeCookie(HttpServletRequest request, HttpServletResponse response, @PathVariable String key*/
/*, @CookieValue(value = "res-cookie2", required = false) String cookie*//*
) {
//        String key = Arrays.stream(request.getCookies()).filter(c -> c.getName().equals("res-cookie2")).findAny().get().getName();
        getCommonUtil().getCookieFactory().removeCookie(request, response, key);

        return ResponseEntity.ok().build();
    }
}
*/
