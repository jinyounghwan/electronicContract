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
        default: return false;
    }
}
/*view contract*/
let viewContract = (seq) =>{
     let data = {'contractNo' : seq }
     $.ajax({
        url: '/contract/view',
        type: 'post',
        dataType:'json',
        data:JSON.stringify(data),
        contentType: 'application/json; charset=UTF-8',
    }).done(function(data) {
        console.log(data)
    }).fail(function(jqXHR) {
        console.log(jqXHR);
    });

}
/*
    button 클릭 시  input type=file 실행
*/
function openFile(){
    $('[data-select="fileInput"]').trigger('click');
}
/*
    file 명
*/
function addFileText(_this){
    let $this = $(_this);
    let fileName = $this[0].files[0].name;
    $('[data-select="fileName"]').text(fileName);
}