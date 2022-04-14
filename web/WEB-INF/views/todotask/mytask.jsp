<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>mytask</title>
    <link rel="icon" href="/img/favicon.ico" />
    <script src="/resource/js/all.js"></script>
    <!-- Bootstrap core CSS -->
    <script src="/resource/js/jquery-3.6.0.min.js"></script>
    <script src="/resource/js/bootstrap.bundle.js"></script>
    <link href="/resource/css/bootstrap.css" rel="stylesheet">
    <link href="/resource/css/custom.css" rel="stylesheet">

    <style>
        td {
            vertical-align: middle;
        }

        .td-100 {
            width: 100px;
        }
    </style>
    <script>
        function complete(tdt_No){
            document.todoRecord.tdtNo.value = tdt_No;
            document.todoRecord.submit();
        }
    </script>

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
                    <h4 class="card-title my-2"><i class="fa-solid fa-table-list"></i> 오늘의 목표 리스트</h4>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-12 p-0">
                            <p class="text-center">
                                현재 그룹과 함께 달성중인 목표 리스트입니다. <br>
                                달성한 목표에 <span class="text-success"><i class="fa-solid fa-pen-to-square"></i>완료 체크</span>를 해주세요!
                            </p>
                            <p class="text-end">
                                <c:set var="now" value="<%=new java.util.Date()%>" />
                                오늘 날짜 : <fmt:formatDate value="${now}" pattern="yy.MM.dd"/>
                            </p>
                            <hr>
                            <form name="todoRecord" action="/todo/dailycomplete" method="POST">
                                <input type="hidden" name="userNo" value="${sessionScope.userNo}">
                                <input type="hidden" name="groupNo" value="${groupNo}">
                                <input type="hidden" name="tdtNo">
                            </form>
                            <table class="table table-light table-striped mb-0 text-center">
                                <tr class="table-primary text-center">
                                    <th>번호</th>
                                    <th colspan="2">목표</th>
                            <%--    TODO 오늘 달성했는지 여부를 record 생성후 가져오기 --%>
                                    <th class="td-100">달성</th>
                            <%--    TODO 오늘 task를 달성한 인원수/참여 인원수 로 아이콘 표시 --%>
                                    <th class="td-100">달성률</th>
                                    <th class="td-100">완료체크</th>
                                </tr>
                                <c:forEach var="tdt" items="${myTasks}" varStatus="status">
                                    <tr>
                                        <td><c:out value="${tdt.tdt_No}"/></td>
                                        <td class="text-start ps-2"><a href="/todo/task/${tdt.tdt_No}" class="link-dark text-decoration-none"><c:out value="${tdt.tdt_goal}"/></a></td>
                                        <td class="td-arrow"><a href="/todo/task/${tdt.tdt_No}"><i class="fa-solid fa-angle-right link-primary"></i></a></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty tdt.tdr_No}">
                                                    <i class="fa-solid fa-clipboard-check text-success"></i>
                                                </c:when>
                                                <c:otherwise>
                                                    <i class="fa-solid fa-clipboard text-body disabled"></i>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>${tdt.today}/${tdt.total}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty tdt.tdr_No}">

                                                </c:when>
                                                <c:otherwise>
                                                    <button type="button" class="btn btn-success" onclick=complete(${tdt.tdt_No})><i class="fa-solid fa-pen-to-square"></i></button>
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