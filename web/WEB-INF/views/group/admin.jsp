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
        ${sessionScope.userNo} , ${sessionScope.userId} 로 로그인 중 <br>
        <c:choose>
            <c:when test="${group ne null}">
                소속 그룹 정보<br>
                ${group.toString()}
                <hr>
                <h3>그룹 메뉴</h3>
                <a href="/group/${group.groupNo}">내 그룹 정보</a><br>
                <c:choose>
                    <c:when test="${sessionScope.userNo eq group.groupAdmin}">
                        <b>${group.groupName}의 관리자 메뉴</b><br>
                        <a href="/group/edit/${group.groupNo}"><button>그룹 수정</button></a>
                        <a href="/group/changeadmin/${group.groupNo}"><button>관리자 위임</button></a>
                        <a href="/group/joinrequest/${group.groupNo}"><button>가입 신청 확인</button></a>
                    </c:when>
                    <c:otherwise>
                        <strong>${group.groupName}의 회원 메뉴</strong><br>
                        <a href="javascript:void(0)" onClick="quitGroup()"><button>그룹 탈퇴</button></a>

                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:otherwise>
                <% response.sendRedirect("/falseaccess");%>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
