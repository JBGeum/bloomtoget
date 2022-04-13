<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>가입 가능한 그룹 검색</title>
    <link rel="icon" href="/img/favicon.ico" />
    <!-- Bootstrap core CSS -->
    <script src="/resource/js/jquery-3.6.0.min.js"></script>
    <script src="/resource/js/bootstrap.bundle.js"></script>
    <link href="/resource/css/bootstrap.css" rel="stylesheet">
    <link href="/resource/css/custom.css" rel="stylesheet">
</head>
<body>
<c:import url="/resource/import/nav.jsp"/>
<div class="container-fluid custom-contents">
    <c:import url="/resource/import/subnav.jsp"> <c:param name="location" value="group"/> </c:import>
    <div class="custom-main">
        <div class="card border-primary mb-5" style="max-width: 800px;">
            <div class="card-header">
                <h4 class="card-title my-2">가입 가능 그룹 목록</h4>
            </div>
            <div class="card-body">

                <c:forEach var="group" items="${grouplist}" varStatus="status">
                    <div class="row">
                        <div class="col-3">
                            <strong class="mb-0">이름</strong>
                        </div>
                        <div class="col-6">
                                ${group.groupName}
                                <a href="/group/${group.groupNo}"><i class="fa-solid fa-angles-right link-secondary"></i></a>
                        </div>
                        <div class="col-3">
                                <strong class="text-success">${memberNumList.get(status.index)}</strong> /6 명
                        </div>
                    </div>
                    <hr class="my-2 bg-primary">
                    <div class="row mt-2">
                        <div class="col-md-9 px-3">
                            ${group.groupDesc}
                        </div>
                        <div class="col-md-3 text-end">
                            <button type="button" class="btn btn-primary" onClick="location.href='/group/join/${group.groupNo}'">가입신청</button>
                        </div>
                    </div>
                    <hr>
                </c:forEach>
            </div>
        </div>
        <hr>
    </div>
</div>
</body>
</html>
