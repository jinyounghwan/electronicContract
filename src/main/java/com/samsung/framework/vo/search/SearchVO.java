package com.samsung.framework.vo.search;

import com.samsung.framework.domain.common.Paging;
import com.samsung.framework.vo.search.board.BoardSearchVO;
import com.samsung.framework.vo.search.code.CodeSearchVO;
import com.samsung.framework.vo.search.menu.MenuSearchVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@AllArgsConstructor
@NoArgsConstructor
public class SearchVO {
    /**
     *  TO-DO : 임직원계정search 내용 추가
     */

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
     * 검색어
     */
    private String searchKeyword = "";
    /**
     * 날짜 검색 시작일 YYYY-MM-DD
     */
    private String startDt = "";

    /**
     * 날짜 검색 종료일 YYYY-MM-DD
     */
    private String endDt = "";

    /** paging 모델 [[ **/

    private Paging paging;

    /**
     * 검색 dropdown code
     */
    private String code;
    /**
     * 검색 dropdown 이름
     */
    private String displayName;

    public SearchVO( String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    /**
     * Date Dropdown리스트
     * @return
     */
    public List<SearchVO> getSearchDateRangeOptionList() {
        var list = new ArrayList<SearchVO>();
        list.add(new SearchVO("ALL","전체"));
        list.add(new SearchVO("CREATED_AT","작성일"));
        list.add(new SearchVO("UPDATED_AT", "수정일"));
        return list;
    }

    /**
     * SearchKeyWord Dropdown리스트
     * @return
     */
    public List<SearchVO> getSearchKeywordTypeOptionList() {
        var list = new ArrayList<SearchVO>();
        list.add(new SearchVO("ALL", "전체"));
        list.add(new SearchVO("TITLE", "제목"));
        list.add(new SearchVO("EMPLOYEE_ID","사번"));
        list.add(new SearchVO( "NAME","이름"));
        return list;
    }


    /**
     * Doc.Status Dropdown리스트
     * @return
     */
    public List<SearchVO> getContractDocSearchStateTypeList(){
        var list = new ArrayList<SearchVO>();
        list.add(new SearchVO("ALL", "전체"));
        list.add(new SearchVO( "CREATED","생성"));
        list.add(new SearchVO("ASSIGNED","ASSIGNED"));
        list.add(new SearchVO("RECALLED","회수"));
        list.add(new SearchVO("COMPLETED", "완료"));
        list.add(new SearchVO("PAPER_CONTRACT","수기 계약서"));
        return list;
    }

    public List<SearchVO> getContractSearchStateTypeList() {
        var list = new ArrayList<SearchVO>();
        list.add(new SearchVO("ALL","전체"));
        list.add(new SearchVO("UNSEEN","확인 전"));
        list.add(new SearchVO( "VIEW","확인 후"));
        list.add(new SearchVO("SIGNED", "서명 완료"));
        list.add(new SearchVO("REJECTED", "거절"));
        return list;
    }

    /**
     * 계정관리 DropdownList
     * @return
     */
    public List<SearchVO> getAcctSearchKeywordTypeOptionList(){
        var list = new ArrayList<SearchVO>();
        list.add(new SearchVO("ALL", "전체"));
        list.add(new SearchVO("EMPLOYEE_ID","사번"));
        list.add(new SearchVO( "NAME","이름"));
        return list;
    }


}
