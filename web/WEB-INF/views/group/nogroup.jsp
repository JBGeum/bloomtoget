
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
            $('#stopJoinRequestBtn').on('click',function(){
                if(confirm('정말로 가입 신청을 취소하시겠습니까?') === true){
                    $('form').submit();
                } else{
                    return false;
                }
            });
        });
    </script>
</head>
<body>
<c:import url="/resource/import/nav.jsp"/>
<div class="container-fluid custom-contents">
    <c:import url="/resource/import/subnav.jsp">
        <c:param name="location" value="group"/>
    </c:import>
    <div class="custom-main">
        <c:choose>
            <c:when test="${not empty joinRequesting}">
                현재 그룹에 가입 신청중인 상태입니다.<br>
                그룹 관리자가 신청을 확인할 때까지 기다려 주세요.<br>
                <form action="/group/stopjoinrequest" method="post">
                    <input type="hidden" name="groupNo" value="${joinRequesting}">
                </form>
                <div class="m-3">
                <button type="button" class="btn btn-primary" onclick="location.href='/group/${joinRequesting}'">그룹 확인</button>
                <button type="button" class="btn btn-warning" id="stopJoinRequestBtn">신청 취소</button>
                </div>
            </c:when>
            <c:otherwise>
                소속된 그룹이 존재하지 않습니다.<br>
                <hr class="mt-3">
                <a class="link-dark fw-bold text-decoration-none" href="/group/joinsearch"><i class="fa-solid fa-magnifying-glass link-primary"></i> 가입 가능한 그룹 찾아보기 </a>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
