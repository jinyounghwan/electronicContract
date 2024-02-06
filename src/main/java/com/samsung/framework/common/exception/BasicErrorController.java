package com.samsung.framework.common.exception;

import com.samsung.framework.vo.common.ResultStatusVO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BasicErrorController implements ErrorController {
    @Value("${server.error.path:${error.path:/error}}")
    private String errorPath;

    @RequestMapping("${server.error.path:${error.path:/error}}")
    public String handleError(HttpServletRequest request , Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            ResultStatusVO result = new ResultStatusVO(statusCode , status.toString());
            model.addAttribute("status", result);
        }
        return errorPath +"/error";
    }

    @GetMapping("${server.error.path:${error.path:/common}}")
    public String displayCommonErrorPage(Model model, @RequestParam("message") String message) {
        model.addAttribute("status", new ResultStatusVO(message));
        return "error/error";
    }
}
