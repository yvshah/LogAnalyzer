<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="header.jsp"></jsp:include>

<div class="container">
	<h3>Minify Odl</h3>

	<jsp:include page="upload.jsp">
		<jsp:param value="minifyOdl" name="action" />
	</jsp:include>


	<c:if test="${not empty filePath}">
Your file has been successfully processed and you can download it from below

Download Processed File <a href="download?fileName=${filePath}">${filePath}</a>
	</c:if>

	<c:if test="${not empty incError}">

		<br>
		<br>

All the incidents reported in file <br>
		<c:forEach items="${incError}" var="incErr">
    Incident ${incErr.key} more details in ${incErr.value} <br>
		</c:forEach>
	</c:if>

	<c:if test="${not empty lstError}">
		<div class="container">
			<button type="button" class="btn btn-info" data-toggle="collapse"
				data-target="#demo">View All Errors</button>
			<div id="demo" class="collapse">
				<c:forEach items="${lstError}" var="err">
    ${err} <br>
				</c:forEach>
			</div>
		</div>
	</c:if>

</div>

</body>
</html>