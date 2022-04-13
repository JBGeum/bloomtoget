<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>index</title>
    <link rel="icon" href="/img/favicon.ico" />
    <script src="/resource/js/all.js"></script>
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
    </c:import>
    <div class="custom-main">
     index
    </div>
  </div>

  </body>
</html>
