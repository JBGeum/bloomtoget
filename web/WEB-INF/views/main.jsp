<meta name="viewport" content="width=device-width, initial-scale=1">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>메인</title>
    <link rel="icon" href="/img/favicon.ico" />
    <!-- Bootstrap core CSS -->
    <script src="/resource/js/jquery-3.6.0.min.js"></script>
    <script src="/resource/js/bootstrap.bundle.js"></script>
    <link href="/resource/css/bootstrap.css" rel="stylesheet">
    <link href="/resource/css/custom.css" rel="stylesheet">
</head>
<body>

<c:import url="/resource/import/nav.jsp" />
<div class="container-fluid custom-contents">
    <c:import url="/resource/import/subnav.jsp">
        <c:param name="location" value="main" />
    </c:import>
    <div class="custom-main">
        <c:choose>
            <c:when test="${empty user}">
                <a href="/login"> 로그인화면으로</a>
            </c:when>
            <c:otherwise>
                sessionScope.userNo : ${sessionScope.userNo} <br>
                userNo : ${userNo}<br>
               ${user.userName}님 환영합니다.
            </c:otherwise>
        </c:choose>

    </div>
</div>
</body>
</html>
