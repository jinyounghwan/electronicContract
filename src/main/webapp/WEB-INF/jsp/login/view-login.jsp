<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <!-- Header Layout include -->
    <c:import url="../common/header.jsp"></c:import>
    <style>
        .main_wrapper {
            display:flex;
            justify-content: center;
        }
        .login_wrapper {
            background-color: rgb(230 230 230);
            height:400px;
            width:500px;
            text-align:center;
            margin:100px;
        }

    </style>
</head>
<body>
<div class="main_wrapper">
    <div class="login_wrapper">
        <h1>로그인</h1>
        <form id="loginForm">
            <div>
                <label for="userId">아이디 </label><br>
                <input id="userId" name="userId" />
            </div>
            <div>
                <label for="password">비밀번호 </label><br>
                <input type="password" id="password" name="password"/>
            </div>
            <br>
            <div id="login">
                <input type="submit" value="로그인" onclick="login()"/>
            </div>
            <div id="memberWrapper">
                <input type="button" value="회원가입" onclick="location.href='/member/sign-up'"/>
                <input type="button" value="아이디찾기" onclick="location.href='/member/findById'"/>
                <input type="button" value="비밀번호찾기" onclick="location.href='/member/findByPassword'"/>
            </div>
        </form>

        <div id="result"></div>
    </div>
</div>

<c:import url="../common/footer.jsp"></c:import>
<script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>

<script>

    function login(){
        let userId = document.getElementById("userId").value;
        let password = document.getElementById("password").value;

        // 아이디 비밀번호 유효성 검사
        if(!userId || !password){
            alert("사용자 아이디 또는 비밀번호를 입력해주세요.");
            return;
        }

        let member = {
            userId: userId,
            password: password
        }

        // 로그인을 위하여 JSON
        $.ajax({
            type : "POST",
            url  : "/login",
            contentType: "application/json",
            async:false,
            data: JSON.stringify(member),
            success: function(result){
                window.location.href="/";
                alert("로그인 성공하였습니다.");
            },
            error: function(request, status, error){
                let result = JSON.parse(request.responseText);
                alert(result.message);
            }
        });


    }

</script>
</body>
</html>