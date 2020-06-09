<form id="upload" action="${param.action}" method="POST"
	enctype="multipart/form-data" onsubmit="return validateFile()">

	<div>
		<div class="container-fluid">
			<div class="row">
				<div class="col-sm-1"></div>
				<div class="col-sm-10">
					<div id="filedrag">
						<div id="beforeUpload">
							<i class="fa fa-cloud-upload fa-5x" aria-hidden="true"></i> <br>
							<label>
								<p id="uploadP" class="text-primary">Choose</p> 
								<input
								type="file" id="fileselect" name="file" accept=".zip" hidden/>
							</label> <span>or drop file here </span>
						</div>
						<div id="afterUpload">
							<i class="fa fa-check" id="fileCheck" aria-hidden="true"></i> <span id="fileNameMsg">Please select File</span>
						</div>

					</div>
				</div>
				<div class="col-sm-1"></div>
			</div>
		</div>

	</div>

	<div class="container-fluid">
		<div class="d-flex justify-content-center">
			<div id="submitbutton">
				<button id="btnShowPopup" class="btn btn-dark" type="submit">Upload
					File</button>
			</div>
		</div>
	</div>


	<div id="MyPopup" class="modal fade" role="dialog">
		<div class="modal-dialog">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
				<h4 class="modal-title">Error</h4>
					<button type="button" class="close" data-dismiss="modal">
						&times;</button>					
				</div>
				<div class="modal-body"></div>
				<div class="modal-footer">
					<button type="button" class="btn btn-danger" data-dismiss="modal">
						Close</button>
				</div>
			</div>
		</div>
	</div>

</form>

<div class="loader-wrapper" style="visibility: hidden">
    <span class="loader"><span class="loader-inner"></span></span>
</div>

<script type="text/javascript">
	function validateFile() {
		if (document.getElementById("fileselect").value != "") {
			return true;
		} else {
			/* alert("Please select file!"); */
			return false;
		}

	}
	$(function() {
		
		$("#fileselect").on('change',function(){
			debugger;
			if (fileValidation()) {
				showUpload();
			} else {
				hideUpload();
				document.getElementById("fileselect").value = "";
			}
	    });
		
		$("#btnShowPopup").click(function() {
			if (!validateFile()) {
				var title = "Error";
				var body = "Please select log file";

				/* $("#MyPopup .modal-title").html(title); */
				$("#MyPopup .modal-body").html(body);
				$("#MyPopup").modal("show");
			}else{
				$(".loader-wrapper").css('visibility', 'visible');
		        $(".loader-wrapper").fadeIn("fast");
			}
		});
	});

	filedrag.ondragover = filedrag.ondragenter = function(evt) {
		evt.preventDefault();
	};

	filedrag.ondrop = function(evt) {
		// pretty simple -- but not for IE :(
		fileselect.files = evt.dataTransfer.files;
		console.log("File transfered");
		// If you want to use some of the dropped files
		const dT = new DataTransfer();
		dT.items.add(evt.dataTransfer.files[0]);
		console.log("File added");
		/* dT.items.add(evt.dataTransfer.files[3]); */
		fileselect.files = dT.files;

		console.log("Files added2");
		evt.preventDefault();
		console.log("Validation about to fire");
		debugger;
		if (fileValidation()) {
			changeText();
		} else {
			document.getElementById("fileselect").value = "";
		}
		console.log("Validation fired");

	};
	window.addEventListener("dragover", function(e) {
		e = e || event;
		e.preventDefault();
	}, false);
	window.addEventListener("drop", function(e) {
		e = e || event;
		e.preventDefault();
	}, false);

	function fileValidation() {
		debugger;
		var fileName = document.getElementById('fileselect').value;
		var parts = fileName.split('.');
		var extension = parts[parts.length - 1];

		if (extension == "zip") {
			return true;
		} else {
			var title = "Error";
			var body = "Only zip files are allowed";

			/* $("#MyPopup .modal-title").html(title); */
			$("#MyPopup .modal-body").html(body);
			$("#MyPopup").modal("show");
			return false;
		}

	}

	function showUpload() {
		debugger;
		var fileName = document.getElementById('fileselect').files[0].name;
		document.getElementById("fileNameMsg").innerHTML="File <code>" + fileName +"</code> selected to Upload!"; 
		document.getElementById("fileCheck").style.visibility = "visible";
	}
	
	function hideUpload(){
		document.getElementById("fileNameMsg").innerHTML="Please select File";
		document.getElementById("fileCheck").style.visibility = "hidden";
	}
	
</script>
<script src="/js/filedrag.js"></script>