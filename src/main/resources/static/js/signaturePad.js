let pad;
signaturePad = function(){
    var self = this;
    // default
    var option = {
        minWidth: 5,
        maxWidth: 10,
        penColor: "rgb(66, 133, 244)",
        backgroundColor:"#00ff0000"
    }
    var canvas = document.getElementById("signaturePad");
    pad = new SignaturePad(canvas,option);
}
// 패드 지우기
function signaturePadClear(){
    pad.clear()
}
// 서명 저장
function saveSignature(){
    pad.toSVG({includeBackgroundColor: false});
    var data = pad.toDataURL('image/png');
    $('[data-id="signatureImg"]').attr('src',data);
    $("#exampleModalScrollable").modal('hide');
}