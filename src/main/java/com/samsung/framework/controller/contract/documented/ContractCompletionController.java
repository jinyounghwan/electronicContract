package com.samsung.framework.controller.contract.documented;

import com.samsung.framework.common.enums.ContractProcessEnum;
import com.samsung.framework.common.enums.LogTypeEnum;
import com.samsung.framework.common.utils.LogUtil;
import com.samsung.framework.domain.common.Paging;
import com.samsung.framework.domain.contract.paper.ContractComp;
import com.samsung.framework.domain.log.LogSaveRequest;
import com.samsung.framework.mapper.contract.template.ContractTemplateMapper;
import com.samsung.framework.service.account.ghr.GhrAccountService;
import com.samsung.framework.service.contract.documented.ContractCompService;
import com.samsung.framework.vo.account.AccountVO;
import com.samsung.framework.vo.common.ResultStatusVO;
import com.samsung.framework.vo.contract.completion.ContractCompVO;
import com.samsung.framework.vo.contract.template.Template;
import com.samsung.framework.vo.file.FilePublicVO;
import com.samsung.framework.vo.log.LogSaveResponse;
import com.samsung.framework.vo.search.SearchVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 계약서 완료 Controller
 */
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/contract/sign/comp")
public class ContractCompletionController {
    private final ContractCompService contractCompletionService;
    private final GhrAccountService ghrAccountService;

    /**
     * Search date range option list list.
     * [검색옵션] 날짜
     * @return the list
     */
    @ModelAttribute("dateRangeSelect")
    public List<SearchVO> searchDateRangeOptionList() {
        return new SearchVO().getSearchDateRangeOptionList();
    }

    /**
     * Search keyword type option list list.
     * [검색옵션] 키워드
     * @return the list
     */
    @ModelAttribute("keywordTypeSelect")
    public List<SearchVO> searchKeywordTypeOptionList() {
        return new SearchVO().getSearchKeywordTypeOptionList();
    }

    /**
     * Search keyword type option  list.
     * [Doc.status] 키워드
     * @return the list
     */
    @ModelAttribute("contractDocSearchStateTypeSelect")
    public List<SearchVO>  searchContractDocSearchStateTypeSelect() {
        return new SearchVO().getContractDocSearchStateTypeList().stream()
                .filter(state -> state.getCode().equals(ContractProcessEnum.processCode(ContractProcessEnum.COMPLETED)) ||
                        state.getCode().equals(ContractProcessEnum.processCode(ContractProcessEnum.PAPER_CONTRACT)) ||
                        state.getCode().equals("ALL")
                )
                .collect(Collectors.toList());
    }

    /**
     * Search keyword type option list list.
     * [contract status] 키워드
     * @return the list
     */
    @ModelAttribute("contractSearchStateTypeSelect")
    public List<SearchVO>  searchContractSearchStateTypeList() {
        return new SearchVO().getContractDocSearchStateTypeList();
    }

    @ModelAttribute("templateTypeSelect")
    public List<Template> templateTypeOptionList(){
        return contractCompletionService.getTemplateCode();
    }

    @GetMapping({"","/"})
    public String getCompletionContractList(Model model){
        model.addAttribute("paging" , new Paging());
        model.addAttribute("list" , new ArrayList<>());
        model.addAttribute("search" , new SearchVO());
        model.addAttribute("totalCount" ,contractCompletionService.getContractCompTotal(new SearchVO()));
        return "contract/completion/completionList";
    }

    @PostMapping("/list")
    public String getContractCompList(Model model, @RequestBody SearchVO searchVO){
        log.info("완료 계약서 vo >> " + searchVO);
        // total
        int totalCount = contractCompletionService.getContractCompTotal(searchVO);

        Paging pagingVO =  Paging.builder()
                .currentPage(searchVO.getPaging().getCurrentPage())
                .displayRow(searchVO.getPaging().getDisplayRow())
                .totalCount(totalCount)
                .build();
        searchVO.setPaging(pagingVO);

        model.addAttribute("totalCount", totalCount);
        // paging
        model.addAttribute("paging", pagingVO);

        // list
        List<ContractCompVO> list = contractCompletionService.getContractCompList(searchVO);
        model.addAttribute("list",list);
        model.addAttribute("search", searchVO);

        return "contract/completion/completionList :: #content-wrapper";
    }

    @GetMapping("/paper")
    public ModelAndView getPaperContractUpload(ModelAndView mv) {
        mv.setViewName("contract/completion/paperCompletionUpload");
        return mv;
    }

    @ResponseBody
    @PostMapping("/api/paper/registration")
    public ResponseEntity createPaperContract(HttpServletRequest request, @RequestPart(value="data", required = true) ContractCompVO contract , @RequestPart(value="file", required = true)List<MultipartFile> file) throws Exception {
        HttpSession session = request.getSession();
        AccountVO account = (AccountVO) session.getAttribute("loginInfo");
        ResultStatusVO resultStatusVO = contractCompletionService.paperContractSave(request, contract, account, file);

        return ResponseEntity.ok(resultStatusVO);
//        return new ResponseEntity<>(result, returnStatus);
    }

    @GetMapping("/detail/{contractNo}")
    public ModelAndView getContractCompDetail(ModelAndView mv, @PathVariable long contractNo){
        ContractCompVO contract = contractCompletionService.getContractCompDetail(contractNo);
        mv.setViewName("contract/completion/contractCompletion-detail");
        mv.addObject("info", contract);

        return mv;
    }

    @GetMapping("/view/{seq}")
    public ResponseEntity<String> viewPdf(@PathVariable String seq) throws IOException {

        FilePublicVO filePathSel = contractCompletionService.getFileSeq(seq);

        //String filePath = "C:/files/electronicContract/upload/Contract/PDF/2405/2405101d0a065adbeb497ea2621fa49959b54e.pdf"; // Replace with your actual file path
        String filePath = filePathSel.getStoragePath(); // file_integration > File Pat

        log.info("filePath select >" + filePath);

        byte[] pdfBytes = Files.readAllBytes(Paths.get(filePath));
        String base64EncodedPdf = Base64.getEncoder().encodeToString(pdfBytes);
        log.info("base !! > " + base64EncodedPdf);

        return ResponseEntity.ok(base64EncodedPdf);
    }

}
