<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Title</title>
    <link rel="icon" href="/img/favicon.ico"/>
    <!-- Bootstrap core CSS -->
    <script src="/resource/js/jquery-3.6.0.min.js"></script>
    <script src="/resource/js/bootstrap.bundle.js"></script>
    <link href="/resource/css/bootstrap.css" rel="stylesheet">
    <link href="/resource/css/custom.css" rel="stylesheet">

</head>
<body>
<c:import url="/resource/import/nav.jsp"/>
<div class="container-fluid custom-contents">
    <c:import url="/resource/import/subnav.jsp">
            <c:param name="location" value="group"/>
    </c:import>

    <div class="custom-main">
        <div class="card border-primary mb-5" style="max-width: 800px;">
            <div class="card-header">
                <h4 class="card-title my-2">가입 대기 회원 목록</h4>
            </div>
            <div class="card-body">
                <form name="joinForm" method="post">
                    <input type="hidden" name="groupNo" value="${groupNo}">
                    <c:choose>
                        <c:when test="${empty users}">
                            현재 가입 대기중인 회원이 없습니다.
                        </c:when>
                        <c:when test="${currentMemberNum >= 6}">
                            <c:forEach var="user" items="${users}" varStatus="status">
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
                                        3
                                    </div>
                                </div>
                                <hr>
                            </c:forEach>
                            <div class="row">
                                <div class="col-xs-12 text-center">
                                    <strong class="text-secondary">현재 그룹 인원수가 최대(6명)입니다.</strong><br>
                                    <div class="mt-3">
                                        <button type="button" class="btn btn-danger" onClick="allowJoin('falseAll')">일괄 승인 거절
                                        </button>
                                        <button type="button" class="btn btn-warning" onclick="history.back();">취소</button>
                                    </div>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="user" items="${users}" varStatus="status">
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
                                        <input type="radio" name="userNo" value="${user.userNo}">
                                    </div>
                                </div>
                            <hr>
                        </c:forEach>
                            <div class="row">
                                <div class="col-xs-12 text-center">
                                    현재 인원수 :<strong class="text-secondary">  ${currentMemberNum} 명 </strong> (최대 6명)<br>
                                    <div class="mt-3">
                                        <div class="btn-group" role="group">
                                            <button type="button" class="btn btn-success" onclick="allowJoin('true')">가입 승인</button>
                                            <button type="button" class="btn btn-warning" onclick="allowJoin('false')"> 가입 거절</button>
                                            <button type="button" class="btn btn-danger" onClick="allowJoin('falseAll')">일괄 승인 거절</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </form>
            </div>
        </div>
        <div class="text-center">
            <button type="button" class="btn btn-warning" onclick="history.back();">취소</button>
        </div>
</div>

<script>
    function allowJoin(mode) {
        var form = document.joinForm;
        if (mode == "falseAll") {
            if (confirm("현재 신청중인 전원의 가입 신청을 정말로 거절하시겠습니까?")) {
                if (!form.userNo) {   //userNo 생성X일때(컨트롤러와 동시에 삭제가능한가?
                    var nullNo = document.createElement("input");
                    nullNo.setAttribute("type", "hidden");
                    nullNo.setAttribute("name", "userNo");
                    nullNo.setAttribute("value", "0");
                    form.appendChild(nullNo);
                } else {
                    form.userNo.value = 0;
                }
                form.action = "/group/declinejoin/all";
            } else
                return false;
        } else if (mode == "true" && userNoChecked()) {
            if (confirm("해당 회원의 가입 신청을 정말로 승인하시겠습니까?")) {
                form.action = "/group/allowjoin/user";
            } else
                return false;
        } else if (mode == "false" && userNoChecked()) {
            if (confirm("해당 회원의 가입 신청을 정말로 거절하시겠습니까?")) {
                form.action = "/group/declinejoin/user";
            } else
                return false;
        } else if (!userNoChecked()) {
            alert("회원에 체크해주세요!");
            return false;
        } else {
            alert("오류가 발생했습니다. 다시 한번 확인해주세요");
            return false;
        }
        alert("전송합니다.")
        form.submit();
    }

    function userNoChecked() {
        var radios = document.getElementsByName("userNo");
        var valid = false;
        var i = 0;
        while (!valid && i < radios.length) {
            if (radios[i].checked) valid = true;
            i++;
        }
        if (!valid) alert("회원을 선택해 주세요");
        return valid;
    }
</script>
</body>
</html>
