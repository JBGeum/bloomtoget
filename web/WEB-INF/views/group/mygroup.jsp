<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="icon" href="/img/favicon.ico" />
    <!-- Bootstrap core CSS -->
    <script src="/resource/js/jquery-3.6.0.min.js"></script>
    <script src="/resource/js/all.js"></script>
    <script src="/resource/js/bootstrap.bundle.js"></script>
    <link href="/resource/css/bootstrap.css" rel="stylesheet">
    <link href="/resource/css/custom.css" rel="stylesheet">

    <script>
        function quitGroup(){
            if(confirm("정말로 그룹에서 탈퇴하시겠습니까?")){
                let f = document.createElement('form');
                f.setAttribute('method', 'post');
                f.setAttribute('action', '/group/quit/${sessionScope.userNo}');
                document.body.appendChild(f);
                f.submit();
            } else {
                 alert("취소하셨습니다.")
            }
        }
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
        <c:choose>
            <c:when test="${group ne null}">
            <div class="col-md-9">
                <div class="card border-primary mb-3" style="max-width: 800px;">
                    <div class="card-header">
                        <h4 class="card-title my-2">내 그룹 정보</h4>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-sm-3">
                                <strong class="mb-0">그룹명</strong>
                            </div>
                            <div class="col-sm-9">
                                    ${group.groupName}
                                        <a href="/group/${group.groupNo}"><i class="fa-solid fa-angles-right link-secondary"></i></a>
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
                                        비공개 <i class="fa-solid fa-lock"></i>
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
                <div>
                <c:choose>
                    <c:when test="${sessionScope.userNo eq group.groupAdmin}">
                    <h4 class="mb-2"><i class="fa-solid fa-crown"></i> ${group.groupName}의 관리자 메뉴</h4>
                        <div class="btn-group" role="group">
                            <button type="button" class="btn btn-primary" onclick="location.href='/group/edit/${group.groupNo};'">그룹 수정</button>
                            <button type="button" class="btn btn-warning" onclick="location.href='/group/changeadmin/${group.groupNo};'">관리자 위임</button>
                            <button type="button" class="btn btn-info" onclick="location.href='/group/joinrequest/${group.groupNo};'">가입 신청 확인</button>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <h4 class="mb-2"><i class="fa-solid fa-user-large"></i> ${group.groupName}의 회원 메뉴</h4>
                        <button type="button" class="btn btn-danger" onclick="quitGroup()">그룹 탈퇴</button>
                    </c:otherwise>
                </c:choose>
                    </div>
                <br>
            </c:when>
                <c:when test="${status eq 'requestProcessing'}">
                    현재 그룹에 가입 신청중인 상태입니다.<br>
                    그룹 관리자가 신청을 확인할 때까지 기다려 주세요.<br>
                    <a href="/group/${groupNo}">신청 중인 그룹</a>
                    <form action="/group/stopjoinrequest" method="post">
                        <input type="hidden" name="groupNo" value="${groupNo}">
                        <button type="submit">신청 취소</button>
                    </form>
                </c:when>
            <c:otherwise>
                오류 발생 <br>
                 response.sendRedirect("/falseaccess");
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
