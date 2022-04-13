
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>Title</title>
</head>
<body>
<script type="text/javascript" src="/resource/js/jquery-3.6.0.min.js"></script>
<c:choose>
    <c:when test="${status eq 'joinCompleted'}">
        <script>
            alert("참가 완료!");
            document.location.href="/todo/task/${tdtNo}";
        </script>
    </c:when>
    <c:when test="${status eq 'quitCompleted'}">
        <script>
            alert("참여를 취소했습니다.");
            document.location.href="/todo/task/${tdtNo}";
        </script>
    </c:when>
    <c:when test="${status eq 'inserted'}">
        <script>
            alert("새 TODO 목표가 생성되었습니다.");
            document.location.href="/todo/task/${tdtNo}";
        </script>
    </c:when>
    <c:when test="${status eq 'editfalse'}">
        <script>
            alert("수정할 수 없습니다.");
            document.location.href="/todo/task/${tdtNo}";
        </script>
    </c:when>
    <c:otherwise>
        <script>
        alert("조건이 존재하지 않습니다.");
        </script>
    </c:otherwise>
</c:choose>

</body>
</html>
