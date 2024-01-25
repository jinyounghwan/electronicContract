package com.samsung.framework.controller.contract.documented;

import com.samsung.framework.service.account.ghr.GhrAccountService;
import com.samsung.framework.service.contract.documented.ContractCompletionService;
import com.samsung.framework.vo.account.AccountVO;
import com.samsung.framework.vo.contract.ContractPaperVO;
import com.samsung.framework.vo.search.SearchVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;

/**
 * 계약서 완료 Controller
 */
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/contract/sign/comp")
public class ContractCompletionController {
    private final ContractCompletionService contractCompletionService;
    private final GhrAccountService ghrAccountService;

    // [검색옵션] 날짜
    @ModelAttribute("dateRangeSelect")
    public List<SearchVO> searchDateRangeOptionList() {
        SearchVO list = new SearchVO();
        return list.getSearchDateRangeOptionList();
    }

    // [검색옵션] 키워드
    @ModelAttribute("keywordTypeSelect")
    public List<SearchVO> searchKeywordTypeOptionList() {
        SearchVO list = new SearchVO();
        return list.getAcctSearchKeywordTypeOptionList();
    }

    @ModelAttribute("templateTypeSelect")
    public List<SearchVO> templateTypeOptionList(){
        SearchVO list = new SearchVO();
        return list.getTemplateSearchKeywordTypeOptionList();
    }

    @GetMapping({"","/"})
    public ModelAndView getPaperContractList(ModelAndView mv){
        mv.setViewName("contract/completion/paperCompletionUpload");
        return mv;
    }

    @PostMapping("/list")
    public ResponseEntity getPaperContractList(){
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/paper")
    public ModelAndView getPaperContractUpload(ModelAndView mv) {
        mv.setViewName("contract/completion/paperCompletionUpload");
        return mv;
    }

    @ResponseBody
    @PostMapping("/api/registration")
    public ResponseEntity createPaperContract(HttpServletRequest request, @RequestPart(value="data", required = true) ContractPaperVO contract , @RequestPart(value="file", required = true)List<MultipartFile> file) throws Exception {
        HttpSession session = request.getSession();
        AccountVO account = (AccountVO) session.getAttribute("loginInfo");

        contractCompletionService.paperContractSave(contract, account, file);


        return new ResponseEntity<>(HttpStatus.OK);
    }


}
