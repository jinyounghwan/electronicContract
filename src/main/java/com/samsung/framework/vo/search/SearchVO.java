package com.samsung.framework.vo.search;

import com.samsung.framework.domain.common.Paging;
import com.samsung.framework.vo.search.board.BoardSearchVO;
import com.samsung.framework.vo.search.code.CodeSearchVO;
import com.samsung.framework.vo.search.menu.MenuSearchVO;
import lombok.Getter;
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
public class SearchVO {

    /**
     * 공용 날짜 표시 형식
     */
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 공용 시각 표시 형식
     */
    public static final SimpleDateFormat SIMPLE_TIME_FORMAT = new SimpleDateFormat("HH:mm");

    /**
     * 공용 날짜 시각 표시 형식
     */
    public static final SimpleDateFormat SIMPLE_DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static final int MILLIS_IN_A_DAY = 24 * 60 * 60 * 1000;


    /**
     * 회원 권한 관련
     */
    private String memberId;
    private String menuId;

    /**
     * 검색조건 > mapper에서 in 구문 사용하는 경우
     */
    private List<String> inList1 = new ArrayList<>();
    private List<String> inList2 = new ArrayList<>();
    private List<String> inList3 = new ArrayList<>();
    private List<String> inList4 = new ArrayList<>();
    private List<String> inList5 = new ArrayList<>();


    /**
     * 검색 조건(dropdown)
     */
    private String type1 = "";
    private String type2 = "";
    private String type3 = "";
    private String type4 = "";
    private String type5 = "";

    /**
     * 검색어
     */
    private String searchWord1 = "";
    private String searchWord2 = "";
    private String searchWord3 = "";
    private String searchWord4 = "";
    private String searchWord5 = "";

    /**
     * 해당 메뉴의 ID값
     */
    private String groupKey = "";

    /**
     * 정렬
     */
    private String orderBy = "";

    /**
     * 오름차순/내림차순
     */
    private String descAsc = "DESC";

    /**
     * 날짜 검색 시작일 YYYY-MM-DD
     */
    private String startDt = "";

    /**
     * 날짜 검색 종료일 YYYY-MM-DD
     */
    private String endDt = "";

    /**
     * 시간 검색 시작시간 HHmm
     */
    private String startTm = "";

    /**
     * 시간 검색 종료시간 HHmm
     */
    private String endTm = "";

    /**
     * [Date] 날짜 검색 시작일
     */
    private Date startDttm = null;

    /**
     * [Date] 날짜 검색 종료일
     */
    private Date endDttm = null;

    /**
     * [Date] 검색일
     */
    private Date searchDttm = null;

    /**
     * 검색일 YYYY-MM-DD
     */
    private String searchDt = "";

    /**
     * 요일 검색  D : 일(1) ~ 토(7)
     */
    private String dy = "";

    private String redirectURL = "";

    /**
     * 사용여부
     */
    private String useYn;




    /** paging 모델 [[ **/

    private Paging paging;

    /** paging 모델 ]] **/





    /** 그룹별 search 모델 [[ **/

    private CodeSearchVO codeSearch;
    private BoardSearchVO boardSearch;
    private MenuSearchVO menuSearch;
    /** 그룹별 search 모델 ]] **/

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

}
