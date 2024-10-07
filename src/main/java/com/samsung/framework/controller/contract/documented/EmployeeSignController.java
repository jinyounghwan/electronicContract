package com.samsung.framework.controller.contract.documented;

import com.samsung.framework.common.enums.ContractProcessEnum;
import com.samsung.framework.domain.common.Paging;
import com.samsung.framework.service.contract.documented.ContractProgressService;
import com.samsung.framework.service.contract.documented.EmployeeSignService;
import com.samsung.framework.vo.common.ResultStatusVO;
import com.samsung.framework.vo.contract.creation.ContractVO;
import com.samsung.framework.vo.file.FilePublicVO;
import com.samsung.framework.vo.search.SearchVO;
import com.samsung.framework.vo.search.account.AccountSearchVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("contract/sign")
@Controller
public class EmployeeSignController {
    private final EmployeeSignService signWaitService;
    private static final String SAVE_DIRECTORY = "D:\\signTest\\";
    private final ContractProgressService contractProgressService;

    @GetMapping(value="/wait")
    public String getContractProgress(Model model , HttpServletRequest request){
        model.addAttribute("paging" , new Paging());
        model.addAttribute("list" , new ArrayList<>());
        model.addAttribute("search" , new SearchVO());
        model.addAttribute("totalCount" ,signWaitService.getTotal(null , request));
        return "contract/signwait/list";
    }
    @PostMapping(value = "/wait/list")
    public String getContractProgressList (Model model , @RequestBody AccountSearchVO searchVO, HttpServletRequest request){
        Paging pagingVo =  Paging.builder()
                .currentPage(searchVO.getPaging().getCurrentPage())
                .displayRow(searchVO.getPaging().getDisplayRow())
                .totalCount(searchVO.getPaging().getTotalCount())
                .build();
        searchVO.setPaging(pagingVo);
        // total
        int totalCount = signWaitService.getTotal(searchVO , request);
        model.addAttribute("totalCount", totalCount);
        // paging
        model.addAttribute("paging",pagingVo);

        // list
        List<ContractVO> list = signWaitService.getContractWaitsList(searchVO , request);
        model.addAttribute("list",list);
        model.addAttribute("search" , searchVO);
        return "contract/signwait/list :: #content";
    }
    @GetMapping(value="/wait/info/{seq}")
    public String getContractWaitInfo(Model model ,@PathVariable String seq ,HttpServletRequest request){
        model.addAttribute("info",signWaitService.getContractWaitInfo(seq , request));
        return "contract/signwait/view";
    }

    @GetMapping("/wait/pdf/{seq}")
    public ResponseEntity<String> viewPdf(@PathVariable String seq) throws IOException {
        log.info("wait pdf in !! ");
        FilePublicVO filePathSel = contractProgressService.getFileSeq(seq);

        //String filePath = "C:/files/electronicContract/upload/Contract/PDF/2405/2405101d0a065adbeb497ea2621fa49959b54e.pdf"; // Replace with your actual file path
        String filePath = filePathSel.getStoragePath(); // file_integration > File Path

        log.info("filePath select >" + filePath);

        byte[] pdfBytes = Files.readAllBytes(Paths.get(filePath));
        String base64EncodedPdf = Base64.getEncoder().encodeToString(pdfBytes);
        log.info("base !! > " + base64EncodedPdf);

        return ResponseEntity.ok(base64EncodedPdf);
    }

    @PostMapping("/wait/saveSignature")
    public ResponseEntity<?> saveSignature(@RequestBody Map<String, String> requestData) throws IOException {
        String pdfBase64 = requestData.get("pdfBase64");
        String signatureBase64 = requestData.get("signatureBase64");
        String seq = requestData.get("seq");

        ContractVO contractVO = new ContractVO();
        // PDF와 서명 이미지를 바이트 배열로 변환
        byte[] pdfBytes = Base64.getDecoder().decode(pdfBase64);
        byte[] signatureBytes = Base64.getDecoder().decode(signatureBase64);

        // PDFBox 라이브러리를 사용하여 PDF에 서명 이미지를 삽입
        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfBytes))) {
            PDPage page = document.getPage(0); // 첫 번째 페이지에 서명 삽입
            PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, signatureBytes, "signature");

            PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true);
            contentStream.drawImage(pdImage, 200, 40, 250, 100); // 서명 이미지 삽입 (위치 및 크기 조정 가능)
            contentStream.close();

            // 서명된 PDF를 지정된 경로에 저장
            String outputFilePath = "C:/files/electronicContract/upload/Contract/PDFSIGN/" + seq + "_signed.pdf";
            document.save(outputFilePath);

            contractVO.setContractNo(Integer.parseInt(seq));
            contractVO.setSignFilePath(outputFilePath);

            signWaitService.updateSignPath(contractVO);
        }

        return ResponseEntity.ok("PDF 저장 성공");
    }

    @PostMapping(value="/wait/reject/update")
    @ResponseBody
    public ResponseEntity updateRejectProcessStatus(@RequestBody ContractVO contractVO , HttpServletRequest request){
        contractVO.setProcessStatus(ContractProcessEnum.processCode(ContractProcessEnum.REJECTED));
        ResultStatusVO resultStatusVO = signWaitService.updateProcessStatus(contractVO , request);
        return ResponseEntity.ok(resultStatusVO);
    }
    @PostMapping(value="/wait/complete/update")
    @ResponseBody
    public ResponseEntity updateCompleteProcessStatus(@RequestBody ContractVO contractVO , HttpServletRequest request){
        log.info("contractVO : " + contractVO);
        contractVO.setProcessStatus(ContractProcessEnum.processCode(ContractProcessEnum.SIGNED));
        ResultStatusVO resultStatusVO = signWaitService.updateProcessStatus(contractVO , request);
        return ResponseEntity.ok(resultStatusVO);
    }

    @PostMapping("/wait/info/contract/sign/wait/info/contract/sign/save-signature")
    public ResponseEntity<String> saveSignature(@RequestParam("imgBase64") String imgBase64 , ContractVO contractVO) {

        log.info("VO!! >> " + contractVO);
        // Remove the "data:image/png;base64," part
        String base64Image = imgBase64.split(",")[1];
        byte[] imageBytes = Base64Utils.decodeFromString(base64Image);

        // Generate a unique filename
        String fileName = UUID.randomUUID().toString() + ".png";
        File file = new File("D:\\SignDirectory\\" + fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(imageBytes);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.ok("Error saving signature");
        }

        // Sign file Image Update param
        String filePathSt = String.valueOf(file);
        contractVO.setContractNo(contractVO.getContractNo());
        contractVO.setSignFilePath(filePathSt);

        signWaitService.updateSignPath(contractVO);
        return ResponseEntity.ok("success");
    }

}
