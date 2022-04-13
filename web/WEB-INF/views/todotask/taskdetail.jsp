<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <c:set var="now" value="<%=new java.util.Date()%>" />
<style>
    td{
        min-width:100px;
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
        <c:choose>
            <c:when test="${status eq 'private'}">
                확인 불가능한 목표입니다.<br>
                권한을 확인해주세요.
            </c:when>
            <c:when test="${status eq 'readable'}">
                <!--TODO : 작성자의 tdt_writterName,groupName 이름 포함 불러오기-->
                <div class="row">
                    <div class="col-9 p-3">
                        <div class="tb-border">
                            <table class="table table-light table-striped mb-0">
                                <tr class="table-primary">
                                    <th>No. ${tdt.tdt_No} </th>
                                    <td colspan="4"></td>
                                    <td class="text-end">
                                        <c:choose>
                                            <c:when test="${tdt.tdt_writer eq sessionScope.userNo}">
                                                <button class="btn btn-light text-primary" onclick="location.href='/todo/edittask/${tdt.tdt_No}'">목표수정</button>
                                            </c:when>
                                            <c:otherwise>
                                                <i class="fa-solid fa-seedling"></i>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                                <tr>
                                    <th>목표</th>
                                    <td colspan="3">${tdt.tdt_goal}</td>
                                    <th>작성자</th>
                                    <td>${tdt.writerName}</td>
                                </tr>
                                <tr>
                                    <th>상태</th>
                                    <td>
                                        <c:choose>
                                            <c:when test="${tdt.tdt_active eq '1'}">
                                                <i class="fa-solid fa-circle-play link-secondary"></i> 진행 중
                                            </c:when>
                                            <c:when test="${tdt.tdt_active eq '0'}">
                                                <i class="fa-solid fa-circle-stop link-secondary"></i> 비활성화
                                            </c:when>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${tdt.tdt_public eq '1'}">
                                                <i class="fa-solid fa-door-open"></i> 공개
                                            </c:when>
                                            <c:when test="${tdt.tdt_public eq '0'}">
                                                <i class="fa-solid fa-lock"></i> 비공개
                                            </c:when>
                                        </c:choose>
                                    </td>
                                    <td></td>
                                    <th>그룹</th>
                                    <td>${tdt.tdt_groupNo}</td>
                                </tr>
                                <tr class="table-primary text-center">
                                    <th colspan="6">참가자</th>
                                </tr>
                                <tr>
                                    <th>현재 시간</th>
                                    <td colspan="3"><fmt:formatDate value="${now}" pattern="yy.MM.dd a hh:mm:ss" /></td>
                                    <th>참가인수</th>
                                    <td>${members.size()} 명</td>
                                </tr>
                                <tr class="table-primary text-center">
                                    <th colspan="2">아이디</th>
                                    <th colspan="2">닉네임</th>
                                    <th colspan="2">시작일자</th>
                                </tr>
                                <c:forEach var="member" items="${members}" varStatus="status">
                                    <tr class="text-center">
                                        <td colspan="2"><c:out value="${member.userId}"/></td>
                                        <td colspan="2"><c:out value="${member.userName}"/></td>
                                        <td colspan="2"><fmt:formatDate value="${member.utStartDate}" pattern="yy.MM.dd"/></td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </div>
                        <form name="userTd"  method="post">
                            <input type="hidden" name="userNo" value="${sessionScope.userNo}">
                            <input type="hidden" name="groupNo" value="${tdt.tdt_groupNo}">
                            <input type="hidden" name="tdtNo" value="${tdt.tdt_No}">
                        </form>
                            <%--TODO 현재 접속중인 멤버가 tdt_groupNo의 그룹 소속인지 확인할 것--%>
                        <div class="col-12 p-3 text-center">
                            <c:choose>
                                <c:when test="${alreadyMember eq 'false'}">
                                    <p class="fw-bold">현재 목표에 참가중이 아닙니다.</p>
                                    <button class="btn btn-success" onclick="utSubmit('join')">함께하기</button>
                                </c:when>
                                <c:when test="${alreadyMember eq 'true'}">
                                    <p class="text-secondary fw-bold">현재 목표에 참가중입니다.</p>
                                    <button class="btn btn-warning" onclick="utSubmit('quit')">그만두기</button>
                                </c:when>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </c:when>
        </c:choose>
    </div>
</div>
<script>
    function utSubmit(mode){
        var form = document.userTd;
        if(mode==="join"){
            if(confirm('이 목표에 정말로 참가하시겠습니까?')){
                form.action="/todo/task/join";
            } else {
                alert('참가를 취소하셨습니다.');
                return false;
            }
        } else if (mode === "quit"){
            if(confirm('이 목표에 참여하는 것을 정말로 중지하시겠습니까?')) {
                form.action = "/todo/task/quit";
            } else {
                alert('참여중지를 취소하셨습니다.');
                return false;
            }

        } else {
            alert("오류 발생!");
            return false;
        }
        form.submit();
    }
</script>
</body>
</html>
