package com.samsung.framework.domain.common;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class Paging {

    @NotNull(message = "현재 페이지 값은 필수 입니다.")
    private int currentPage; // 현재 페이지
    @NotNull(message = "노출 로우 값은 필수 입니다.")
    private int totalCount; // 전체 게시물
    private int totalPage; // 전체페이지 수

    private int displayRow = 10; // 한 페이지에 보여줄 로우 수
    private int displayPage = 10; // 한 페이지에 보여줄 페이지 수

    private int beginPage; // 시작 페이지
    private int endPage; // 종료 페이지
    private int prevPage; // 이전 페이지 Block
    private int nextPage; // 다음 페이지 Block

    private int offset; // 검색 로우

    public Paging() {
        setDefaultPaging(1, this.displayRow, 0);
    }

    @Builder
    public Paging(int currentPage, int displayRow, int totalCount) {
        if(totalCount < 1) {
            setDefaultPaging(currentPage, displayRow, totalCount);
        }else {
            this.currentPage = currentPage;
            this.displayRow = displayRow;
            this.totalCount = totalCount;

            setOffset();
            setTotalPage();
            setDisplayPageBlock();
        }
    }

    private void setDefaultPaging(int currentPage, int displayRow, int totalCount) {
        this.currentPage = currentPage;
        this.displayRow = displayRow;
        this.totalCount = totalCount;
        this.totalPage = 1;
        this.offset = 0;
        this.prevPage = 1;
        this.nextPage = this.displayPage;
    }

    private void setOffset() {
        this.offset = (getCurrentPage() - 1) * getDisplayRow();
    }

    private void setTotalPage() {
        if(getTotalCount() == 0 || getDisplayRow() == 0) {
            this.totalPage = 1;
        }else {
            this.totalPage = (int)Math.ceil(Float.valueOf(getTotalCount()) / Float.valueOf(getDisplayRow()));
        }
    }

    private void setDisplayPageBlock() {
        if(getTotalCount() == 0 || getDisplayRow() == 0) {
            this.beginPage = 1;
            this.endPage = 1;
            this.prevPage = 1;
            this.nextPage = 1;
        }else {
            this.beginPage = (((getCurrentPage() - 1) / getDisplayPage()) * getDisplayPage() + 1);
            this.endPage = ((((getCurrentPage() - 1) / getDisplayPage()) * getDisplayPage() + 1) + getDisplayPage() - 1) > getTotalPage() ? getTotalPage() : ((((getCurrentPage() - 1) / getDisplayPage()) * getDisplayPage() + 1) + getDisplayPage() - 1);
            this.prevPage = (getBeginPage() - getDisplayPage()) < 1 ? 1 : (getBeginPage() - getDisplayPage());
            this.nextPage = (getEndPage() + 1) > getTotalPage() ? getTotalPage() : (getEndPage() + 1);
        }
    }
}
