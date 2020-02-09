<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>
<!doctype html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>Data Subset | Index</title>
<link rel="stylesheet" type="text/css" href="css/jquery-ui.css">
<link rel="stylesheet" type="text/css" href="css/style.css">
<link rel="stylesheet" type="text/css" href="css/custom.css">
<link rel="stylesheet" type="text/css" href="css/demo.css" />
<link rel="stylesheet" type="text/css" href="css/style1.css" />
<link rel="stylesheet" type="text/css" href="css/animate-custom.css" />
<link rel="stylesheet" type="text/css" href="css/menu.css" />
<link rel="stylesheet" type="text/css" href="css/theme.default.css">
<link rel="stylesheet" type="text/css" href="css/stylesNew.css">
<link rel="stylesheet" type="text/css" href="css/ModelDialogBox.css">

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
		var innerText="";
		if(conditionFl.match("Manual_Select")) {
			
			
			
			innerText="<option>you can not extract Child <br>and Parent Tables</option>";
			
			document.getElementById('parentChildTables').innerHTML = ""; 
			document.getElementById('parentChildTables').innerHTML = innerText;
			$("#tableList").innerHTML(innerText);
			$('#tableList').attr('disabled', 'disabled');
			//$('#tableList').prop('disabled', true );
			/* $("#tableList").attr( "disabled", true ); */
		}
		else {
			
		$('#selectedTable :selected').each(function(i, selected){ 		 
		  selectedTables = selectedTables+$(selected).text()+",";
		});
		selectedTables = selectedTables.substring(0, selectedTables.lastIndexOf(","));
		dbConId = $("#tableExportId :selected").val();
		
		$.ajax({
			url : "fetchConditionRelationTabs?reqVals=" + selectedTables+"&databaseId="+dbConId+"&conditionFlag="+conditionFl,
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
	function chech() {
		
		var innerText = "";
		$('select#selectedTable')
				.find('option')
				.each(
						function() {
							var textVal = $(this).val() + "";
							innerText = innerText +textVal +",";
							/* innerText = innerText
									+ '<input type="hidden" name="dataTables['+counter+']" value="'+textVal+'" />' */
									
						
						});

		
		$("#dataTableExportForm").append('<input type="hidden" name="tableExtractDetails.tableList" value="'+innerText+'" />');
		//document.getElementById('dataTableExportForm').innerHTML =innerText;

	}
</script>

</head>
<body>

	<div class="mainAll">

		<jsp:include page="indexHeader.jsp"></jsp:include>
		<!-- <input type="hidden" name="dbConnectionsDTOs."/> -->
		<section class="bodySec">
			<div class="container">
				<form:form id="dataTableExportForm"
					modelAttribute="dbConnectionsDto" name="dataTableExportForm"
					action="exportTables">
					<div class="two-col">
						<h2 style="color: #0098cc; padding-top: 5%;">Extract database
							Connection</h2>
						<table
							style="width: 100%; border: 0; font-size: 13px; cellpadding: 2; border-spacing: 5px; padding: 2% 0% 0% 0%;">
							<%--  <form:hidden path="conId"/> --%>
							<tbody>
								<tr>
									<td class="lable-title" align="left" style="width:170px;" valign="middle">Job Name<span>*</span></td>
									<td style="padding-left: 15px;width:190px; " align="left" valign="middle">
										<form:input path="tableExtractDetails.jobName" style="width:190px; " class="form-control"/>
									</td>
									<%-- <td class="lable-title" align="left" style="width:100px;" valign="middle">Table Operation<span>*</span></td>
									<td style="padding-left: 15px;width:190px; " align="left" valign="middle">
										<form:select path="tableExtractDetails.tableOperation" id="tableOperation" style="width: 200px;" class="down-control">
												<form:option value="update">Update</form:option>
												<form:option value="insert">Insert</form:option>
										</form:select>
									</td> --%>
								</tr>
								<form:hidden path="tableExtractDetails.id"/>
								<tr>
									<td class="lable-title" align="left" style="width:170px; margin-bottom: 29px;" valign="middle">Connection Name<span>*</span></td>
									<td style="padding-left: 15px;width:150px; " align="left" valign="middle">
										<form:select path="tableExtractDetails.dbConnectionDto.conId" id="tableExportId"  style="width: 200px;" class="down-control" onchange="getTablesFromDatabase(event)">
									
											<c:forEach items="${dbConnectionsDto.dbConnectionsDtoList}"
												var="dbConnectionsDTOs" varStatus="status">
												<form:option value="${dbConnectionsDTOs.conId }">${dbConnectionsDTOs.connectionName }</form:option>
												<%-- <form:option value="${dbConnectionsDTOs.connectionName }">${dbConnectionsDTOs.connectionName }</form:option> --%>
											</c:forEach>

										</form:select>
									</td>
									<td class="lable-title" align="left" style="width:70px; padding-left: 35px;" valign="middle">Extraction<span>*</span></td>
									 <td style="padding-left: 5px;" align="left" valign="middle">									
										<form:select path="tableExtractDetails.extractionType" id="selectExtractionType" style="width: 120px;" class="down-control" onchange="getRalationalTables(event);">
												<form:option value="All">Parent+Child</form:option>
												<form:option value="Parent">Parent</form:option>
												<form:option value="Child">Child</form:option>
												<form:option value="Manual_Select">Manual Select</form:option>
										</form:select>
									</td> 
									
								<%-- 	<td><form:radiobutton path="fetchConditionTable"
											id="allId" value="All" checked="checked" /> All</td>
									<td><form:radiobutton path="fetchConditionTable"
											id="parentId" value="Parent" /> Parent</td>
									<td><form:radiobutton path="fetchConditionTable"
											id="childId" value="Child" /> Child</td>
									<td><input type="button" value="Fetch Tables" onclick="getRalationalTables()"></td> --%>
								</tr>
								
								
								
								<%-- <tr>
									<td class="lable-title" class="lable-title" align="left" style="width:150px;" valign="middle">Table
										List<span>*</span>
									</td>

									<td style="padding-left: 10px;" align="left" valign="middle" align="left" valign="middle">
										<!-- <select size="25px;" id="sbOne" multiple="multiple"> -->
										<select style="margin-top: 25px; width: 200px; height: 200px"
										id="tableList" multiple="multiple">

									</select>
									</td>
									<td><input type="button" value=" >> "
										onclick="swapSelectedTable()"><br>
									<br> <input type="button" value=" << "
										onclick="backSelectedTable()"></td>
									<td><select
										style="margin-top: 25px; width: 200px; height: 200px"
										id="selectedTable" multiple="multiple">

									</select></td>

									<td style="padding-left: 15px;" align="left" valign="middle">
										<!-- <select size="25px;" id="sbOne" multiple="multiple"> -->
										<select style="margin-top: 25px; width: 200px; height: 200px"
										id="parentChildTables" multiple="multiple">

									</select>
									</td>
								</tr>
								 --%>
								
							</tbody>
						</table>

						<table style="border-spacing: 10px;margin-top: 25px;">
							<%--  <form:hidden path="conId"/> --%>
							<tbody>
							
							
							<tr>
									<td class="lable-title" class="lable-title" align="left" style="width:150px; " valign="middle">
									&nbsp;
									</td>

									<td style="padding-left: 10px;" align="left" valign="middle" align="left" valign="middle">
										<!-- <select size="25px;" id="sbOne" multiple="multiple"> -->
										TABLE LIST
									</td>
									<td>&nbsp;</td>
									<td>SELECTED TABLE</td>

									<td style="padding-left: 15px;" align="left" valign="middle">
										<!-- <select size="25px;" id="sbOne" multiple="multiple"> -->
										RELATED TABLES
									</td>
								</tr>
							
							
								<tr>
									<td class="lable-title" class="lable-title" align="left" style="width:130px;" valign="middle">Table
										List<span>*</span>
									</td>

									<td style="padding-left: 10px;" align="left" valign="middle" align="left" valign="middle">
										<!-- <select size="25px;" id="sbOne" multiple="multiple"> -->
										<select style="margin-top: 5px; width: 200px; height: 200px;"
										id="tableList" multiple="multiple">
											<c:forEach items="${dbConnectionsDto.unSelectedTableList}" var="tbl">
												<option id="${tbl}">${tbl}</option>
											</c:forEach>
									</select>
									</td>
									<td><input type="button" value=" >> "
										onclick="swapSelectedTable()"><br>
									<br> <input type="button" value=" << "
										onclick="backSelectedTable()"></td>
									<td><select	style="margin-top: 5px; width: 200px;  height: 200px;"
										id="selectedTable" multiple="multiple">
											<c:forEach items="${dbConnectionsDto.selectedTableList}" var="tbl">
												<option id="parentChild${tbl}">${tbl}</option>
											</c:forEach>
									</select></td>

									<td style="padding-left: 15px;" align="left" valign="middle">
										<!-- <select size="25px;" id="sbOne" multiple="multiple"> -->
										<select style="margin-top: 5px; width: 200px;  height: 200px;"
										id="parentChildTables" multiple="multiple">

									</select>
									</td>
								</tr>
								<tr>
								<td style="width:160px;">&nbsp;</td>
								<td>
									<form:select path="tableExtractDetails.extractOnly" class="down-control" style="width: 200px;">
										<option value="ddl_with_data">DDL With Data</option>
										<option value="onlyData">Only Data </option>
										<option value="onlySchema">Only Schema</option>
									</form:select>
								</td>
								<td >&nbsp;</td>
								</tr>
								<tr>
								<td style="width:150px;">&nbsp;</td>
								<td style="width:150px;" align="left" valign="middle" class="buttonsAll22">
					  	 			<input style="width:150px;" type="submit" onclick="chech()" value="Save/Update">
					  	 		</td>
								
								
								<!-- <td>
									<input type="submit" class="buttonsAll22"
										value="Extract" onclick="chech()"/>
									<br>
								</td> -->
								
								</tr>
							</tbody>
						</table>
					</div>
				</form:form>
			</div>
		</section>
		<script src="include/footer.js"></script>
	</div>
	<script>
		menu_highlight('admin');
		menu_highlight('admin_db_connection');
		menu_highlight('db_connection');

		$(document).ready(function() {
			var btn = $
			{
				btn
			}
			;
			if (btn == false) {
				$('#create').prop('disabled', false);
			} else {
				$('#create').prop('disabled', true);
			}
		});
	</script>
</body>
</html>
