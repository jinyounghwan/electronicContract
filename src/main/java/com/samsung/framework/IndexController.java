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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URI;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/index")
@Controller
public class IndexController {

    private final LogUtil logUtil;

    @GetMapping({"", "/"})
    public String index() {
        return "sample/index";
    }

    /**
     * 로그 저장 테스트
     * @param request
     * @return
     */
    @ResponseBody
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



