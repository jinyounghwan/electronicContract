<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--<c:set var="contextPath" value="${pageContext.request.contextPath}" />--%>
<html>
<header>
    <c:import url="../common/header.jsp"></c:import>
</header>
<body>
    <h3>회원가입 폼</h3>
    <br>
    <form action="/sign-up" method="post">
        <h3>계정 정보 입력</h3>
        <div id="user-info-wrapper">
            <div id="id-wrapper">
                <label for="memberId">아이디 * </label>
                <input type="text" id="memberId" name="memberId"/>
                <div id="userResult"></div>
            </div>

            <div id="password-wrapper">
                <label for="password">비밀번호 * </label>
                <input type="password" id="password" name="password"/>
                <div id="passwordResult"></div>
            </div>

            <div id="password-check-wrapper">
                <label for="check-password">비밀번호 확인 * </label>
                <input type="password" id="check-password"/>
                <div id="passwordCheckResult"></div>
            </div>
        </div>
        <br>

        <div id="member-info-wrapper">
            <h3>회원 정보 입력</h3>
            <div>
                <label for="memberName">이름 * </label>
                <input type="text" id="memberName" path="memberName"/>
                <div id="memberNameResult"></div>
            </div>

            <div>
                <label for="userTel">전화번호 * </label>
                <input type="tel" name="userTel" id="userTel"/>
                <div id="userTelResult"></div>
            </div>

            <div>
                <label for="email">이메일 * </label>
                <input type="email" id="email"/>
                <div id="emailResult"></div>
            </div>

            <div>
                <label for="departmentCd">조직</label>
                <select name="departmentCd" id="departmentCd">
                    <option value="none">선택</option>
                    <option value="DEPT0101">구로</option>
                    <option value="DEPT0102">천안</option>
                    <option value="DEPT0103">분당</option>
                </select>
                <div id="departmentResult"></div>
            </div>

            <div>
                <label for="appointCd">직위</label>
                <select name="appointCd" id="appointCd">
                    <option value="none">선택</option>
                    <option value="DEPT0201">대표</option>
                    <option value="DEPT0202">임원</option>
                    <option value="DEPT0203">팀장</option>
                    <option value="DEPT0204">팀원</option>
                </select>
                <div id="appointResult"></div>
            </div>

            <div>
                <label for="positionCd">직책</label>
                <select name="positionCd" id="positionCd" onchange>
                    <option value="none">선택</option>
                    <option value="DEPT0301">대표이사</option>
                    <option value="DEPT0302">이사</option>
                    <option value="DEPT0303">본부장</option>
                    <option value="DEPT0304">수석</option>
                    <option value="DEPT0305">책임</option>
                </select>
                <div id="positionResult"></div>
            </div>
        </div>

        <input type="button" id="cancelBtn" value="취소" onclick="location.href='/login'">
        <input type="button" id="signUpBtn" value="회원가입 요청" onclick='signUpClick()'>
    </form>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
    <script>
        //회원가입 요청 클릭 시
        function signUpClick(){
            let memberId = document.getElementById("memberId").value;
            let password = document.getElementById("password").value;
            let checkPassword = document.getElementById("check-password").value;
            let name = document.getElementById("memberName").value;
            let userTel = document.getElementById("userTel").value;
            let email = document.getElementById("email").value;
            let deptCode = document.getElementById("departmentCd").value;
            let appointCode = document.getElementById("appointCd").value;
            let positionCode = document.getElementById("positionCd").value;

            let memberInfo = {
                id : memberId,
                password : password,
                checkPassword : checkPassword,
                name : name,
                userTel : userTel,
                email : email,
                deptCode : deptCode,
                appointCode : appointCode,
                positionCode : positionCode
            };
            memberInfoNullCheck(memberInfo);
            // 회원정보 Check
            if(name==="" ||password==="" || checkPassword === "" || email==="" || deptCode==="none" || appointCode==="none" || positionCode==="none"){
                alert("필수 회원정보를 올바르게 입력해주세요.");
            }
            else if(password === checkPassword) {// 비밀번호 확인, 비밀번호 체크 후 같으면 회원가입 진행
                $.ajax({
                    type : "POST",
                    url : "/member/sign-up",
                    contentType:"application/json",
                    async : false,
                    data : JSON.stringify(memberInfo),
                    success: function(result){
                        alert("회원가입 요청이 완료 되었습니다.\n 승인 완료 후 로그인이 가능합니다.");
                        window.location.href="/login";
                    },
                    error: function(request, status, error){
                        let result = JSON.parse(request.responseText);
                        if(result.code===987){ // 987 회원가입시 아이디가 이미 있는 경우
                            alert(result.message);
                        }else {
                            alert("회원가입을 실패하였습니다.")
                        }
                    }
                });
            }else {
                alert("패스워드가 일치하지 않습니다.");
            }
        }

        function memberInfoNullCheck(info){
           if(info.id===""){
               document.getElementById("userResult").innerText ="아이디를 입력해 주세요.";
           } else if(info.password===""){
               document.getElementById("passwordResult").innerText = "비밀번호를 입력해주세요.";
           } else if(info.checkPassword===""){
               document.getElementById("passwordCheckResult").innerText = "비밀번호를 한번 더 입력해 주세요.";
           } else if(info.email===""){
               document.getElementById("emailResult").innerText = "이메일을 입력해 주세요.";
           } else if(info.name===""){
                document.getElementById("memberNameResult").innerText = "이름을 입력해 주세요.";
           } else if(info.userTel===""){
                document.getElementById("userTelResult").innerText = "전화번호를 입력해 주세요.";
           } else if(info.deptCode==="none"){
                document.getElementById("departmentResult").innerText = "조직을 선택해 주세요.";
           } else if(info.appointCode==="none"){
                document.getElementById("appointResult").innerText = "직위를 선택해 주세요.";
           } else if(info.positionCode==="none"){
                document.getElementById("positionResult").innerText = "직급을 선턱해 주세요.";
           }
        }

    </script>
</body>
</html>