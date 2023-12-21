package com.samsung.framework;

import com.samsung.framework.domain.Hello;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequestMapping("/index")
@RestController
public class IndexController {

    private static final String content = "%s";
    private final AtomicInteger counter = new AtomicInteger();

    @GetMapping({"", "/"})
    public ResponseEntity index() {
        var hello = new Hello(counter.incrementAndGet(), String.format(content, "Samsung Framework"));
        HttpHeaders headers = new HttpHeaders();
        headers.add("headertest", "headervalue");

        return ResponseEntity.ok().headers(headers).body(hello);
    }

    /**
     * 리다이렉션 샘플
     * @param request
     * @return
     */
    @GetMapping("/redirect")
    public ResponseEntity redirect(HttpServletRequest request) {
        log.info("REDIRECT TEST");
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/member"));
        return new ResponseEntity(headers, HttpStatus.MOVED_PERMANENTLY);
    }

}



