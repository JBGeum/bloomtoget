
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
        <c:param name="admin" value='${sessionScope.userNo eq group.groupAdmin}'/>
    </c:import>
<div class="custom-main">
    <c:choose>
        <c:when test="${status eq 'false'}">
            이 그룹은 비공개 상태입니다.
        </c:when>
        <c:when test="${status eq 'join' or status eq 'opened'}">
            <div class="col-md-9">
                <div class="card border-primary mb-3" style="max-width: 800px;">
                    <div class="card-header">
                        <h4 class="card-title my-2">그룹 정보</h4>
                    </div>
                    <div class="card-body">
                        <c:if test="${status eq 'join'}">
                            <div class="row">
                                <div class="col-sm-12">
                                  <small class="text-muted"> 현재 가입중인 그룹입니다.</small> <hr>
                                </div>
                            </div>
                        </c:if>
                        <div class="row">
                            <div class="col-sm-3">
                                <strong class="mb-0">그룹명</strong>
                            </div>
                            <div class="col-sm-9">
                                    ${group.groupName}
                            </div>
                        </div>
                        <hr>
                        <div class="row">
                            <div class="col-sm-3">
                                <strong class="mb-0">관리자</strong>
                            </div>
                            <div class="col-sm-9">
                                    ${group.groupAdmin}
                            </div>
                        </div>
                        <hr>
                        <div class="row">
                            <div class="col-sm-3">
                                <strong class="mb-0">LV</strong>
                            </div>
                            <div class="col-sm-9">
                                    ${group.groupLv}
                            </div>
                        </div>
                        <hr>
                        <div class="row">
                            <div class="col-sm-3">
                                <strong class="mb-0">상태</strong>
                            </div>
                            <div class="col-sm-9">
                                <c:choose>
                                    <c:when test="${group.groupPublic eq 0}">
                                        비공개  <i class="fa-solid fa-lock"></i>
                                    </c:when>
                                    <c:when test="${group.groupPublic eq 1}">
                                        공개 <i class="fa-solid fa-door-open"></i>
                                    </c:when>
                                    <c:otherwise>
                                        -
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <hr class="mb-0">
                        <div class="row px-2">
                            <div class="col-sm-12 text-center bg-light p-2">
                                <strong class="mb-0">설명</strong>
                            </div>
                        </div>
                        <hr class="mt-0">
                        <div class="row" style="min-height: 150px;">
                            <div class="col-sm-12 p-3">
                                    ${group.groupDesc}
                            </div>
                        </div>
                    </div>
                </div>
                <br>
        </c:when>
        <c:otherwise>
            다른 오류
        </c:otherwise>
        </c:choose>
            </div>
 </div>
</body>
</html>
