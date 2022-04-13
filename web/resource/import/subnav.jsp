<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
<script src="/resource/js/all.js"></script>
    <style>
        h4{
            font-weight: bolder;
            margin-top: auto !important;
            margin-bottom: auto !important;
        }
    </style>
</head>


<div class="d-none d-md-inline-block custom-subnav">
<div class="d-flex flex-column flex-shrink-0 p-3 bg-light custom-full">
<c:choose>
    <c:when test="${param.location eq 'group'}">
        <a href="/group" class="d-flex text-center my-2 mx-auto link-dark text-decoration-none">
          <h4><i class="fa-solid fa-users"></i> Group</h4>
        </a>
        <hr>
        <ul class="sidebar-nav flex-column mb-auto">
            <li><a href="/group"><i class="fa-solid fa-caret-right me-1"></i>내 그룹</a></li>
            <li><a href="/group/joinsearch"><i class="fa-solid fa-caret-right me-1"></i>그룹 탐색</a></li>
            <li><a href="/group/create"><i class="fa-solid fa-caret-right me-1"></i>그룹 작성</a></li>
<%--            <c:if test="${param.admin eq true}">
                <hr>
                <li><a href="/group/admin">관리자 메뉴</a></li>
            </c:if>--%>
        </ul>
    </c:when>
    <c:when test="${param.location eq 'todo'}">
        <a href="/todo" class="d-flex text-center my-2 mx-auto link-dark text-decoration-none">
            <h4><i class="fa-solid fa-seedling "></i> Todo</h4>
        </a>
        <hr>
        <ul class="sidebar-nav flex-column mb-auto">
            <li><a href="/todo/grouplist"><i class="fa-solid fa-caret-right me-1"></i>그룹 목표</a></li>
            <li><a href="/todo/dailylist"><i class="fa-solid fa-caret-right me-1"></i>오늘의 목표</a></li>
            <li><a href="/todo/addtask"><i class="fa-solid fa-caret-right me-1"></i>목표 작성</a></li>
        </ul>
    </c:when>
    <c:otherwise>
        <a href="/"  class="d-flex text-center my-2 mx-auto link-dark text-decoration-none">
            <h4><i class="fa-solid fa-house"></i> Main</h4>
        </a>
        <hr>
        <ul class="sidebar-nav flex-column mb-auto">
            <li><a href="/group"><i class="fa-solid fa-caret-right me-1"></i>그룹</a></li>
            <li><a href="/todo"><i class="fa-solid fa-caret-right me-1"></i>TODO</a></li>
        </ul>
    </c:otherwise>
</c:choose>



    <hr>
    <div class="dropdown">
        <c:choose>
        <c:when test="${not empty sessionScope.userNo}">
            <a href="#" class="d-flex align-items-center link-dark text-decoration-none dropdown-toggle" id="dropdownUser2" data-bs-toggle="dropdown" aria-expanded="false">
                <img src="${sessionScope.profImgThumb}" alt="" width="32" height="32" class="rounded-circle me-2">
                <strong>${sessionScope.userId}</strong></a>
            <ul class="dropdown-menu text-small shadow" aria-labelledby="dropdownUser2">
                <li><a class="dropdown-item" href="#">회원정보</a></li>
                <li><a class="dropdown-item" href="#">Profile</a></li>
                <li><hr class="dropdown-divider"></li>
                <li><a class="dropdown-item" href="/logout">로그아웃</a></li>
        </c:when>
            <c:otherwise>
                <a href="#" class="d-flex align-items-center link-dark text-decoration-none dropdown-toggle" id="dropdownUser2" data-bs-toggle="dropdown" aria-expanded="false">
                <img src="/img/default.png" alt="" width="32" height="32" class="rounded-circle me-2">
                    <strong>로그인필요</strong></a>
                <ul class="dropdown-menu text-small shadow" aria-labelledby="dropdownUser2">
                    <li><a class="dropdown-item" href="/login">로그인</a></li>
                    <li><a class="dropdown-item" href="/user/join">회원가입</a></li>
                </ul>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</div>

