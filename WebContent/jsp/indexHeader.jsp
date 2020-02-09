<%@page import="com.TDMData.constant.AppConstant"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<section class="headerDiv">
	<header>
		<section class="top-blue-nav">
			<div class="primary-nav">
				<nav>
					<ul>
						<li><a href="./index"><img src="images/home-icon.png" width="20" height="20" alt="" /></a></li>
						<li><a href="?lang=en">English</a></li>
						<li><a href="?lang=jp">Japanese</a></li>
						<li><a href="http://www.capgemini.com/about-capgemini" TARGET="_NEW"><img src="images/about-icon.png" width="20" height="20" alt="" /> About Us</a></li>
						<li><a href="http://www.in.capgemini.com/contact-capgemini" TARGET="_NEW"><img src="images/contact-icon.png" width="20" height="20" alt="" /> Contact Us</a></li>
						<li><a href="./logout?logout=true"><img src="images/logout-icon.png" width="20" height="20" alt="" />Logout</a></li>
					</ul>
				</nav>
			</div>
			<div class="welcome">
				<h5>
					<%-- <spring:message code="label.welcome"></spring:message> --%>
					<%
					out.println((String) session.getAttribute("UserId"));
				%>
				</h5>
			</div>
		</section>
		<section class="navigation">
			<div class="logo">
				<img src="images/logo-cap.jpg" class="logo" alt="" />
			</div>	
			<div class="main-nav">
				<div id="cssmenu">
					<ul>
						<li id="admin"><a href="#" >Admin</a>
							<ul>
								<li id="admin_db_connection"><a href="#" >Database Connections</a>
									<ul>
										<li id="db_connection"><a href="./dataConAddConnection">Add Connections</a></li>
										<li id="db_connection_list"><a href="./dataConListConnections">Modify/Remove Connections</a></li>
									</ul>
								</li>
								<!-- <li><a href="#">Profiles</a>
									<ul>
										<li><a href="./createProfiler">Add Profile</a></li>
										<li><a href="#">Modify/Remove Profile</a></li>
										<li><a href="#">Dump Profile</a></li>
										<li><a href="./tdmProfilersDashboard">Dash boarding</a></li>
									</ul>
								 </li> -->
							</ul>
						</li>
						<li id="uploads"><a href="#" >Services</a>
						  <ul>
							<!-- <li id="uploads_file_tab"><a href="#" >File to Table</a>
								<ul>
									<li id="uploads_file_tab_single"><a href="./dataConPage1">Upload File</a></li>
									<li id="uploads_file_tab_multi"><a href="#">Upload Multiple Files</a></li>
								</ul>
							</li>
							<li id="uploads_tab_tab"><a href="#" >Table to Table</a>
								<ul>
									<li id="uploads_tab_single"><a href="#">Single Table</a></li>
									<li id="uploads_tab_multi"><a href="#">Multiple Tables</a></li>
								</ul>
							</li> -->
							
							<li id="export_import_table"><a href="#" >Extract/Load Data</a>
								<ul>
									<li id="export_table"><a href="./dataTableExtract">Extract Data</a></li>
									<li id="import_table"><a href="./dataTableLoad">Load Data</a></li>
									<li id="dash_board"><a href="./<%=AppConstant.TABLE_EXTR_DASH_BOARD %>"+>Dash Board</a></li>
								</ul>
							</li>
							
							
						  </ul> 
					</ul>
				</div> 
			</div>
		</section>
		<section class="title-band">
			<div class="title">
				<h3 class="h3Tdm">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</h3>
			</div>
		</section>
	</header>
</section>