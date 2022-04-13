
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>

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
        <c:param name="location" value="group" />
    </c:import>
    <div class="custom-main">
        joinresult
        <c:choose>
            <c:when test="${joinResult eq true}">
                가입신청이 정상적으로 처리되었습니다.
            </c:when>
            <c:when test="${joinResult eq false}">
                가입 불가능한 상태였습니다.<br>
                다시 확인해 주세요.<br>
            </c:when>
        </c:choose>
        <br>
        <a href="/group">그룹 화면으로</a>
    </div>
</div>

</body>
</html>
