
/**
 * 태그 존재 여부 확인 존재하지 않으면 false 존재하면 true
 * @param arrayTag (Array)
 * @returns {boolean}
 */
function isExist(arrayTag){
    for(let i=0; i< arrayTag.length; i++){
        let htmlTag = document.getElementById(arrayTag[i]);
        if(isEmpty(htmlTag)) return false;
    }
    return true;
}
/**
 * 빈 값 여부 확인
 * @param targetData
 * @returns {boolean}
 */
function isEmpty(targetData){
    if(targetData === '' || targetData === null || targetData === undefined){
        return true;
    }
    return false;
}

// html tag 테이블 데이터 Setting
function settingTableData(id){
    // 엑셀로 변환할 테이블 id tag 가져오기
    let table = document.getElementById(id);

    let rowList = table.rows;

    let arrData = [];
    let rowNum = rowList.length;
    let columnNum;
    
    // table 데이터 가져오기
    for(let i=0; i< rowList.length; i++){
        let row = rowList[i];
        columnNum = rowList[0].childElementCount;

        for(let j=0; j<columnNum; j++){
            if(!isEmpty(row.cells[j])){
                let str = row.cells[j].innerText;

                let arr= Array.from(str);
                arrData.push(arr);
            }
        }
    }
    
    // 's','t','r' 형식으로 있는 데이터 문자열로 형식 변경
    let data= new Array();
    let str;
    for(let i=0; i<arrData.length; i++){
        str = "";
        for(let j=0; j<arrData[i].length; j++){
                str+=arrData[i][j];
        }
        data.push(str);
    }

    const arrayData = new Array(rowNum);
     for(let i=0; i < rowNum; i++){
         arrayData[i] = new Array(columnNum);
     }
    
     // 1차원 데이터 값 2차원 배열 데이터로 형식 변경
     let count =0;
     for(let i=0; i < arrayData.length; i++){
         for(let j=0; j < arrayData[i].length; j++){
             arrayData[i][j] = data[count];
             count++;
         }
     }
    return arrayData;
}



// 권한 테이블 Setting
function authTableSetting(id){
    let table = document.getElementById(id);
    let rowList = table.rows;

    let arrData = [];
    let rowNum = rowList.length;
    let columnNum;

    // table 데이터 가져오기
    for(let i=0; i< rowList.length; i++){
        let row = rowList[i];
        columnNum = rowList[0].childElementCount;

        for(let j=0; j<columnNum; j++){
            // column에 자식 요소를 확인
            let rowChildren = row.cells[j].children;
            let str='';
            // length 0 이상이면 CheckBox가 있는 것으로 간주
            if(rowChildren.length!==0){
                let len = rowChildren.length;

                for(let z=0; z<len; z++){
                    if(!isEmpty(rowChildren[z].innerText)){
                        str+=rowChildren[z].innerText;
                    }
                    if(rowChildren[z].type==="checkbox"){
                        if(rowChildren[z].checked){
                            str+='Y';
                        } else{
                            str+='N';
                        }
                    }

                }
                let arr = Array.from(str);
                arrData.push(arr);
            }else {
                if(!isEmpty(row.cells[j])){
                    let str = row.cells[j].innerText;
                    let arr= Array.from(str);
                    arrData.push(arr);
                }
            }

        }

    }

    let data= new Array();
    let str;
    for(let i=0; i<arrData.length; i++){
        str = "";
        for(let j=0; j<arrData[i].length; j++){
            str+=arrData[i][j];
        }
        data.push(str);
    }

    const arrayData = new Array(rowNum);
    for(let i=0; i < rowNum; i++){
        arrayData[i] = new Array(columnNum);
    }

    // 1차원 데이터 값 2차원 배열 데이터로 형식 변경
    let count =0;
    for(let i=0; i < arrayData.length; i++){
        for(let j=0; j < arrayData[i].length; j++){
            arrayData[i][j] = data[count];
            count++;
        }
    }

    return arrayData;
}

// 엑셀 다운로드
function fnExcelDownload(id, fileName){
    let excelDownload = document.getElementById(id);
    excelDownload.addEventListener("click", function(){
        let wb = XLSX.utils.table_to_book(document.getElementById("rfrnc-table"), {sheet: "sheet", raw:true});
        XLSX.writeFile(wb, (fileName));
    });
}
