<%--
  Created by IntelliJ IDEA.
  User: ivt
  Date: 2022-02-12
  Time: 오후 9:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>회원 가입</title>
    <link rel="icon" href="/img/favicon.ico" />
    <!-- Bootstrap core CSS -->
    <script src="/resource/js/jquery-3.6.0.min.js"></script>
    <script src="/resource/js/bootstrap.bundle.js"></script>
    <link href="/resource/css/bootstrap.css" rel="stylesheet">
    <link href="/resource/css/custom.css" rel="stylesheet">
<style>
    input::placeholder{
        text-align: center;
        font-size:0.8rem;
    }
    .input-form {
        width: 800px;
        margin: auto;

        padding: 32px;
        background: #fff;
        -webkit-border-radius: 10px;
        -moz-border-radius: 10px;
        border-radius: 10px;
        -webkit-box-shadow: 0 8px 20px 0 rgba(0, 0, 0, 0.15);
        -moz-box-shadow: 0 8px 20px 0 rgba(0, 0, 0, 0.15);
        box-shadow: 0 8px 20px 0 rgba(0, 0, 0, 0.15)
    }
    .form-control{
        width:30%;
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
    .form-label{
        text-align:center;
    }


</style>
    <script>
        //TODO : jquery로 수정하기
        function idUniqueCheck(){
            document.querySelector("#userId").value="";
            window.open("/user/idCheck","","width=500,height=300");
        }
        function getId(userId){
            document.querySelector("#userId").value=userId;
            document.getElementById("userId").backgroundColor = "gray";
            document.getElementById("userId").onclick = null;
        }
        //TODO : alert 창을 modal로 표시
        function signupRequest(){
            let name=document.querySelector("#userName").value;
            if(name.trim()==""){
                alert("이름을 입력해 주세요.");
                return false;
            }
            let id=document.querySelector("#userId").value;
            if(id.trim()==""){
                alert("아이디는 필수입력 항목입니다.");
                return false;
            }
            let password=document.querySelector("#userPwd").value;
            if(password.trim()==""){
                alert("패스워드는 필수입력 항목입니다.");
                return false;
            }
            let password2=document.querySelector("#userPwd2").value;
            if(password2.trim()==""){
                alert("패스워드를 다시 입력해 주세요.");
                return false;
            }
            if(password!=password2){
                alert("패스워드는 동일해야 합니다.");
                return false;
            }

            let email1 = document.querySelector("#email1").value;
            let email2 = document.querySelector("#email2").value;
            if(email2.trim()==""){
                alert("이메일 도메인을 선택해주세요.");
                return false;
            }
            let email = document.querySelector("#email");
            email.value=email1+"@"+email2;
            return true;
        }
    </script>
</head>
<body>
<c:import url="/resource/import/nav.jsp" />
<div class="container-fluid custom-contents align-items-center">
        <main class="m-auto">
            <div class="py-5">
                <div class="input-form">
        <c:choose>
            <c:when test="${empty sessionScope.userNo}">
                <h1 class="h3 mb-3 fw-normal text-center">회원 가입</h1>
                <hr>
                <form id="joinForm" onsubmit="return signupRequest()" action="/user/join" method="post" enctype="multipart/form-data">

                    <label class="form-label">ID</label>

                    <div class="input-group" style="width:calc(30% + 5.5rem);">
                        <input type="text" class="form-control" name="userId" id="userId" onclick="idUniqueCheck()" readonly/>
                        <button type="button" class="btn btn-primary" onclick="idUniqueCheck()">중복검사</button>
                    </div><br>
                    <label class="form-label">PassWord</label>
                    <input type="text" class="form-control" name="userPwd" id="userPwd"/><br>
                    <label class="form-label">PassWord2</label>
                    <input type="text" class="form-control" name="userPwd2" id="userPwd2"/>
                    <div class="pwdcheck"><br></div>
                    <label class="form-label">닉네임</label>
                    <input type="text" class="form-control" name="userName" id="userName"/><br>
                    <label class="form-label">E-mail</label>
                    <input type="text" class="form-control inline" id="email1" /> @
                    <input type="text" class="form-control inline" id="email2" list="emails" placeholder="입력 또는 선택해주세요">
                    <datalist id="emails">
                        <option>naver.com</option>
                        <option>daum.net</option>
                        <option>gmail.com</option>
                    </datalist>
                    <input type="hidden" name="email" id="email"/><br><br>
                    <label class="form-label">프로필 사진 :</label>
                    <input type="file" class="form-control" id="profImg" name="file" accept=".jpg, .jpeg, .png" style="width:50%"/>
                </form>

                <div class="select_img"><img src="" /></div>
                <div class="text-center mt-4">
                    <button type="submit" class="btn btn-secondary" form="joinForm">등록</button><br>
                </div>
                <br>


<%--                <%=request.getRealPath("/")%><br>--%>
            </c:when>
            <c:otherwise>
                로그인 된 상태입니다.<br>
                (가입된 유저)
            </c:otherwise>
        </c:choose>
                </div>
            </div>
        </main>
    </div>


<script>
    $("#profImg").change(function(){
        if(this.files && this.files[0]) {
            var reader = new FileReader;
            reader.onload = function(data) {
                $(".select_img img").attr("src", data.target.result).width(300);
            }
            reader.readAsDataURL(this.files[0]);
        }
    });

    $("#userPwd, #userPwd2").change(function(){
        var pwd = $("#userPwd").val();
        var pwd2 = $("#userPwd2").val();
        if(pwd.trim() != "" && pwd2.trim() != ""){
            if(pwd == pwd2){
                $("#userPwd2").addClass("is-valid").removeClass("is-invalid");
                $(".pwdcheck").html("패스워드 일치");
            } else {
                $("#userPwd2").addClass("is-invalid").removeClass("is-valid");
                $(".pwdcheck").html("패스워드 불일치!");
            }
        }
    });
</script>


</body>
</html>
