/**
* @author: 허민지
* @since: 2024-01-19
* @desc :
**/
/*
   목록 조회를 위한
   기본 데이터 셋팅
*/
function setData(){
    let parameter = {
        paging :{
          totalCount : $('#totalCount').val()
        }
    }
    return parameter;
}
/*
    페이징 , 검색어 조회
*/
let searchList = (currentPage , displayRow, totalCount)=> {
    let viewRow = parseInt(displayRow , 0 );
    let total = parseInt(totalCount , 0 );
    if(viewRow == 0 ){
        viewRow = $('[data-select="viewDropDown"] option:selected').val();
    }
    let parameter = {
        searchDateType : $('[data-select="dateRangeSelect"] option:selected').val(),
        searchKeywordType : $('[data-select="keywordTypeSelect"] option:selected').val(),
        startDt: $('#startDt').val(),
        endDt: $('#endDt').val(),
        searchKeyword :$('#searchKeyword').val(),
        paging : {
              currentPage: currentPage,
              displayRow: viewRow,
              totalCount: total,

        }
    }
    getList(parameter);
}
let viewCountSelect = (selected) => {
    console.log(selected.value);
    let displayRow = selected.value;
    let totalCount = $('#totalCount').val();
    searchList(1 , displayRow ,totalCount);
}



