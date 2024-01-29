package com.samsung.framework.service.board;

import com.samsung.framework.common.enums.RequestTypeEnum;
import com.samsung.framework.common.enums.TableNameEnum;
import com.samsung.framework.common.utils.ObjectHandlingUtil;
import com.samsung.framework.common.utils.StringUtil;
import com.samsung.framework.domain.board.Board;
import com.samsung.framework.domain.common.Paging;
import com.samsung.framework.service.file.FileService;
import com.samsung.framework.vo.account.AccountVO;
import com.samsung.framework.mapper.board.BoardMapper;
import com.samsung.framework.vo.board.BoardPublicVO;
import com.samsung.framework.vo.board.BoardVO;
import com.samsung.framework.vo.file.FilePublicVO;
import com.samsung.framework.vo.file.FileVO;
import com.samsung.framework.vo.search.board.BoardSearchVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {

    private final FileService fileService;

    private final BoardMapper boardMapper;

    /**
     * 게시판 저장
     * @param board
     * @return
     * @throws Exception
     */
    public int insertBoard(Board board, List<FilePublicVO> files, AccountVO account) throws Exception{

        Board target = Board.builder()
                            .boardCode(board.getBoardCode())
                            .title(board.getTitle())
                            .contents(board.getContents())
                            .fixYn(board.getFixYn())
                            .displayYn(board.getDisplayYn())
                            .useYn(board.getUseYn())
                            .tableName(TableNameEnum.BOARD.name())
                            .logId1("")
                            .logType(RequestTypeEnum.CREATE.getRequestType())
                            .logId2(null)
                            .logJson(null)
                            .remark(null)
                            .regId(account.getUserId())
                            .build();

        int iAffectedRows = boardMapper.insert(target);


        //파일 저장
        if(StringUtil.isNotEmpty(files)) {
//            fileService.saveFile(files);
        }

        return iAffectedRows;
    }

    /**
     * 게시판 페이징 조회
     * @param search
     * @return
     */
    public List<BoardVO> pagingBoard(BoardSearchVO search){
        search.setEntityName(TableNameEnum.BOARD.name());

        int totalCount = boardMapper.pagingCountBySearch(search);
        Paging paging = ObjectHandlingUtil.pagingOperatorBySearch(search, totalCount);
        search.setPaging(paging);
        List<BoardVO> boardList = (List<BoardVO>) boardMapper.pagingBySearch(search);
        return boardList;
    }

    /**
     * 게시판 단 건 조회
     * @param search
     * @return
     */
    public BoardVO rowBoard(BoardSearchVO search){
        search.setEntityName(TableNameEnum.BOARD.name());

        BoardVO board = (BoardVO) boardMapper.rowBySearch(search);

        // 게시판에 물린 파일 목록 가져오기
        board.setFiles(boardFileSearch(search));

        return board;
    }

    @Override
    public BoardPublicVO findById(BoardSearchVO search) {
        return null;
    }

    @Override
    public List<BoardPublicVO> findAll(BoardSearchVO boardSearchVO) {
        return null;
    }

    @Override
    public void increaseHits(long boardSeq) {

    }

    /**
     *  단 건 게시판 파일 조회
     * @param search
     * @return
     */
    public List<FileVO> boardFileSearch(BoardSearchVO search){
        return  boardMapper.boardFileSearch(search);
    }

    /**
     * 게시판 수정
     * @param board
     * @return
     */
    public int updateBoard(Board board, List<FilePublicVO> fileList, AccountVO account) throws Exception {
        Board target = Board.builder()
                            .boardSeq(board.getBoardSeq())
                            .title(board.getTitle())
                            .contents(board.getContents())
                            .fixYn(board.getFixYn())
                            .displayYn(board.getDisplayYn())
                            .useYn(board.getUseYn())
                            .tableName(TableNameEnum.BOARD.name())
                            .logId1(String.valueOf(board.getBoardSeq()))
                            .logType(RequestTypeEnum.UPDATE.getRequestType())
                            .logId2(null)
                            .logJson(null)
                            .remark(null)
                            .regId(account.getUserId())
                            .build();

        // Mapper Update
        int iAffectedRows = boardMapper.update(target);


        return iAffectedRows;
    }

}
