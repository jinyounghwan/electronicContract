$(document).ready(function() {
    const sDatePicker = $("#sDate");
    const eDatePicker = $("#eDate");


    sDatePicker.datepicker({
        dateFormat: "yy-mm-dd",
        showOtherMonths: true,          // 현재 월에 속하지 않는 날짜도 달력 위젯에 표시하도록 지시
        selectOtherMonths: true,        // 이전 월 및 다음 월의 날짜를 선택할 수 있도록 함
        closeText: "닫기",
        prevText: "이전달",
        nextText: "다음달",
        yearSuffix: "년", //달력의 년도 부분 뒤 텍스트
        monthNames: [ "1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"],
        dayNames: ["일", "월", "화", "수", "목", "금", "토"],
        dayNamesShort: ["일", "월", "화", "수", "목", "금", "토"],
        dayNamesMin : ["일", "월", "화", "수", "목", "금", "토"],
        showMonthAfterYear: true,
        buttonImageOnly: false,	// input 옆에 조그만한 아이콘으로 캘린더 선택가능하게 하기
        /*minDate: "-3Y", //최소 선택일자(-1D:하루전, -1M:한달전, -1Y:일년전)
        maxDate: "+3Y", //최대 선택일자(+1D:하루후, -1M:한달후, -1Y:일년후)*/

        onClose: function(selectedDate) {
            $("#eDate").datepicker("option","minDate",selectedDate);
        }
    });

    eDatePicker.datepicker({
        dateFormat: "yy-mm-dd",
        showOtherMonths: true,          // 현재 월에 속하지 않는 날짜도 달력 위젯에 표시하도록 지시
        selectOtherMonths: true,        // 이전 월 및 다음 월의 날짜를 선택할 수 있도록 함
        closeText: "닫기",
        prevText: "이전달",
        nextText: "다음달",
        yearSuffix: "년", //달력의 년도 부분 뒤 텍스트
        monthNames: [ "1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"],
        dayNames: ["일", "월", "화", "수", "목", "금", "토"],
        dayNamesMin : ["일", "월", "화", "수", "목", "금", "토"],
        showMonthAfterYear: true,
        buttonImageOnly: false,	// input 옆에 조그만한 아이콘으로 캘린더 선택가능하게 하기
        /*minDate: "-3Y", //최소 선택일자(-1D:하루전, -1M:한달전, -1Y:일년전)
        maxDate: "+3Y", //최대 선택일자(+1D:하루후, -1M:한달후, -1Y:일년후)*/
        onClose: function(selectedDate) {
            $("#sDate").datepicker("option","maxDate",selectedDate);
        }
    });
});

