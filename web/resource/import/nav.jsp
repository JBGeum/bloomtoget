<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <script src="/resource/js/nav.js"></script>
    <title></title>
    <style>
        .button-group{
            padding: 0.5rem;
        }
    </style>
</head>
<body>
<nav class="navbar navbar-expand-md navbar-dark bg-primary">
    <div class="container-fluid p-2">
        <a class="navbar-brand" href="/main">BLOOMTOGET</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarColor01" aria-controls="navbarColor01" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarColor01">
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link active ps-2" href="/main">Main
                        <span class="visually-hidden">(current)</span>
                    </a>
                </li>
                <c:choose>
                    <c:when test="${not empty sessionScope.userNo}">
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle ps-2" data-bs-toggle="dropdown" href="/group/mygroup" role="button" aria-haspopup="true" aria-expanded="false">Group</a>
                            <div class="dropdown-menu">
                                <a class="dropdown-item" href="/group">내 그룹</a>
                                <a class="dropdown-item" href="/group/joinsearch">그룹 탐색</a>
                                <a class="dropdown-item" href="/group/create">그룹 작성</a>
                            </div>
                        </li>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle ps-2" data-bs-toggle="dropdown" href="/todolist" role="button" aria-haspopup="true" aria-expanded="false">Todo</a>
                            <div class="dropdown-menu">
                                <a class="dropdown-item" href="/todo/grouplist">그룹 목표</a>
                                <a class="dropdown-item" href="/todo/dailylist">오늘의 목표</a>
                                <a class="dropdown-item" href="/todo/grouplist">목표 작성</a>
                            </div>
                        </li>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle ps-2" data-bs-toggle="dropdown" href="/user/userinfo" role="button" aria-haspopup="true" aria-expanded="false">User</a>
                            <div class="dropdown-menu">
                                <a class="dropdown-item disabled" href="#">회원 정보</a>
                                <a class="dropdown-item disabled" href="#">내 프로필</a>
                            </div>
                        </li>
                <%--<li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle ps-2" data-bs-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">-</a>
                    <div class="dropdown-menu">
                        <a class="dropdown-item" href="#">Action</a>
                        <a class="dropdown-item" href="#">Another action</a>
                        <a class="dropdown-item" href="#">Something else here</a>
                        <div class="dropdown-divider"></div>
                        <a class="dropdown-item" href="#">Separated link</a>
                    </div>
                </li>--%>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item">
                            <a class="nav-link ps-2" href="#">About</a>
                        </li>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle ps-2" data-bs-toggle="dropdown" href="/group/joinsearch" role="button" aria-haspopup="true" aria-expanded="false">Group</a>
                            <div class="dropdown-menu">
                                <a class="dropdown-item" href="/group/joinsearch">그룹 탐색</a>
                            </div>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>


            <div class="button-group text-end">
                <c:choose>
                    <c:when test="${not empty sessionScope.userNo}">
                        <button type="button" class="btn btn-warning" onclick="goLogout();">로그아웃</button>
                    </c:when>
                    <c:otherwise>
                        <div class="btn-group" role="group">
                            <button type="button" class="btn btn-secondary" onclick="goLogin();">로그인</button>
                            <button type="button" class="btn btn-warning" onclick="goJoin();">가입</button>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

        </div>




    </div>
</nav>
</body>
</html>
