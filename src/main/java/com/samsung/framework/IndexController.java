package com.samsung.framework;

import com.samsung.framework.common.enums.LogTypeEnum;
import com.samsung.framework.common.utils.LogUtil;
import com.samsung.framework.domain.log.LogSaveRequest;
import com.samsung.framework.vo.log.LogSaveResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/index")
@RestController
public class IndexController {

    private static final String content = "%s";
    private final AtomicInteger counter = new AtomicInteger();

    private final LogUtil logUtil;

    @GetMapping({"", "/"})
    public ResponseEntity index() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("test", "value");

        return ResponseEntity.ok().headers(headers).body(null);
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

    @GetMapping("/saveLog")
    public ResponseEntity saveLog(HttpServletRequest request) {
        var logSaveRequest = LogSaveRequest.builder()
                .logType(LogTypeEnum.LOGIN)
                .ipAddress(request.getRemoteAddr() + ":" + request.getRemotePort())
                .createdBy("ikjoo Lee")
                .build();

        Map<String, LogSaveResponse> resultMap = logUtil.saveLog(logSaveRequest);

        return ResponseEntity.ok().body(resultMap);
    }
}



