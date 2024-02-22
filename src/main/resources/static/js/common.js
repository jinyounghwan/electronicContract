/**
* @author: 허민지
* @since: 2024-01-19
* @desc :
**/
/*
   목록 조회를 위한
   기본 데이터 셋팅
   기본 valid
*/
const _toString = Object.prototype.toString;
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
        contractDocStatusType : $('[data-select="contractDocSearchStateTypeSelect"] option:selected').val(),
        contractStatusType : $('[data-select="contractSearchStateTypeSelect"] option:selected').val(),
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
/*
*   목록에 보여 줄 개수 선택
*/
let viewCountSelect = (selected) => {
    console.log(selected.value);
    let displayRow = selected.value;
    let totalCount = $('#totalCount').val();
    searchList(1 , displayRow ,totalCount);
}
/*
* form 아래에
data-required true or false
data-maxlength 최대 글자수
data-minlength 최소 글자수

: 
속성이 포함 되어 있는 요소 validation
*/
let valid = (_$selector) =>{
    let isValid = true;
    if(isArray(_$selector)) {
        $(_$selector).each(function() {
            return isValid = privateValid.call(this, _$selector);
        });
    }
    else {
        _$selector.find('input,select,textarea').each(function() {
            return isValid = privateValid.call(this, _$selector);
        });
    }
    return isValid;
}

/**
* 오브젝트가 Array인지 판단합니다.
*/
function isArray(O) {
    return _toString.call(O) == "[object Array]";
}
/**
* 오브젝트가  undefined , null , "" 인지 판단합니다.
*/
function isEmpty(O) {
  return typeof O === "undefined" || O === null || O === "";
}
function privateValid(_$selector) {
    let $this = $(this);
    let tag = $this.prop('tagName').toLowerCase();
    let type = $this.prop('type').toLowerCase();
    let v = $this.val();
    if($this.is('[data-required="true"]')) {
        if (tag === 'input' && (type === 'radio' || type === 'checkbox')) {
            let name = $this.attr('name');
            if(! _$selector.find('input[name="' + name + '"]').is(':checked')) {
                alert(getValidMessage($this, 'required'));
                $this.focus().select();
                return false;
            }
        }
        else {
            if(! v) {
                alert(getValidMessage($this, 'required'));
                $this.focus().select();
                return false;
            }
        }
    }
    if($this.is('[data-minlength]')) {
        let minlength = parseInt($this.attr('data-minlength'));
        if($this.val().length < minlength) {
            alert(getValidMessage($this, 'minlength'));
            $this.focus().select();
            return false;
        }
    }
    if($this.is('[data-maxlength]')) {
        let maxlength = parseInt($this.attr('data-maxlength'));
        if($this.val().length > maxlength) {
            alert(getValidMessage($this, 'maxlength'));
            $this.focus().select();
            return false;
        }
    }
    return true;
}
/**
    alert 메시지
*/
function getValidMessage(_$, _type) {
    let label = _$.attr('data-label'),
        alertMessage = _$.attr('data-alert');
    let tag = _$.prop('tagName').toLowerCase();
    let type = _$.prop('type').toLowerCase();
    if(_type === 'required') {
        let s = '입력';
        switch(tag) {
            case 'select':
                s = '선택';
                break;
            case 'input':
                if(type == 'checkbox' || type == 'radio') {
                    s = '선택';
                }
                break;
        }
        return alertMessage ? alertMessage : label + '을(를) ' + s + '해주세요.';
    }
    else if(_type === 'minlength') {
        let minlength = parseInt(_$.attr('data-minlength'));
        return alertMessage ? alertMessage : label + '을(를) ' + minlength + '자 이상 입력해주세요.';
    }
    else if(_type === 'maxlength') {
        let maxlength = parseInt(_$.attr('data-maxlength'));
        return alertMessage ? alertMessage : label + '은(는) ' + maxlength + '자를 초과할 수 없습니다.';
    }
}
/*
    전체 체크
*/
let allCheckBox = (_$) => {
    let checked = $(_$).is(':checked');
    if(checked){
        $('[data-select="check"]').prop('checked' , true);
    }else{
        $('[data-select="check"]').prop('checked' , false);
    }
}
/*
    alert call
*/
let type;
let openAlert = (text, _type) => {
    $('[data-target="modal"]').attr('style' , 'display:block');
    $('[data-target="background"]').attr('class' , 'modal-backdrop');
    $('[data-select="alertText"]').text(text);
    type = _type;
}
let closeAlert = () => {
    $('[data-target="modal"]').attr('style' , 'display:none');
    $('[data-target="background"]').removeAttr('class');
    $('[data-select="alertText"]').text('');
    type = '';
}
let updateOk = () =>{
    switch(type){
        case 'updateAssign' : updateAssign(); break;
        case 'updateRecall' :updateRecall(); break;
        case 'recall' :recall(); break;
        case 'assign' :assign(); break;
        default: return false;
    }
}
let viewHistory = (seq) =>{
     // 팝업창 열기
     $('[data-target="history"]').removeAttr('style');
     let data = {'contractNo' : seq }
     $.ajax({
        url: '/contract/view/history',
        type: 'post',
        dataType:'json',
        data:JSON.stringify(data),
        contentType: 'application/json; charset=UTF-8',
    }).done(function(data) {
        $('[data-target="historyBody"]').empty();
        if(data.length > 0){
            data.forEach((value)=> {
                var $tr  = $('<tr>');
                var $td1 = $('<td>' +value.processStep+ '</td>');
                var $td2= $('<td>' +value.createdByName + ' ('+ value.createdBy+') '+'</td>');
                var $td3 = $('<td>' +value.createdAt+ '</td>');
                var $td4 = $('<td>' +value.ipAddress+ '</td>');
                $tr.append($td1);
                $tr.append($td2);
                $tr.append($td3);
                $tr.append($td4);
                $('[data-target="historyBody"]').append($tr);

            });
        }else{
             var $tr  = $('<tr>');
             var $td = $('<td colspan="4">데이터 가 없습니다. </td>');
             $tr.append($td);
             $('[data-target="historyBody"]').append($tr);
        }
        $('[data-target="history"]').attr('style' , 'display:block');
        $('[data-target="historyBackground"]').attr('class' , 'modal-backdrop');
    }).fail(function(jqXHR) {
        console.log(jqXHR);
    });

}
let closeHistoryView = () =>{
    $('[data-target="history"]').attr('style' , 'display:none');
    $('[data-target="historyBackground"]').removeAttr('class');
}
/*view contract*/
let viewContract = (seq) =>{
     // 팝업창 열기
     $('[data-target="view"]').removeAttr('style');
     let data = {'contractNo' : seq }
     $.ajax({
        url: '/contract/view',
        type: 'post',
        dataType:'json',
        data:JSON.stringify(data),
        contentType: 'application/json; charset=UTF-8',
    }).done(function(data) {
        console.log(data);
        if(!isEmpty(data)){
            $('[data-select]').each(function(index, item){
                var $this = $(item);
                var key = $this.data('select');
                $this.html(data[key]);
            });
            $('[data-target="view"]').attr('style' , 'display:block');
            $('[data-target="viewBackground"]').attr('class' , 'modal-backdrop');
            if((data.docStatus != 'PRCS1002' && data.docStatus != 'PRCS1001' ) && (data.processStatus != 'PRCS2003' && data.processStatus != 'PRCS2004')){
                console.log('aaa');
                $('[data-target="statusBtn"]').hide();
            }
        }
    }).fail(function(jqXHR) {
        console.log(jqXHR);
    });

}
/*
    contract viewer close
*/
let closeContractView = () =>{
    $('[data-target="view"]').attr('style' , 'display:none');
    $('[data-target="viewBackground"]').removeAttr('class');
}
/*
    ViewPaperContract
 */
let viewPaperContract = (fileDataNo) =>{
    $('[data-target="paper-view"]').removeAttr('style');
    if(!isEmpty(fileDataNo)){
        $('[data-target="paper-view"]').attr('style','display:block');
        $('[data-target="paperViewBackground"]').attr('class' , 'modal-backdrop');
        // 파일 다운로드 시
        $('#download').on("click", function(){
            $.ajax({
                url: "/file/download/"+fileDataNo,
                type: "GET",
                xhrFields: {
                    responseType: 'blob' // 응답을 Blob 객체로 받음
                }
            }).done(function(blob, status, xhr) {
                // 파일 다운로드
                buttonFileDownload(blob, xhr);
            })
            .fail(function(jqXHR) {
                console.log(jqXHR);
            });
        });
    }
}

let closePaperContract = () => {
    $('[data-target="paper-view"]').attr('style', 'display:none');
    $('[data-target="paperViewBackground"]').removeAttr('class');
}
/*
    button 클릭 시  input type=file 실행
*/
function openFile(){
    //$('#file-input').attr('disabled' , false);
    $('[data-select="fileInput"]').trigger('click');
    //id="file-input"
}
/*
    file 명
*/
function addFileText(_this){
    let $this = $(_this);
    let fileName = $this[0].files[0].name;
    $('[data-select="fileName"]').text(fileName);
    //$('#file-input').attr('disabled' , true);
}

// reject
let rejectConfirm = () =>{
    $('[data-target="reject"]').attr('style' , 'display:block');
    $('[data-select="rejectReason"]').val('');
    $('[data-target="rejectBackground"]').attr('class' , 'modal-backdrop');
}
// reject ok call
let rejectConfirmCheck = () =>{
    if(valid($('#rejectForm'))){
        // reject 확인 시
        console.log($('#contractNo').val())
        let data = {'contractNo' : $('#contractNo').val()
                    ,'rejectReason' : $('[data-select="rejectReason"]').val()
        };
        $.ajax({
            url: '/contract/sign/wait/reject/update',
            type: 'post',
            dataType:'json',
            data:JSON.stringify(data),
            contentType: 'application/json; charset=UTF-8',
        }).done(function(data) {
           $('[data-target="view"]').removeAttr('style');
           $('[data-target="reject"]').removeAttr('style');
           $('[data-target="rejectBackground"]').removeAttr('class');
           location.href= '/contract/sign/wait';
        }).fail(function(jqXHR) {
            console.log(jqXHR);
        });
    }
}

let rejectConfirmClose = () =>{
    $('[data-target="reject"]').attr('style' , 'display:none');
    $('[data-select="rejectReason"]').val('');
    $('[data-target="rejectBackground"]').removeAttr('class');
}
let completeConfirmClose = () =>{
    $('[data-target="alert3"]').attr('style' , 'display:none');
    $("#formCheck1").prop("checked", false);
}

let contractComplete = () =>{
    if(!$("#formCheck1").is(':checked')){
        return false;
    }
    // complete 확인 시
    let data = {'contractNo' : $('#contractNo').val()};
    $.ajax({
        url: '/contract/sign/wait/complete/update',
        type: 'post',
        dataType:'json',
        data:JSON.stringify(data),
        contentType: 'application/json; charset=UTF-8',
    }).done(function(data) {
       $('[data-target="view"]').removeAttr('style');
       $('[data-target="reject"]').removeAttr('style');
       $('[data-target="rejectBackground"]').removeAttr('class');
       location.href= '/contract/sign/wait';
    }).fail(function(jqXHR) {
        console.log(jqXHR);
    });
}

/**
 * Redirect to Error Page
 * @param jqXHR
 */
let sendToErrorPage = (jqXHR) => {
    let ex = JSON.parse(jqXHR.responseText);
    console.log('redirectToErrorPage :: ' + ex);
    window.location.href='/error/common?message=' + ex.message;
}

let openAgreementAlert = () => {
    $('[data-target="alert3"]').attr('style' , 'display:block');
}

/**
 * Pdf Download
 */
let pdfDownload = () => {
    let formData = new FormData();
    let html = $('#target-pdf').html();

    let param = {
        "html": html
    };
    console.log(html);

    formData.append('param', new Blob([JSON.stringify(param)], { type: 'application/json' }));

    $.ajax({
        url: '/pdf/download',
        type: 'POST',
        cache: false,
        data: formData,
        contentType:false,
        processData:false,
        xhrFields: {
            responseType: 'blob' // 응답을 Blob 객체로 받음
        }
    }).done(function (blob, status, xhr){
        buttonFileDownload(blob, xhr);
    }).fail(function(jqXHR){
        console.log(jqXHR);
    })
}

// 파일 다운로드
function buttonFileDownload(blob, xhr){
    // check for a filename
    let fileName = "";
    let disposition = xhr.getResponseHeader("Content-Disposition");
    if(disposition && disposition.indexOf("attachment") !== -1) {
        let filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
        let matches = filenameRegex.exec(disposition);
        if(matches != null && matches[1]){
            fileName = decodeURI(matches[1].replace(/['"]/g, ""));
            console.log(fileName);
        }

    }
    if (window.navigator && window.navigator.msSaveOrOpenBlob) {
        window.navigator.msSaveOrOpenBlob(blob, fileName);
    } else {
        let URL = window.URL || window.webkitURL;
        let downloadUrl = URL.createObjectURL(blob);

        if (fileName) {
            let a = document.createElement("a");

            // for safari
            if (a.download === undefined) {
                window.location.href = downloadUrl;
            } else {
                a.href = downloadUrl;
                a.download = fileName;
                document.body.appendChild(a);
                a.click();
            }
        } else {
            window.location.href = downloadUrl;
        }
    }
}