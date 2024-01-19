package com.samsung.framework.vo.search;

import com.samsung.framework.domain.common.Paging;
import com.samsung.framework.vo.search.board.BoardSearchVO;
import com.samsung.framework.vo.search.code.CodeSearchVO;
import com.samsung.framework.vo.search.menu.MenuSearchVO;
import lombok.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *  공용 검색 모델 <br>
 *  검색 조건이 더 생길 경우 상속 클래스를 별도로 만들 것
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SearchVO {

    /** paging 모델 [[ **/

    private Paging paging;

    /** paging 모델 ]] **/


    /**
     * 검색 날짜 유형
     */
    private String searchDateType;
    /**
     * 검색 키워드 유형
     */
    private String searchKeywordType;
    /**
     * 검색 상태 유형
     */
    private String searchStatusType;

    /**
     * 검색 dropdown seq
     */
    private long seq;
    /**
     * 검색 dropdown code
     */
    private String code;
    /**
     * 검색 dropdown 이름
     */
    private String displayName;

    public SearchVO(long seq, String code, String displayName) {
        this.seq = seq;
        this.code = code;
        this.displayName = displayName;
    }

    /**
     * Date Dropdown리스트
     * @return
     */
    public List<SearchVO> getSearchDateRangeOptionList() {
        var list = new ArrayList<SearchVO>();
        list.add(new SearchVO(1,"CREATED_AT","작성일"));
        list.add(new SearchVO(2,"UPDATED", "수정일"));
        return list;
    }

    /**
     * SearchKeyWord Dropdown리스트
     * @return
     */
    public List<SearchVO> getSearchKeywordTypeOptionList() {
        var list = new ArrayList<SearchVO>();
        list.add(new SearchVO(1,"ALL", "전체"));
        list.add(new SearchVO(2, "TITLE", "제목"));
        list.add(new SearchVO(3,"EMPLOYEE_ID","사번"));
        list.add(new SearchVO(4, "NAME","이름"));
        return list;
    }

    /**
     * Doc.Status Dropdown리스트
     * @return
     */
    public List<SearchVO> getContractDocSearchStateTypeList(){
        var list = new ArrayList<SearchVO>();
        list.add(new SearchVO(1,"ALL", "전체"));
        list.add(new SearchVO(2, "CREATED","생성"));
        list.add(new SearchVO(3,"ASSIGNED","ASSIGNED"));
        list.add(new SearchVO(4,"RECALLED","회수"));
        list.add(new SearchVO(5,"COMPLETED", "완료"));
        list.add(new SearchVO(6,"PAPER_CONTRACT","수기 계약서"));

        return list;
    }

    public List<SearchVO> getContractSearchStateTypeList() {
        var list = new ArrayList<SearchVO>();
        list.add(new SearchVO(1,"UNSEEN","확인 전"));
        list.add(new SearchVO(2, "VIEW","확인 후"));
        list.add(new SearchVO(3, "SIGNED", "서명 완료"));
        list.add(new SearchVO(4, "REJECTED", "거절"));

        return list;
    }
    
}
