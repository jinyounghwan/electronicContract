<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <!-- Header Layout include -->
    <c:import url="../common/header.jsp"></c:import>
</head>
<body>
    <c:import url="../common/left.jsp"></c:import>

    <form:form action="/jsp/addRedirect" onsubmit="valueSetter()" modelAttribute="sample">
        <h3>값 세팅</h3>
        <form:label path="seq" for="seq" title="시퀀스">Sequence: </form:label>
        <form:input id="seq" path="seq"/><br>
        <form:label path="name" for="name" title="이름">이름: </form:label>
        <form:input id="name" path="name"/><br>
        <form:label path="email" for="email" title="이메일">이메일: </form:label>
        <form:input id="email" path="email"/><br>

        <h3>Checkbox</h3>
        <form:checkbox path="interests" value="Basketball" />
        <form:checkbox path="interests" value="Baseball" />

        <h3>Checkboxes</h3>
        <form:checkboxes path="interests" items="${checkboxList}"/>

<%--        <h3>Radiobutton</h3>--%>
<%--        <form:radiobutton path="receiveNotification" value="Y" label="Yes"/>--%>
<%--        <form:radiobutton path="receiveNotification" value="N" label="No"/>--%>

        <h3>Radiobuttons</h3>
        <h4>radioList</h4>
        <form:radiobuttons path="receiveNotification" items="${radioList}"/>
<%--        <h4>radioMap</h4>--%>
<%--        <form:radiobuttons path="receiveNotification" items="${radioMap}"/>--%>

        <br><br>
        <input type="submit" value="submit">
    </form:form>
    <c:import url="../common/footer.jsp"></c:import>
<script>
    let valueSetter = () => {
        document.getElementById("seq").value = "1111";
        document.getElementById("name").value = "new name";
        document.getElementById("email").value = "new email";
    }
</script>
</body>
</html>