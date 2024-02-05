package com.samsung.framework;

import com.samsung.framework.common.enums.LogTypeEnum;
import com.samsung.framework.common.utils.EncryptionUtil;
import com.samsung.framework.common.utils.LogUtil;
import com.samsung.framework.common.utils.VariableHandlingUtil;
import com.samsung.framework.domain.common.Variables;
import com.samsung.framework.domain.log.LogSaveRequest;
import com.samsung.framework.vo.account.AccountVO;
import com.samsung.framework.vo.log.LogSaveResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class IndexController {

    private final LogUtil logUtil;
    private final VariableHandlingUtil variableHandlingUtil;
    private final EncryptionUtil encryptionUtil;

    /**
     * Index Page Handler
     * @param request
     * @return
     */
    @GetMapping({"", "/"})
    public String index(HttpServletRequest request) {
        HttpSession session = request.getSession();
        AccountVO account = (AccountVO) session.getAttribute("loginInfo");
        if (account == null) {return "redirect:/account/login";}

        return "redirect:/contract/progress";
    }

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * TEST CODE LIVES BEYOND HERE
 * *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  */
    /**
     * 로그 저장 Test
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
     * 리다이렉션 Test
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

    /**
     * Contract Variable Replcement Test
     * @return
     */
    @PostMapping("/contract-variable-replacement-test")
    public ResponseEntity variableReplacement() {

        Variables replacementTarget = Variables.builder()
                .name("Ikjoo")
                .employeeNo(String.valueOf(12345678))
                .hireDateEn("01/03/2024")
                .hireDateHu("2024.03.01.")
                .jobTitleEn("Supervisor_EN")
                .jobTitleHu("Supervisor_HU")
                .salaryNo("458,900")
                .salaryEn("four hundred fifty eight thousand nine hundred")
                .wageTypeEn("month_EN")
                .wageTypeHu("hó")
                .build();

        String text = """
                the SAMSUNG SDI Magyarország Zrt. (registered seat: 2131 Göd Schenek István u. 1.;), as employer (hereinafter: „Employer”) and
                {{name}} (employee No.: {{employee_no}}), as employee (hereinafter „ Employee”)
                (hereinafter jointly referred to as „Contracting parties”) at the place and on the date below under the following conditions:
                                
                                
                1. Contracting parties modify by mutual consent the employment contract concluded on {{hire_date_en}} made by and between them (hereinafter: “Employment contract”) with effect from {{hire_date_en}} under the following:
                In addition:
                                
                a) The first sentence of Section 4. of the Employment contract is replaced by the following:
                The Employer and the Employee agree on that the job function of the Employee is: {{job_title_en}}.
                b) The first sentence of Section 6.1. of the Employment contract is replaced by the following:
                “6.1. The Employer and the Employee agree that the gross monthly base wage of the Employee is {{salary_no}} HUF/{{wage_type_en}}, i.e. [(확정 전)계약서 생성 시 입력_eng] {{salary_en}} Hungarian HUF/{{wage_type_hu}}.”
                                
                                
                2. Provisions of the Employment contract not affected by this amendment remain unchanged in force. To questions not regulated in this amendment of employment contract the provisions of the Labor Code shall apply.
                                
                3. This amendment of employment contract has been made in two (2) original samples in Hungarian and English, from which the Employee has received one sample. In the event of any inconsistency the Hungarian version shall apply.
                """;

        String replaced = variableHandlingUtil.replaceVariables(text, replacementTarget);

        return ResponseEntity.ok(replaced);
    }


    /**
     * Encryption Test
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     * @throws InvalidKeySpecException
     */
    @GetMapping("/encryption")
    @ResponseBody
    public ResponseEntity test() throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeySpecException {
        String password = "qwer1234";
        return ResponseEntity.ok(encryptionUtil.encrypt(password));
    }
}



