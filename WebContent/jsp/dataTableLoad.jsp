<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>Data Subset | Index</title>
<link rel="stylesheet" type="text/css" href="css/jquery-ui.css" />
<link rel="stylesheet" type="text/css" href="css/style.css" />
<link rel="stylesheet" type="text/css" href="css/custom.css" />
<link rel="stylesheet" type="text/css" href="css/demo.css" />
<link rel="stylesheet" type="text/css" href="css/style1.css" />
<link rel="stylesheet" type="text/css" href="css/animate-custom.css" />
<link rel="stylesheet" type="text/css" href="css/menu.css" />
<link rel="stylesheet" type="text/css" href="css/theme.default.css" />
<link rel="stylesheet" type="text/css" href="css/stylesNew.css" />

<script type="text/javascript" src="js/html5.js"></script>
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery.tablesorter.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.js"></script>
<script type="text/javascript" src="js/main.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="js/script.js"></script>

<script type="text/javascript">
	function getTablesFromDatabase(evt) {
		//alert(evt.srcElement.value);
		//alert(evt.srcElement.value);

		var innerText = "";
		$.ajax({
			url : "getTablesFromDatabase?databaseName=" + evt.srcElement.value,
			type : "GET",
			dataType : 'json',
			success : function(data) {

				for (var i = 0; i < data.dataTable.length; i++) {
					innerText = innerText
							+ "<option id=\""+data.dataTable[i]+"\">"
							+ data.dataTable[i] + "</option>"
				}
				document.getElementById('tableList').innerHTML = innerText;
				document.getElementById('selectedTable').innerHTML = "";

				//$("#tableList").innerHTML(innerText);

			}
		});

	}

	function getRalationalTables(evt) {

		var conditionFl = $("#selectExtractionType option:selected").val();
		var selectedTables = "";
		var dbConId = "";
		var innerText = "";
		if (conditionFl.match("Manual_Select")) {

			innerText = "<option>you can not extract Child <br>and Parent Tables</option>";

			document.getElementById('parentChildTables').innerHTML = "";
			document.getElementById('parentChildTables').innerHTML = innerText;
			$("#tableList").innerHTML(innerText);
			$('#tableList').attr('disabled', 'disabled');
			//$('#tableList').prop('disabled', true );
			/* $("#tableList").attr( "disabled", true ); */
		} else {

			$('#selectedTable :selected').each(function(i, selected) {
				selectedTables = selectedTables + $(selected).text() + ",";
			});
			selectedTables = selectedTables.substring(0, selectedTables
					.lastIndexOf(","));
			dbConId = $("#tableExportId :selected").val();

			$
					.ajax({
						url : "fetchConditionRelationTabs?reqVals="
								+ selectedTables + "&databaseId=" + dbConId
								+ "&conditionFlag=" + conditionFl,
						type : "GET",
						dataType : 'json',
						success : function(data) {

							//	alert(data.responseData);

							for (var i = 0; i < data.responseData.length; i++) {
								innerText = innerText
										+ "<option id=\"parentChild"+data.responseData[i]+"\">"
										+ data.responseData[i] + "</option>"
							}

							//document.getElementById('parentChildTables').innerHTML = ""; 
							//$('#tableList').prop('disabled', false);
							//$("#tableList").attr( "disabled", false);
							document.getElementById('parentChildTables').innerHTML = "";
							document.getElementById('parentChildTables').innerHTML = innerText;
							$("#tableList").innerHTML(innerText);
							$('#tableList').remove('disabled', 'disabled');

						}
					});

		}

	}

	function backSelectedTable() {
		var swapObject = $('#selectedTable :selected');
		$("#tableList").append(swapObject);
	}

	function swapSelectedTable() {
		//alert($("#tableList"));
		var swapObject = $('#tableList :selected');

		var val = swapObject.text();
		//alert(val);

		/* 	'<input type="hidden" name="dbConnectionsDTOs.dataTables['+counter+']"/>' */
		var option1 = '<option id="'+val+'1">' + val + '</option>';

		$("#selectedTable").append(swapObject);
	}
	function onTableCheckSchema() {

		if ($("#selectSchema")[0].checked == true) {
			var createSchemaBtn = '<input  type="submit" id="createSchema" style="width: 130px;" value="Create Schema" />';
			$("#buttonHolder").append(createSchemaBtn);
			$("#loadDataBtn").remove();
		} else {
			var createSchemaBtn = '<input type="submit" value="Load" id="loadDataBtn" style="width: 80px;" />'
			$("#buttonHolder").append(createSchemaBtn);
			$("#createSchema").remove();
		}
	}
	function createTableSchema() {
		//   url : 'createSchemaForUpload',
		var targetId = document.getElementById("tableExportId").value;
		var schemaFile = document.getElementById("schemaFile");
		
		//var formdata = new FormData();
		var formdata = new FormData();
		formdata.append("id", targetId);
		formdata.append("databaseFile", schemaFile); 
		var xmlhttp;
		
		if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp = new XMLHttpRequest();
        } else {// code for IE6, IE5
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
       
        xmlhttp.open("POST", "createSchemaForUpload", true);
        xmlhttp.send(formdata);
        
	/* 
		var xhr = new XMLHttpRequest();
		xhr.open("POST", "createSchemaForUpload", true);
		xhr.send(formdata);
		xhr.onload = function(e) {
			var arr = this.responseText;
			alert(arr);
		} */
		
		/* var htmlRender = "";
	//	formdata.append("fileDetails", sampleFile);
		//alert("is header : " + isHeader);
	//	formdata.append("parameters", isHeader);
		var xhr = new XMLHttpRequest();
		xhr.open("POST", "createSchemaForUpload", true);
		xhr.send(formdata);
		xhr.onload = function(e) {
			alert(e);
		} */
		
		/* var formData = $('#myform').serialize();
	     $.ajax({
	            type: "POST",
	            url: "second.jsp",
	            async: true,
	            data: formData,
	            contentType: "multipart/form-data",
	            processData: false,
	            success: function(msg) {
	             alert("File has been uploaded successfully");
	            }
	        });
		
		
		
		
		$.ajax({
			type: 'POST',
			url : 'createSchemaForUpload',
			enctype: 'multipart/form-data',
            data: 'filename='+ $('#schemaFile').val()+'&targetId='+targetId,
			/* data: formdata,
	        dataType: 'multipart/form-data', */
	       /*  success: function(msg) {
	            alert(msg);
	        }
			
			 */
			
		/* 	url : "createSchemaForUpload",
			type : "POST",
			data:  formdata,
			contentType: "multipart/form-data",
			success : function(data) {

				alert(data); */

				//$("#tableList").innerHTML(innerText);

			
	/* 	}); */
		
		
		
	}

	function chech() {
		var counter = 0;
		var innerText = "";
		$('select#selectedTable')
				.find('option')
				.each(
						function() {
							var textVal = $(this).val() + "";
							innerText = innerText
									+ '<input type="hidden" name="dataTables['+counter+']" value="'+textVal+'" />'
							counter++;
						});

		$("#dataTableExportForm").append(innerText);
		//document.getElementById('dataTableExportForm').innerHTML =innerText;

	}
</script>
<style type="text/css">
div.columns {
	width: 700px;
}
</style>
</head>
<body>
	<div class="mainAll">
		<jsp:include page="indexHeader.jsp"></jsp:include>
		<!-- <input type="hidden" name="dbConnectionsDTOs."/> -->
		<!-- <section class="bodySec"> -->
		<div class="container">
			<%-- <form:form id="dataTableExportForm"
					modelAttribute="dbConnectionsDto" name="dataTableLoadForm"
					action="loadTables" enctype="multipart/form-data"> --%>
			<%-- <form:form id="dataTableExportForm" method="POST"
					modelAttribute="dbConnectionsDto" name="dataTableLoadForm"
					action="loadTables" enctype="multipart/form-data"> --%>
			<div style="hight: 90px;">
				<h2 style="color: #0098cc; padding-top: 5%;">Load database
					Connection</h2>
			</div>


			<%-- 	
				<form:form id="databaseConnctionDetailsFlatFile"
				name="databaseConnctionDetails"
				action="loadTablesIntoDatabase" method="post"
				modelAttribute="dbConnectionsDto"
				enctype="multipart/form-data">
 --%>


			<%-- <div class="form-group col-sm-9">
					<div class="col-sm-12 fieldsHeight" style="margin-top: 25px;">
						<label class="col-sm-3 control-label fieldsHeight">Connection
							Name:</label>
						<div class="col-sm-9 fieldsHeight">
							<form:select path="dbConnectionDto.conId" style="width: 200px;"
									id="tableExportId" class="down-control"
									onchange="getTablesFromDatabase(event)">
									<c:forEach items="${dbConnectionsDto.dbConnectionsDtoList}"
										var="dbConnectionsDTOs" varStatus="status">
										<form:option value="${dbConnectionsDTOs.conId }">${dbConnectionsDTOs.connectionName }</form:option>
									</c:forEach>
								</form:select>
						</div>
					</div>
	

					<div class="col-sm-12 fieldsHeight" style="margin-top: 25px;"
						id="fixedFilePath">

						<label class="col-sm-3 control-label fieldsHeight">Metadata
							file path :</label>
						<div class="col-sm-8 fieldsHeight">
							<div class="col-sm-5 fieldsHeight">
								<input class="col-sm-12" type="file" name="flatMetaDataFileName"
									id="metaFilePathInText" onchange="showMetaFilePath(event);" />
							</div>
							
						</div>
						
					</div> --%>
			<!-- <div class="col-sm-12 fieldsHeight" style="margin-top: 5px;"
						id="delimeterId">
						<label class="col-sm-3 control-label fieldsHeight">Populate
							Result:</label>
						<div class="col-sm-9">
							<button type="button" onclick="checkContentsTry(event)"
								class="btn btn-warning">Schema</button>

						</div>
					</div> -->




			<%-- 
				</div>
				
				
				<input type="submit"/>
				

			
			</form:form>
				 --%>






			<div style="hight: 690px;">

				<form:form id="loadTablesIntoDatabase" name="loadTablesIntoDatabase"
					action="${pageContext.request.servletContext.contextPath}/loadTablesIntoDatabase" method="POST"
					modelAttribute="dbConnectionsDto" enctype="multipart/form-data">

					<div style="float: left;">
						<div style="margin-top: 35px; hight: 110px;">

							<div style="width: 200px; margin-left: 25px; float: left;">
								<h2 style="color: #0098cc;">Target Connection</h2>
							</div>
							<div style="width: 200px; float: left;">
								<form:select path="dbConnectionDto.conId" style="width: 200px;"
									id="tableExportId" class="down-control"
									onchange="getTablesFromDatabase(event)">
									<c:forEach items="${dbConnectionsDto.dbConnectionsDtoList}"
										var="dbConnectionsDTOs" varStatus="status">
										<form:option value="${dbConnectionsDTOs.conId }">${dbConnectionsDTOs.connectionName }</form:option>
									</c:forEach>
								</form:select>
							</div>
							<div style="width: 100px; padding-left: 55px; float: left;">
								<h2 style="color: #0098cc;">Browse File</h2>
							</div>
							<div style="width: 200px; float: left;">
								<input class="col-sm-12" type="file" id="schemaFile" name="databaseFile" />
							</div>
						</div>
					</div>
					<div style="float: left; margin-top: 25px;">
						<div style="hight: 30px;">

							<div style="width: 200px; float: left; margin-left: 25px;">
								<h2 style="color: #0098cc;">Operation</h2>
							</div>

							<div style="width: 200px; float: left;">
								<form:select path="tableExtractDetails.tableOperation"
									id="tableOperation" style="width: 200px;" class="down-control">
									<form:option value="insertOrUpdate">INSERT & UPDATE</form:option>
									<form:option value="truncateAndLoad">TRUNCATE & FRESH-LOAD</form:option>
								</form:select>
							</div>
							<div style="width: 100px; float: left; margin-left: 55px;">
								<h2 style="color: #0098cc;">Create Schema</h2>
							</div>
							<div
								style="width: 200px; float: left; margin-top: 4px; margin-left: 10px;">
								<form:checkbox path="createSchema"
									onclick="onTableCheckSchema();" id="selectSchema"
									value="createTableSchema" />
							</div>
						</div>
					</div>

					<div style="float: left; margin-top: 25px;">
						<div style="hight: 30px;">

							<div class="buttonsAll22"
								style="width: 230px; float: left; margin-top: 4px;">
								&nbsp;</div>
							<div class="buttonsAll22" style="width: 600px; float: left;"
								id="buttonHolder">
								<input type="submit" value="Load" id="loadDataBtn" style="width: 80px;" />
							</div>
						</div>
					</div>

					<div class="buttonsAll22"
						style="margin-top: 40px; width: 800px; padding-left: 210px; hight: 90px; float: left;">

						<table
							style="font-size: 13px; cellpadding: 2; border-spacing: 5px;">
							<thead>
								<tr>
									<td width="200px;">Table</td>
									<td width="125px;">Fail to Load</td>
									<td width="125px;">Rows Inserted</td>
									<td width="125px;">Rows updated</td>
									<td width="125px;">Logs</td>
								</tr>
							</thead>
							<tbody>

								<c:forEach items="${dbConnectionsDto.databaseLoadingInfoModel }"
									var="table">

									<tr>
										<td width="200px;">${table.tableName }</td>
										<td width="125px;">${table.failToLoad }</td>
										<td width="125px;">${table.totalInsert }</td>
										<td width="125px;">${table.totalUpdate }</td>
										<td width="125px;">-</td>
									</tr>
								</c:forEach>

							</tbody>
						</table>
					</div>
					<%-- </c:if> --%>

					<%-- 
				</c:if> 
 --%>
				</form:form>
			</div>
		</div>
		<!-- </section> -->
		<script src="include/footer.js"></script>
	</div>

</body>
</html>
