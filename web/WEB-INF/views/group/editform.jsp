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
    <script>
        var gpstatus = '${group.groupPublic}';
        $(document).ready(function(){
            $('#gp'+ gpstatus).prop('checked', true);});
        // $("input:radio[name='groupPublic']:radio[value=gpstatus]").prop('checked', true);
    </script>
</head>
<body>
<c:import url="/resource/import/nav.jsp" />
<div class="container-fluid custom-contents">
    <c:import url="/resource/import/subnav.jsp">
            <c:param name="location" value="group" />
            <c:param name="admin" value='${sessionScope.userNo eq group.groupAdmin}'/>
        </c:import>
        <div class="custom-main">
            <div class="col-md-9">
                <div class="card border-primary mb-3" style="max-width: 800px;">
                    <div class="card-header">
                        <h4 class="card-title my-2">그룹 정보</h4>
                    </div>
                    <div class="card-body">
                        <form action="/group/edit/${group.groupNo}" id="groupEdit" method="post">
                        <div class="row">
                            <div class="col-sm-3 my-auto">
                                <strong class="mb-0">그룹명</strong>
                            </div>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="groupName" id="groupName" value="${group.groupName}" required>
                            </div>
                        </div>
                        <hr>
                        <div class="row">
                            <div class="col-sm-3">
                                <strong class="mb-0">상태</strong>
                            </div>
                            <div class="col-sm-9">
                                <label class="form-check-label me-3"><input type="radio" class="form-check-input" name="groupPublic" id="gp1" value="1"> 공개</label>
                                <label class="form-check-label ms-3"><input type="radio" class="form-check-input" name="groupPublic" id="gp0" value="0"> 비공개</label>
                            </div>
                        </div>
                        <hr class="mb-0">
                        <div class="row px-2">
                            <div class="col-sm-12 text-center bg-light p-2">
                                <strong class="mb-0">설명</strong>
                            </div>
                        </div>
                        <hr class="mt-0 mb-1">
                        <div class="row" style="min-height: 150px;">
                            <div class="col-sm-12 p-3">
                                <textarea class="form-control" name="groupDesc" id="groupDesc" rows="6">${group.groupDesc}</textarea>
                            </div>
                        </div>
                            <button type="submit" class="btn btn-primary" form="groupEdit">수정</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
</body>
</html>
