package com.samsung.framework.service.common;

import com.samsung.framework.service.authority.AuthorityService;
import com.samsung.framework.service.board.ApproveService;
import com.samsung.framework.service.board.BoardPublicServiceImpl;
import com.samsung.framework.service.board.BoardService;
import com.samsung.framework.service.code.CodeService;
import com.samsung.framework.service.excel.ExcelPublicServiceImpl;
import com.samsung.framework.service.file.FilePublicServiceImpl;
import com.samsung.framework.service.mail.MailService;
import com.samsung.framework.service.member.MemberService;
import com.samsung.framework.service.menu.MenuService;
import com.samsung.framework.service.menu.MenuServiceImpl;
import com.samsung.framework.service.pdf.PdfServiceImpl;
import com.samsung.framework.service.record.RecordService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Service 공통으로 사용
 */
@RequiredArgsConstructor
@Component
@Getter
public class CommonService {

    private final CodeService codeService;
    private final MemberService memberService;
    private final AuthorityService authorityService;
    private final BoardService boardService;
    private final BoardPublicServiceImpl boardPublicServiceImpl;
    private final ExcelPublicServiceImpl excelPublicServiceImpl;
    private final MenuServiceImpl menuServiceImpl;
    private final FilePublicServiceImpl fileServiceImpl;
//  private final MailService mailService;
    private final RecordService recordService;
    private final ApproveService approveService;
    private final PdfServiceImpl pdfService;
}
