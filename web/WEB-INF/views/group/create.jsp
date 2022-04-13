
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>

    <link rel="icon" href="/img/favicon.ico" />
    <script src="/resource/js/all.js"></script>
    <!-- Bootstrap core CSS -->
    <script src="/resource/js/jquery-3.6.0.min.js"></script>
    <script src="/resource/js/bootstrap.bundle.js"></script>
    <link href="/resource/css/bootstrap.css" rel="stylesheet">
    <link href="/resource/css/custom.css" rel="stylesheet">

    <script>
        $(function () {
            $('#groupCreateBtn').on("click", function () {
                if (confirm('새 그룹을 만드시겠습니까?')) {
                    $('#groupCreate').submit();
                } else {
                    return false;
                }
            });
        });
    </script>
</head>
<body>
<c:import url="/resource/import/nav.jsp" />
<div class="container-fluid custom-contents">
    <c:import url="/resource/import/subnav.jsp">
        <c:param name="location" value="group" />
    </c:import>
    <div class="custom-main">
        <c:choose>
            <c:when test="${status eq 'available'}">
            <div class="col-md-9">
                <div class="card border-primary mb-3" style="max-width: 800px;">
                    <div class="card-header">
                        <h4 class="card-title my-2">새 그룹 만들기</h4>
                    </div>
                    <div class="card-body">
                        <form action="/group/create" id="groupCreate" method="post">
                            <div class="row">
                                <div class="col-sm-3 my-auto">
                                    <strong class="mb-0">그룹 이름</strong>
                                </div>
                                <div class="col-sm-9">
                                    <input type="text" class="form-control" name="groupName" id="groupName" required>
                                </div>
                            </div>
                            <hr>
                            <div class="row mb-3">
                                <div class="col-sm-3">
                                    <strong class="my-auto">공개 여부</strong>
                                </div>
                                <div class="col-sm-9">
                                    <label class="form-check-label me-3">
                                        <input type="radio" class="form-check-input" name="groupPublic" id="gp1" value="1"> 공개</label>
                                    <label class="form-check-label ms-3">
                                    <input type="radio" class="form-check-input" name="groupPublic" id="gp0" value="0"> 비공개</label>
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
                                    <textarea name="groupDesc" class="form-control my-auto" rows="6" id="groupDesc"></textarea>
                                </div>
                            </div>
                            <div class="text-center">
                                <input type="hidden" name="groupAdmin" id="groupAdmin" value="${sessionScope.userNo}">
                                <button type="button" class="btn btn-primary" id="groupCreateBtn">생성</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            </c:when>
            <c:when test="${status eq 'joinRequesting'}">
                현재 다른 그룹에 가입 신청 중이기 때문에 새로운 그룹을 생성할 수 없습니다.<br>
                새로운 그룹을 만들기 위해서는 그룹에 가입 신청 중이거나 소속되지 않은 상태여야 합니다.<br>
            </c:when>
            <c:when test="${status eq 'alreadyMember'}">
                현재 그룹에 소속중인 멤버이기 때문에 새로운 그룹을 생성할 수 없습니다.<br>
                새로운 그룹을 만들기 위해서는 그룹에 가입 신청 중이거나 소속되지 않은 상태여야 합니다.<br>
            </c:when>
            <c:otherwise>
                기타 오류
            </c:otherwise>
        </c:choose>
</body>
</html>