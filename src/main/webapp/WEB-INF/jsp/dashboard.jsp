<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<!-- Bootstrap -->
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css"
	rel="stylesheet" media="screen">
<link href="${pageContext.request.contextPath}/css/font-awesome.css"
	rel="stylesheet" media="screen">
<link href="${pageContext.request.contextPath}/css/main.css"
	rel="stylesheet" media="screen">
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="dashboard.html"> Application -
				Computer Database </a>
		</div>
	</header>

	<section id="main">
		<div class="container">
			<h1 id="homeTitle">${ requestScope.numComputers } Computers found</h1>
			<div id="actions" class="form-horizontal">
				<div class="pull-left">
					<form id="searchForm" action="#" method="GET" class="form-inline">
						<input type="search" id="searchbox" name="search"
							class="form-control" placeholder="Search name" /> <input
							type="submit" id="searchsubmit" value="Filter by name"
							class="btn btn-primary" />
					</form>
				</div>
				<div class="pull-right">
					<a class="btn btn-success" id="addComputer" href="addComputer">Add
						Computer</a> <a class="btn btn-default" id="editComputer" href="#"
						onclick="$.fn.toggleEditMode();">Edit</a>
				</div>
			</div>
		</div>

		<form id="deleteForm" action="#" method="POST">
			<input type="hidden" name="selection" value="">
		</form>

		<div class="container" style="margin-top: 10px;">
			<table class="table table-striped table-bordered">
				<thead>
					<tr>
						<!-- Variable declarations for passing labels as parameters -->
						<!-- Table header for Computer Name -->

						<th class="editMode" style="width: 60px; height: 22px;"><input
							type="checkbox" id="selectall" /> <span
							style="vertical-align: top;"> - <a href="#"
								id="deleteSelected" onclick="$.fn.deleteSelected();"> <i
									class="fa fa-trash-o fa-lg"></i>
							</a>
						</span></th>
						<th>Computer name</th>
						<th>Introduced date</th>
						<!-- Table header for Discontinued Date -->
						<th>Discontinued date</th>
						<!-- Table header for Company -->
						<th>Company</th>

					</tr>
				</thead>
				<!-- Browse attribute computers -->
				<tbody id="results">
					<c:forEach var="computer" items="${ requestScope.page.contenue }">
						<tr>
							<td class="editMode"><input type="checkbox" name="cb"
								class="cb" value="0"></td>
							<td><a href="editComputer.html" onclick="">${ computer.name }</a></td>
							<td>${ computer.introduced }</td>
							<td>${ computer.discontinued }</td>
							<td>${ computer.companyId }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</section>

	<footer class="navbar-fixed-bottom">
		<div class="container text-center">
			<ul class="pagination">
				<c:set var="url"
					value="${ request.getRequestURI() }?search=${ param.search }&numComputerPerPage=${ param.numComputerPerPage }"></c:set>
				<c:set var="firstPage" value="1"></c:set>
				<c:set var="lastPage" value="${ requestScope.page.totalPage }"></c:set>
				<c:set var="actual" value="${ requestScope.page.numPage }"></c:set>
				<c:set var="doublePrev" value="${ actual - 2 }"></c:set>
				<c:set var="prev" value="${ actual - 1}"></c:set>
				<c:set var="next" value="${ actual + 1 }"></c:set>
				<c:set var="doubleNext" value="${ actual + 2 }"></c:set>

				<li><a href="${ url }&page=${ firstPage }"
					aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
				</a></li>

				<c:if test="${ doublePrev >= firstPage }">
					<li><a href="${ url }&page=${ doublePrev }">${ doublePrev }</a></li>
				</c:if>

				<c:if test="${ prev >= firstPage}">
					<li><a href="${ url }&page=${ prev }">${ prev }</a></li>
				</c:if>

				<li><a href="${ url }&page=${ actual }">${ actual }</a></li>

				<c:if test="${ next <= lastPage }">
					<li><a href="${ url }&page=${ next }">${ next }</a></li>
				</c:if>

				<c:if test="${ doubleNext <= lastPage }">
					<li><a href="${ url }&page=${ doubleNext }">${ doubleNext }</a></li>
				</c:if>

				<li><a href="${ url }&page=${ lastPage }" aria-label="Next">
						<span aria-hidden="true">&raquo;</span>
				</a></li>
			</ul>

			<div class="btn-group btn-group-sm pull-right" role="group">
				<form id="numComputerPerPage" action="#" method="GET">
					<button type="submit" class="btn btn-default"
						name="numComputerPerPage" value="10">10</button>
					<button type="submit" class="btn btn-default"
						name="numComputerPerPage" value="50">50</button>
					<button type="submit" class="btn btn-default"
						name="numComputerPerPage" value="100">100</button>
				</form>
			</div>
		</div>
	</footer>
	<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/dashboard.js"></script>

</body>
</html>