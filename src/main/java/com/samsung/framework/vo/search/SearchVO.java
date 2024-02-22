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
    /**
     * 계약서  doc 상태 유형
    * */
    private String contractDocStatusType;
    /**
    *  계약서 상태 유형
    * */
    private String contractStatusType;

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
        list.add(new SearchVO("ALL","ALL"));
        list.add(new SearchVO("CREATED_AT","CREATED DATE"));
        list.add(new SearchVO("UPDATED_AT", "UPDATE DATE"));
        return list;
    }

    /**
     * SearchKeyWord Dropdown리스트
     * @return
     */
    public List<SearchVO> getSearchKeywordTypeOptionList() {
        var list = new ArrayList<SearchVO>();
        list.add(new SearchVO("ALL", "ALL"));
        list.add(new SearchVO("TITLE", "TITLE"));
        list.add(new SearchVO("EMPLOYEE_ID","EMPLOYEE ID"));
        list.add(new SearchVO( "NAME","NAME"));
        return list;
    }
    /**
     * SearchKeyWord Dropdown리스트
     * @return
     */
    public List<SearchVO> getSearcTempletehKeywordTypeOptionList() {
        var list = new ArrayList<SearchVO>();
        list.add(new SearchVO("ALL", "ALL"));
        list.add(new SearchVO("TITLE", "TITLE"));
        list.add(new SearchVO( "TYPE","TEMPLATE TYPE"));
        return list;
    }


    /**
     * Doc.Status Dropdown리스트
     * @return
     */
    public List<SearchVO> getContractDocSearchStateTypeList(){
        var list = new ArrayList<SearchVO>();
        list.add(new SearchVO("ALL", "ALL"));
        list.add(new SearchVO( "PRCS1001","CREATED"));
        list.add(new SearchVO("PRCS1002","ASSIGNED"));
        list.add(new SearchVO("PRCS1003","RECALLED"));
        list.add(new SearchVO("PRCS1004", "COMPLETED"));
        list.add(new SearchVO("PRCS1005","PAPERCONTRACT"));
        return list;
    }
    /**
     * contract Status Dropdown리스트
     * @return
     */
    public List<SearchVO> getContractSearchStateTypeList() {
        var list = new ArrayList<SearchVO>();
        list.add(new SearchVO("ALL","ALL"));
        list.add(new SearchVO("PRCS2001","UNSEEN"));
        list.add(new SearchVO( "PRCS2002","VIEWED"));
        list.add(new SearchVO("PRCS2003", "SIGNED"));
        list.add(new SearchVO("PRCS2004", "REJECTED"));
        return list;
    }

    /**
     * 계정관리 DropdownList
     * @return
     */
    public List<SearchVO> getAcctSearchKeywordTypeOptionList(){
        var list = new ArrayList<SearchVO>();
        list.add(new SearchVO("ALL", "ALL"));
        list.add(new SearchVO("EMPLOYEE_ID","EMPLOYEE ID"));
        list.add(new SearchVO( "NAME","NAME"));
        return list;
    }

    /**
     * 계약서 템플릿 관리 DropwdownList
     * @return
     */
    public List<SearchVO> getTemplateSearchKeywordTypeOptionList() {
        var list = new ArrayList<SearchVO>();
        list.add(new SearchVO("TMPL1000","Employeement contract"));
        list.add(new SearchVO("TMPL2000", "Salary contract"));
        list.add(new SearchVO("TMPL3000","Privacy agreement"));
        return list;
    }

}
