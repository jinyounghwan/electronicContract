<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%--<c:set var="contextPath" value="${pageContext.request.contextPath}" />--%>
<html>
<head>
    <title>Add Sample</title>
<%--    <link href="<c:url value="/css/common.css"/>" rel="stylesheet" type="text/css">--%>
    <style>
        table {
            width: 100%;
            border: 1px solid black;
            border-collapse: collapse;
        }
        th, td {
            border: 1px solid black;
        }
    </style>
</head>
<body>
    <table>
        <thead>
        <tr>
            <th>Seq</th>
            <th>Name</th>
            <th>Email</th>
        </tr>
        </thead>
        <tbody>
            <tr>
                <td>${sample.seq}</td>
                <td>${sample.name}</td>
                <td>${sample.email}</td>
            </tr>
        </tbody>
    </table>
</body>
</html>