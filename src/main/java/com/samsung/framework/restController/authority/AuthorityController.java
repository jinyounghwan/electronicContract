/*
package com.samsung.framework.restController.authority;

import com.samsung.framework.restController.common.ParentController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

*/
/**
 * 권한 체크 컨트롤러 (테스트용)
 *//*

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthorityController extends ParentController {

    @PostMapping(value = {"", "/"})
    public ResponseEntity selectAuth(Authentication authentication) {
        log.info("selectAuth: {}", authentication.toString());
        return ResponseEntity.ok(authentication);
    }

    @PostMapping("save")
    public ResponseEntity saveAuth(Authentication authentication) {
        log.info("createAuth: {}", authentication.toString());
        return ResponseEntity.ok(authentication);
    }

    @PostMapping("update")
    public ResponseEntity updateAuth(Authentication authentication) {
        log.info("updateAuth: {}", authentication.toString());
        return ResponseEntity.ok(authentication);
    }

    @PostMapping("delete")
    public ResponseEntity deleteAuth(Authentication authentication) {
        log.info("deleteAuth: {}", authentication.toString());
        return ResponseEntity.ok(authentication);
    }

    @PostMapping("download")
    public ResponseEntity downloadAuth(Authentication authentication) {
        log.info("downloadAuth: {}", authentication.toString());
        return ResponseEntity.ok(authentication);
    }
}
*/
