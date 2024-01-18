package com.samsung.framework.mapper.common;


import com.samsung.framework.mapper.authority.AuthorityMapper;
import com.samsung.framework.mapper.board.ApproveMapper;
import com.samsung.framework.mapper.board.BoardMapper;
import com.samsung.framework.mapper.code.CodeMapper;
import com.samsung.framework.mapper.file.FileMapper;
import com.samsung.framework.mapper.mail.MailMapper;
import com.samsung.framework.mapper.menu.MenuMapper;
import com.samsung.framework.mapper.record.RecordMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * mapper 공통으로 사용
 */
@RequiredArgsConstructor
@Component
@Getter
public class CommonMapper {

    private final CodeMapper codeMapper;

    private final BoardMapper boardMapper;

    private final MenuMapper menuMapper;

    private final FileMapper fileMapper;

    private final MailMapper mailMapper;

    private final RecordMapper recordMapper;

    private final AuthorityMapper authorityMapper;

    private final ApproveMapper approveMapper;
}