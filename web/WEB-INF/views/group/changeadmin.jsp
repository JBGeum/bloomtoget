
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
        $(function(){
            $('#submitBtn').on("click",function(){
                var adminNo = $("#changeAdmin [name='groupAdmin']:checked").val();
                if(adminNo === undefined){
                    alert('새로운 관리자를 선택해 주세요!')
                } else {
                    if(confirm('해당 사용자로 관리자를 바꾸시겠습니까?')){
                        $('#changeAdmin').submit();
                    } else{
                        return false;
                    }
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

<%--TODO 각 회원 userProfilie 페이지로 연결        --%>
        <div class="card border-primary mb-5" style="max-width: 800px;">
            <div class="card-header">
                <h4 class="card-title my-2">관리자 변경</h4>
            </div>
            <div class="card-body">
                <form action="/group/changeadmin/${groupNo}" id="changeAdmin" method="post">
                <c:forEach var="user" items="${groupMembers}" varStatus="status">
                    <div class="row">
                        <div class="col-9">
                    <div class="row mb-1">
                        <div class="col-sm-3">
                            <strong class="mb-0">아이디</strong>
                        </div>
                        <div class="col-sm-9">
                            <c:out value="${user.userId}"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-3">
                            <strong class="mb-0">이름</strong>
                        </div>
                        <div class="col-sm-9">
                            <c:out value="${user.userName}"/>
                        </div>
                    </div>
                        </div>
                    <div class="col-3 text-center my-auto">
                    <c:choose>
                        <c:when test="${user.userNo eq groupAdmin}">
                            <strong class="text-secondary">현재 관리자</strong>
                        </c:when>
                        <c:otherwise>
                            <input type="radio" class="form-check-input" name="groupAdmin" value="${user.userNo}">
                        </c:otherwise>
                    </c:choose>
                    </div>
                    </div>
                    <hr>
                </c:forEach>
                    <div class="row">
                        <div class="col-xs-12 text-center">
                            <input type="hidden" value="${groupNo}">
                            <button type="button" id="submitBtn" class="btn btn-primary">변경</button>
                            <button type="button" class="btn btn-warning" onclick="history.back();">취소</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
