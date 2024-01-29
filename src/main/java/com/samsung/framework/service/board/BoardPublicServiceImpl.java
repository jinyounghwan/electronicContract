package com.samsung.framework.service.board;

import com.samsung.framework.common.exception.CustomFileException;
import com.samsung.framework.common.utils.DateUtil;
import com.samsung.framework.common.utils.FileUtil;
import com.samsung.framework.common.utils.StringUtil;
import com.samsung.framework.common.utils.ValidationUtil;
import com.samsung.framework.domain.board.Board;
import com.samsung.framework.domain.board.BoardPublic;
import com.samsung.framework.mapper.board.BoardMapper;
import com.samsung.framework.mapper.file.FileMapper;
import com.samsung.framework.service.file.FileService;
import com.samsung.framework.vo.account.AccountVO;
import com.samsung.framework.vo.board.BoardPublicVO;
import com.samsung.framework.vo.board.BoardReplyVO;
import com.samsung.framework.vo.board.BoardVO;
import com.samsung.framework.vo.common.SelectOptionVO;
import com.samsung.framework.vo.file.FilePublicVO;
import com.samsung.framework.vo.file.FileVO;
import com.samsung.framework.vo.search.board.BoardSearchVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Primary
@RequiredArgsConstructor
@Component
public class BoardPublicServiceImpl implements BoardService {

    FileService fileService;
    private final BoardMapper boardMapper;
    private final FileMapper fileMapper;
    private final ValidationUtil validationUtil;
    private final FileUtil fileUtil;
    @Override
    public int insertBoard(Board board, List<FilePublicVO> files, AccountVO member) throws Exception {
        return 0;
    }

    @Override
    public List<BoardVO> pagingBoard(BoardSearchVO search) {
        return null;
    }

    @Override
    public BoardVO rowBoard(BoardSearchVO search) {
        return null;
    }

    /**
     * 게시판 단 건 조회
     * @param search
     * @return rowData {@link BoardPublicVO}
     */
    @Override
    public BoardPublicVO findById(BoardSearchVO search) {
//        search.setEntityName(TableNameEnum.BOARD.name());

        BoardPublicVO rowData = boardMapper.findById(search);
        rowData.setRegDtStr(DateUtil.convertLocalDateTimeToString(rowData.getRegDt(), DateUtil.DATETIME_YMDHM_PATTERN));
        rowData.setLastDtStr(DateUtil.convertLocalDateTimeToString(rowData.getLastDt(), DateUtil.DATETIME_YMDHM_PATTERN));

        // 게시판 댓글 목록 조회
        if(rowData != null) {
            List<BoardReplyVO> replyBySeq = boardMapper.findReplyBySeq(rowData.getBoardSeq());
            if(!replyBySeq.isEmpty()) {
                replyBySeq.forEach(ele -> {
                    ele.setRegDtStr(DateUtil.convertLocalDateTimeToString(ele.getRegDt(), DateUtil.DATETIME_YMDHM_PATTERN));
                });
                rowData.setReplyList(replyBySeq);
            }

            // 게시판에 물린 파일 목록 가져오기
//        board.setFileList(boardFileSearch(search));
        }

        return rowData;
    }

    /**
     * 게시판 다 건 조회
     * @param boardSearchVO
     * @return list {@link List}
     */
    @Override
    public List<BoardPublicVO> findAll(BoardSearchVO boardSearchVO) {
        int totalCount = this.totalCount(boardSearchVO);
        if(totalCount < 1) {
            return new ArrayList<>();
        }

        List<BoardPublicVO> list = boardMapper.findAll(boardSearchVO);
        list.forEach(ele -> {
            ele.setRegDtStr(DateUtil.convertLocalDateTimeToString(ele.getRegDt(), DateUtil.DATETIME_YMDHM_PATTERN));
            ele.setLastDtStr(DateUtil.convertLocalDateTimeToString(ele.getLastDt(), DateUtil.DATETIME_YMDHM_PATTERN));
        });

        return list;
    }

    @Override
    public void increaseHits(long boardSeq) {
        int updateCount = boardMapper.increaseHits(boardSeq);
        System.out.println("updateCount " + updateCount);
    }

    @Override
    public List<FileVO> boardFileSearch(BoardSearchVO search) {
        return null;
    }

    @Override
    public int updateBoard(Board board, List<FilePublicVO> fileList, AccountVO member) throws Exception {
        return 0;
    }

    /**
     * 검색 날짜 범위 조회
     * @return list {@link List}
     */
    public List<SelectOptionVO> getSearchDateRangeOptionList(String listType) {
        var list = new ArrayList<SelectOptionVO>();
        list.add(new SelectOptionVO(0, "ALL", "전체"));

        if(listType.equalsIgnoreCase("APPROVE")) {
            list.add(new SelectOptionVO(0, "REG_DT", "작성일"));
            list.add(new SelectOptionVO(0, "COMPLETED", "완료일"));

            return list;
        }
        list.add(new SelectOptionVO(0, "REG_DT", "등록일"));

        return list;
    }

    /**
     * 검색 키워드 타입 조회
     * @return list {@link List}
     */
    public List<SelectOptionVO> getSearchKeywordTypeList() {
        var list = new ArrayList<SelectOptionVO>();
        list.add(new SelectOptionVO(0, "ALL", "전체"));
        list.add(new SelectOptionVO(0, "TITLE", "제목"));
        list.add(new SelectOptionVO(0, "REG_ID", "등록자"));

        return list;
    }

    /**
     * 공통 게시판 전체 게시물 수 조회
     * @param boardSearchVO {@link BoardSearchVO}
     * @return {@link Integer}
     */
    public int totalCount(BoardSearchVO boardSearchVO) {
        return boardMapper.findAllTotalCount(boardSearchVO);
    }

    /**
     * 게시판 삭제
     * @param lastId {@link String}
     * @param tgtList {@link Map}
     * @return result {@link Map}
     */
    public Map<String, Object> deleteBySeq(String lastId, List<Integer> tgtList) {
        var result = new HashMap<String, Object>();
        result.put("code", 200);

        var paramMap = new HashMap<String, Object>();
        paramMap.put("lastId", lastId);
        paramMap.put("boardSeqList", tgtList);
        int deleteCount = boardMapper.deleteList(paramMap);
        if(deleteCount < 1) {
            result.put("code", 204);
            result.put("message", "삭제할 대상이 없습니다.");
            return result;
        }


        ArrayList<String> attachList= new ArrayList<>();
        tgtList.forEach(value->{
            BoardSearchVO searchVO = new BoardSearchVO();
            searchVO.setBoardSeq(Long.valueOf(value));
            BoardPublicVO target = boardMapper.findById(searchVO);
            attachList.addAll(fileUtil.splitAttachId(target.getAttachId()));
        });
        paramMap.put("seqList",attachList); // 파일 입출력을 위한 seqList Setting
        fileMapper.deleteFileList(paramMap);
        result.put("message", "삭제 되었습니다.");

        return result;
    }

    /**
     * 게시판 수정
     * @param board {@link Board}
     * @return result {@link Map}
     */
    public Map<String, Object> updateBoard(Board board, List<MultipartFile> files) throws Exception {
        var result = new HashMap<String, Object>();
        result.put("code", 200);

        int updateCount = boardMapper.updateBoard(board);
        if(updateCount < 1) {
            result.put("code", 204);
            result.put("message", "수정할 대상이 없습니다.");
            return result;
        }
        result.put("message", "수정 되었습니다.");

        // FileUpdate로직
        List<String> originFiles = new ArrayList<>();
        BoardSearchVO boardSearchVO = new BoardSearchVO();
        boardSearchVO.setBoardSeq(board.getBoardSeq());
        BoardPublicVO boardPublicVO =boardMapper.findById(boardSearchVO);
        if(!StringUtil.isEmpty(boardPublicVO.getAttachId())){
            originFiles.addAll(fileUtil.splitAttachId(boardPublicVO.getAttachId()));
        }

        List<String> attachList = new ArrayList<>();
        // board.getFiles가 빈배열이 아닐때,
        if(!ObjectUtils.isEmpty(board.getFiles()) && !ObjectUtils.isEmpty(originFiles.isEmpty())){
            fileService.updateFiles(board.getFiles(), originFiles, board.getLastId());
            board.getFiles().forEach(file -> {
                attachList.add(String.valueOf(file.getFileSeq()));
            });
        }


        // 신규 파일 upload
        if(!ObjectUtils.isEmpty(files)){
            List<FilePublicVO> targetFiles = fileService.uploadFile(files);
            // 공통 File에 신규 file 저장

            fileService.saveFile(targetFiles,board.getRegId()).forEach(value-> attachList.add(String.valueOf(value.getFileSeq())));
        }

        String attachId="";
        if(!ObjectUtils.isEmpty(attachList)){
            attachId = fileUtil.makeAttachIdStr(attachList);
            Board targetBoardFile = Board.builder()
                    .boardSeq(board.getBoardSeq())
                    .attachId(attachId)
                    .build();
            updateBoardFile(targetBoardFile);
        }
        return result;
    }

    /**
     * 게시판 파일 수정
     */
    public void updateBoardFile(Board board) throws CustomFileException {
        try{
            boardMapper.updateBoardFile(board);
        } catch(Exception e){
            throw new CustomFileException();
        }

    }

    /**
     * 게시판 등록
     * @param boardPublic
     * @return result {@link Map}
     */
    public Map<String, Object> registration(BoardPublic boardPublic, List<MultipartFile> files) throws Exception {
        var result = new HashMap<String, Object>();
        result.put("code", 200);

        BoardPublic target = BoardPublic.builder()
                .boardCd(boardPublic.getBoardCd())
                .title(boardPublic.getTitle())
                .contents(boardPublic.getContents())
                .regId(boardPublic.getRegId())
                .build();

        Map<String, Object> validated = validationUtil.clientRequestParameterValidator(target, BoardPublic.class);
        if(!validated.isEmpty()) {
            result.put("code", 400);
            result.put("message", "잘못된 요청");
            result.put("errMsg", validated);
            return result;
        }

        int regCount = boardMapper.registrationBoard(boardPublic);
        if(regCount < 1) {
            result.put("code", 204);
            result.put("message", "등록에 실패 했습니다.");
            result.put("errMsg", "incomplete");
            return result;
        }

        if(!StringUtil.isEmpty(files)){
            // 실제 directory에 저장한 파일 리스트
            List<FilePublicVO> targetFiles = fileService.uploadFile(files);
            // DB에 저장한 파일 리스트
            List<FilePublicVO> saveFileList = fileService.saveFile(targetFiles, boardPublic.getRegId());

            if(saveFileList.isEmpty()){
                result.put("code",204);
                result.put("message","파일 저장에 실패하였습니다.");
                result.put("errMsg","incomplete");
            }

            String attachIds = fileUtil.makeAttachIdVO(saveFileList);
            log.info("attachIds : {}",attachIds);
            Board fileTargetBoard = Board.builder()
                    .boardSeq(boardPublic.getBoardSeq())
                    .attachId(attachIds)
                    .build();

            boardMapper.updateBoardFile(fileTargetBoard);
        }

        result.put("message", "등록 되었습니다.");

        return result;
    }

    /**
     * 공통 게시판 댓글 삭제
     * @param paramMap
     * @return
     */
    public List<BoardReplyVO> removeBoardReply(Map<String, Object> paramMap) {
        boardMapper.removeReplyBySeq(paramMap);

        List<BoardReplyVO> replyBySeq = boardMapper.findReplyBySeq(Long.parseLong(paramMap.get("boardSeq").toString()));
        if(!replyBySeq.isEmpty()) {
            replyBySeq.forEach(ele -> ele.setRegDtStr(DateUtil.convertLocalDateTimeToString(ele.getRegDt(), DateUtil.DATETIME_YMDHM_PATTERN)));
        }

        return replyBySeq;
    }
}
