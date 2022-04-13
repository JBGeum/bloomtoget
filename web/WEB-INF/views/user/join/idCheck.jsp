<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
    //boolean 사용가능= true;//(boolean)request.getAttribute("usable");
    Boolean idExists = (Boolean)request.getAttribute("idExists");
    String userId=(String)request.getAttribute("userId");
%>
<!DOCTYPE html>
<html>
<head>
    <script src="/resource/js/jquery-3.6.0.min.js"></script>
    <script src="/resource/js/bootstrap.bundle.js"></script>
    <link href="/resource/css/bootstrap.css" rel="stylesheet">
    <link href="/resource/css/custom.css" rel="stylesheet">
    <style>
        body{
            margin : auto;
            padding : 32px;
        }

        label{
            float:left;
            margin-top:0.5rem;
            font-weight:bold;
            width:20%;
        }
        .inline{
            display: inline;
        }
    </style>

    <script>
        function useId(){
            var userId=document.querySelector("#userId").value;
            opener.getId(userId);
            window.close();
        }
    </script>
    <meta charset="UTF-8">
    <title>아이디 중복 검사</title>
</head>

<body>
<div class="text-center">
<h1 class="h3 mb-3 fw-normal">아이디 중복 검사</h1><hr>
<form action="/user/idCheck" method="post">
    <label class="form-label">ID</label>
    <div class="input-group mt-4" style="width:70%;">
    <input type="text"  class="form-control" name="userId" id="userId" value="<%=(userId==null)?"":userId%>"/>
        <button type="submit" class="btn btn-primary">중복 체크</button>
    </div>
    <br>
</form>

<c:if test="${idExists ne null}">
    <c:choose>
        <c:when test="${idExists eq false}">
            사용 가능한 id입니다.<br>
            <div class="btn-group mt-2" role="group">
                <button type="button" class="btn btn-success" onclick="useId()">사용</button>
                <button type="button" class="btn btn-warning" onclick="window.close()">취소</button>
            </div>
        </c:when>
        <c:otherwise>
            이미 사용 중인 id 입니다. <br>
            <button type="button" class="btn btn-warning mt-2" onclick="window.close()">취소</button>
        </c:otherwise>
    </c:choose>
</c:if>
<br>


</div>
</body>
</html>