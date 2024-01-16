package com.samsung.framework.domain.common;

import com.samsung.framework.domain.account.Account;
import com.samsung.framework.domain.board.Board;
import com.samsung.framework.domain.code.Code;
import com.samsung.framework.domain.file.File;
import com.samsung.framework.domain.menu.Menu;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 모델 공통 관리
 */
@Getter
@Setter
public class DataObject {

    private Data data;

    @Getter
    @Setter
    public static class Data {
        private Code code;

        private List<Code> codeList;

        private Account account;

        private Board board;

        private List<Board> boardList;

        private Menu menu;

        private List<Menu> menuList;

        private File file;

        private List<File> fileList;

    }
}
