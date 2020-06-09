<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="header.jsp"></jsp:include>


<div class="container">
	<h3>PlSql Analyzer</h3>

	<jsp:include page="upload.jsp">
		<jsp:param value="procPlSql" name="action" />
	</jsp:include>

	<c:if test="${not empty listProc}">

Your Data has been processed, Below are the methods which might have error'd: <br>
		<c:forEach items="${listProc}" var="proc">
    Entered in method <code>${proc.key}</code> on line ${proc.value} <br>
		</c:forEach>
	</c:if>

</div>

</body>
</html>