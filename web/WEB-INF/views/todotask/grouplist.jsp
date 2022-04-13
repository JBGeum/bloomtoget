
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>grouplist</title>
    <link rel="icon" href="/img/favicon.ico" />
    <script src="/resource/js/all.js"></script>
    <!-- Bootstrap core CSS -->
    <script src="/resource/js/jquery-3.6.0.min.js"></script>
    <script src="/resource/js/bootstrap.bundle.js"></script>
    <link href="/resource/css/bootstrap.css" rel="stylesheet">
    <link href="/resource/css/custom.css" rel="stylesheet">
<style>
    .link-dark {
        color: #888;
    }

    table {
        width: 100%;
    }

    td {
        vertical-align: middle;
    }
</style>
</head>
<body>
<c:import url="/resource/import/nav.jsp" />
<div class="container-fluid custom-contents">
    <c:import url="/resource/import/subnav.jsp">
        <c:param name="location" value="todo" />
    </c:import>
        <div class="custom-main">
            <div class="col-md-10 p-3">
                <div class="card border-primary mb-3" style="max-width: 1000px;">
                    <div class="card-header">
                        <h4 class="card-title my-2"><i class="fa-solid fa-table-list"></i> 그룹 내 목표 리스트</h4>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-12 m-auto text-center">
                                <strong class="text-secondary">
                                <c:choose>
                                    <c:when test="${status eq 'group'}">
                                        <i class="fa-solid fa-diamond"></i> 그룹 내의 모든 목표를 표시 중
                                    </c:when>
                                    <c:when test="${status eq 'user'}">
                                        <i class="fa-solid fa-diamond"></i> 그룹에서 참가중인 목표를 표시 중
                                    </c:when>
                                    <c:when test="${status eq 'other'}">
                                        <i class="fa-solid fa-diamond"></i> 그룹의 목표 중 참가중이 아닌 목표를 표시 중
                                    </c:when>
                                </c:choose>
                                </strong>
                                <hr>
                            </div>
                        </div>
                        <div class="row align-items-center">
                            <div class="col-6">
                                <div>
                                    <a class="link-dark text-decoration-none" href="/todo/dailylist"><i
                                            class="fa-solid fa-angles-left link-info"></i> 오늘 내 목표 달성 상태 확인</a><br>
                                </div>
                            </div>
                            <div class="col-6 text-end">
                                <c:if test="${status ne 'group'}">
                                    <a class="link-dark text-decoration-none" href="/todo/grouplist">그룹 전체 목표 표시  <i class="fa-solid fa-angles-right link-secondary"></i></a> <br>
                                </c:if>
                                <c:if test="${status ne 'user'}">
                                    <a class="link-dark text-decoration-none" href="/todo/grouplist/user">참가중인 목표 표시  <i class="fa-solid fa-angles-right link-secondary"></i></a> <br>
                                </c:if>
                                <c:if test="${status ne 'other'}">
                                    <a class="link-dark text-decoration-none" href="/todo/grouplist/other">참가하지 않는 목표 표시  <i class="fa-solid fa-angles-right link-secondary"></i></a> <br>
                                </c:if>
                            </div>
                        </div>
                        <hr>
                        <div class="row">
                            <div class="col-12 p-0">
                                <table class="table table-light table-striped mb-0">
                                    <tr class="table-primary text-center">
                                        <th>작성자</th>
                                        <th colspan="2">목표내용</th>
                                        <th>인원</th>
                                        <th>시작일</th>
                                        <th>진행중</th>
                                    </tr>
                                    <c:forEach var="tdt" items="${groupTasks}" varStatus="status">
                                        <tr class="text-center">
                                            <td><c:out value="${tdt.writerName}"/></td>
                                            <td class="text-start">
                                                <a href="/todo/task/${tdt.tdt_No}" class="link-dark text-decoration-none"><c:out value="${tdt.tdt_goal}"/></a>
                                            </td>
                                            <td class="td-arrow">
                                                <a href="/todo/task/${tdt.tdt_No}"><i class="fa-solid fa-angle-right link-primary"></i></a>
                                            </td>
                                            <td>${tdt.memberCount}명</td>
                                            <td><fmt:formatDate value="${tdt.tdt_startdate}" pattern="yy.MM.dd"/></td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${tdt.tdt_active eq '1'}">
                                                        <i class="fa-solid fa-circle-play text-info"></i>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <i class="fa-solid fa-circle-pause text-muted"></i>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
</div>
</body>
</html>
