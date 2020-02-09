
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>Data Subset  | Profilers Dashboard</title>
 <link rel="stylesheet" type="text/css" href="css/jquery-ui.css">
	<link rel="stylesheet" type="text/css" href="css/style.css">
	<link rel="stylesheet" type="text/css" href="css/custom.css">
	<link rel="stylesheet" type="text/css" href="css/demo.css" />
	<link rel="stylesheet" type="text/css" href="css/style1.css" />
	<link rel="stylesheet" type="text/css" href="css/animate-custom.css" />
	<link rel="stylesheet" type="text/css" href="css/menu.css" />
	<link rel="stylesheet" type="text/css" href="css/theme.default.css">
	<link rel="stylesheet" type="text/css" href="css/stylesNew.css">
	    
	<script type="text/javascript" src="js/html5.js"></script>
	<script type="text/javascript" src="js/jquery.min.js"></script>
	<script type="text/javascript" src="js/jquery.tablesorter.min.js"></script>
	<script type="text/javascript" src="js/jquery-ui.js"></script>
	<script type="text/javascript" src="js/main.js"></script>
	<script type="text/javascript" src="js/jquery.validate.min.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
	<script type="text/javascript" src="js/jquery-migrate-1.2.1.min.js"></script>
	<script type="text/javascript" src="js/script.js"></script>
	
</head>
<body>
	<div class="wrapper mainAll">
		<jsp:include page="indexHeader.jsp"></jsp:include>
		 
		<div class="container">
				 
	      <h2 style="color: #0098cc ; padding-top: 38px;">Table Extraction Dash Board</h2>  
	        
	  <div class="nav" id="myid">	
	   <table id="search_output_table" class="hoverTable"  style="width:100%; font-size: 13px; border:0; cellpadding:0; cellspacing:1;">
				<thead>
					<tr>
					  	<th align="center"  bgcolor="#E3EFFB" scope="col" class="whitefont">Job Name</th>
					    <th align="center"  bgcolor="#E3EFFB" scope="col" class="whitefont">DB Con Name</th>						  	
						<th align="center"  bgcolor="#E3EFFB" scope="col" class="whitefont">Table List</th>	
						<th align="center"  bgcolor="#E3EFFB" scope="col" class="whitefont">Extract Only</th>						
						<th align="center"  bgcolor="#E3EFFB" scope="col" class="whitefont">ExtractionType</th>
						<th align="center"  bgcolor="#E3EFFB" scope="col" class="whitefont">Edit/Delete</th>	
						<th align="center"  bgcolor="#E3EFFB" scope="col" class="whitefont">Download</th>							
					</tr>
				</thead>
			 <tbody>
                   <c:forEach items="${dataTableExportImportDTO.tableExtractionDetailsDTOList}" var="table" >
	                    <tr>
	                     <td align="left">${table.jobName}</td>
	                     <td align="left">${table.dbConnectionDto.connectionName}</td>
	                     <td align="left">${table.tableList}</td>
	                     <td align="left">${table.extractOnly}</td>
	                     <td align="left">${table.extractionType}</td>
	                     <td align="center"><a href="editExtractionDetails?id=${table.id}"><span style="color: blue;">&#9998;</span></a> &nbsp;&nbsp; Or  &nbsp;&nbsp;<a href="deleteExtractionDetails?id=${table.id}" ><span style="color: red;">&#10008;</span></a></td>
	                     <td align="left"><a href="downloadExtraction?id=${table.id}" ><span style="color: Blue;">&nbsp;&#8681;&nbsp;&nbsp;Run & Extract</span></a></td>
	                   </tr>
                   </c:forEach>
               </tbody>
		</table>							
	 </div>		 
		 
		 	
   	    	 
        
        
   </div>
	<script src="include/footer.js"></script>
  </div>		

<script>
menu_highlight('admin');
menu_highlight('admin_db_connection');
menu_highlight('db_connection_list');

window.location.hash = "myid";
  /*  function getRequestDtls(id){
	   document.location.href="./dataConAddConnection?id="+id;
   } */
   
   $("#search_output_table").tablesorter({
	    widgets: ['zebra']
	  });
   $(".table tr:odd").css('background-color', '#ffffff');
   $(".table tr:even").addClass('even'); 
   
   function deleteTdmProfiler(manId){
		 if (confirm('Are you sure to delete a Profiler?')) {
	 	   document.location.href="./deleteProfiler?reqprofileName="+manId;
		  }
}
   
   function editTdmProfiler(manId){
	 	   document.location.href="./editProfiler?reqprofileName="+manId;
		  }
   function downloadTdmProfilerQuery(profileId){
	   document.location.href="./downloadProfilerQueries?reqprofileName="+profileId;
   }
</script>
</body>
</html>