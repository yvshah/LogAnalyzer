<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8" />
	<title>Log Analyzer</title>
	<link rel="stylesheet" type="text/css" media="all" href="/css/bootstrap.min.css" /><!-- CSS only -->
	<link rel="stylesheet" type="text/css" media="all" href="/css/styles.css" /><!-- CSS only -->
	<link rel="stylesheet" type="text/css" media="all" href="/css/loader.css" /><!-- CSS only -->
	<link rel="stylesheet" type="text/css" media="all" href="/fa/css/all.min.css" /><!-- CSS only -->
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
	<!-- JS and jQuery -->
	<script src="/js/jquery-3.5.1.min.js"></script>
	<script src="/js/popper.min.js"></script>
	<script src="/js/bootstrap.min.js"></script>

<script>
	$(document).ready(function() {
		$('[data-toggle="tooltip"]').tooltip();
	});
	
	$(function () {
        $("#logo").dblclick(function () {
        	debugger;
	            $("#devByPopup").modal("show");
        });
    });
	
</script>

</head>
<body>

	<div id="devByPopup" class="modal fade" role="dialog">
		<div class="modal-dialog">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title">Developed & Maintained By</h4>
					<button type="button" class="close" data-dismiss="modal">
						&times;</button>
				</div>
				<div class="modal-body">
					<ul>
						<li>Yash Shah</li>
						<li>Ashutosh Parmar</li>
						<li>Rohan Jain</li>
						<li>Himanshu Ramani</li>
						<li>Darshan Dadhaniya</li>
					</ul> 
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-danger" data-dismiss="modal">
						Close</button>
				</div>
			</div>
		</div>
	</div>

<nav class="navbar navbar-expand-sm bg-dark navbar-dark">
  <!-- Brand/logo -->
  <a class="navbar-brand" id="logo" href="#">
  <img src="/img/sherlock.png" alt="Logo" style="width:40px;">
  </a>
  
  <a class="navbar-brand" href="/LogAnalyzer/home">
  Log Analyzer<span class="badge badge-pill badge-light" style="font-size: 50%">Beta</span>
  </a>
  
  <!-- Links -->
  <ul class="navbar-nav">
    <li class="nav-item">
      <a class="nav-link" href="/LogAnalyzer/showMinifyOdl">Minify ODL</a>
    </li>
    <li class="nav-item">
      <a class="nav-link" href="/LogAnalyzer/showPlSql">PL/Sql Analyzer</a>
    </li>
    <li class="nav-item">
      <a class="nav-link disabled" href="#" data-toggle="tooltip" title="Coming Soon!">GMFZT Analyzer</a>
    </li>
  </ul>
  
</nav>