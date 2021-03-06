<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>

    <link rel="icon" href="/img/favicon.ico" />
    <script src="/resource/js/all.js"></script>
    <!-- Bootstrap core CSS -->
    <script src="/resource/js/jquery-3.6.0.min.js"></script>
    <script src="/resource/js/bootstrap.bundle.js"></script>
    <link href="/resource/css/bootstrap.css" rel="stylesheet">
    <link href="/resource/css/custom.css" rel="stylesheet">
    <script>
        $(function () {
            $('#formBtn').on("click", function () {
                if (confirm('새 TODO 목표를 만드시겠습니까?')) {
                    $('#addTask').submit();
                } else {
                    return false;
                }
            });
        });
    </script>
</head>
<body>

<c:import url="/resource/import/nav.jsp" />
<div class="container-fluid custom-contents">
    <c:import url="/resource/import/subnav.jsp">
        <c:param name="location" value="todo" />
    </c:import>

    <div class="custom-main">
        <div class="col-md-9">
            <div class="card border-primary mb-3" style="max-width: 800px;">
                <div class="card-header">
                    <h4 class="card-title my-2">새로운 TODO 작성</h4>
                </div>
                <div class="card-body">
                    <form action="/todo/addtask" id="addTask" method="post">
                        <input type="hidden" name="tdt_writer" value="${sessionScope.userNo}">
                        <div class="row">
                            <div class="col-sm-3 my-auto">
                                <strong class="mb-0">작성자</strong>
                            </div>
                            <div class="col-sm-9">
                                ${sessionScope.userId}
                            </div>
                        </div>
                        <hr>
                        <div class="row">
                            <div class="col-sm-3 my-auto">
                                <strong class="mb-0">달성 목표</strong>
                            </div>
                            <div class="col-sm-9">
                                <textarea name="tdt_goal" class="form-control my-auto" rows="3"></textarea>
                            </div>
                        </div>
                        <hr>
                        <div class="row mb-3">
                            <div class="col-sm-3">
                                <strong class="my-auto">활성화</strong>
                            </div>
                            <div class="col-sm-9">
                                <label class="form-check-label me-3">
                                    <input type="radio" class="form-check-input" name="tdt_active" value="1">
                                    네</label>
                                <label class="form-check-label me-3">
                                    <input type="radio" class="form-check-input" name="tdt_active" value="0">
                                    아니오</label>
                            </div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-sm-3">
                                <strong class="my-auto">공개 여부</strong>
                            </div>
                            <div class="col-sm-9">
                                <label class="form-check-label me-3">
                                    <input type="radio" class="form-check-input" name="tdt_public" value="1">
                                    네</label>
                                <label class="form-check-label me-3">
                                    <input type="radio" class="form-check-input" name="tdt_public" value="0">
                                    아니오</label>
                            </div>
                        </div>
                        <hr>
                        <div class="text-center">
                            <button type="button" class="btn btn-primary" id="formBtn">생성</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
