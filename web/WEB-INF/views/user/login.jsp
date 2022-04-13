<meta name="viewport" content="width=device-width, initial-scale=1">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>로그인</title>
    <link rel="icon" href="/img/favicon.ico" />
    <!-- Bootstrap core CSS -->
    <script src="/resource/js/jquery-3.6.0.min.js"></script>
    <script src="/resource/js/bootstrap.bundle.js"></script>
    <link href="/resource/css/bootstrap.css" rel="stylesheet">
    <link href="/resource/css/signin.css" rel="stylesheet">

</head>
<body class="text-center">

<c:import url="/resource/import/nav.jsp" />

<main class="form-signin">
<c:choose>
    <c:when test="${alreadyLoggedIn eq true}">
        이미 로그인 되어 있습니다.<br>
        <div class="btn-group" role="group">
            <button type="button" class="btn btn-primary" onclick="goMain();">메인으로</button>
            <button type="button" class="btn btn-warning" onclick="goLogout();">로그아웃</button>
        </div>
    </c:when>
    <c:otherwise>
        <c:if test="${loginResult eq false}">
            로그인에 실패했습니다.
        </c:if>
    <form action="/login" method="post">
        <h1 class="h3 mb-3 fw-normal">로그인</h1>
        <div class="form-floating">
            <input type="text" name="userId" class="form-control" id="userId" placeholder="Id" value="user1" >
            <label for="userId">아이디</label>
        </div>
        <div class="form-floating">
            <input type="password" name="userPwd" class="form-control" id="userPwd" placeholder="Password" value="1234">
            <label for="userPwd">비밀번호</label>
        </div>
        <div class="checkbox mb-3">
                <%--<label>
                    TODO : 아이디 기억 기능 가능할지
                    <input type="checkbox" value="remember-me"> Remember me
            </label>--%>
        </div>
        <button class="w-100 btn btn-lg btn-primary" type="submit">로그인</button>
    </form>
    </c:otherwise>
</c:choose>
</main>
</body>
</html>
