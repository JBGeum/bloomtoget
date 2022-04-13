
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>Title</title>
</head>
<body>
<c:choose>
    <c:when test="${reason eq 'notAdmin'}">
        그룹의 관리자가 아닙니다.
    </c:when>
    <c:when test="${reason eq ''}">

    </c:when>
    <c:otherwise>
        기타 이유
    </c:otherwise>
</c:choose>
권한이 없습니다
<button onclick="history.back()">뒤로 가기</button>


</body>
</html>
