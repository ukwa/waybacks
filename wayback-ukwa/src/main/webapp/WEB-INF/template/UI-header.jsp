<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page import="org.archive.wayback.core.WaybackRequest" %>
<%@ page import="org.archive.wayback.core.UIResults" %>
<%@ page import="org.archive.wayback.util.StringFormatter" %>
<%@ page language="java" pageEncoding="utf-8" contentType="text/html;charset=utf-8"%>
<%
UIResults results = UIResults.getGeneric(request);
WaybackRequest wbRequest = results.getWbRequest();
StringFormatter fmt = wbRequest.getFormatter();

String staticPrefix = results.getStaticPrefix();
String queryPrefix = results.getQueryPrefix();
String replayPrefix = results.getReplayPrefix();

%>
<!-- HEADER -->
<html xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en"><head>


<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta content="UK Web Archive, preserving UK website" name="description">
<meta content="UKWA, UK Web Archive, webarchive.org.uk" name="keywords">
<link media="screen" rel="stylesheet" href="<%= staticPrefix %>css/web_arch_reset.css" type="text/css">
<link media="screen" rel="stylesheet" href="<%= staticPrefix %>css/web_arch_styles.css" type="text/css">
<!--[if gte IE 7]><link href="<%= staticPrefix %>css/ie7.css" type="text/css" rel="stylesheet" /><![endif]-->
<!--[if IE 6]><link href="<%= staticPrefix %>css/ie6.css" type="text/css" rel="stylesheet" /><![endif]-->
<!--[if IE 5]><link href="<%= staticPrefix %>css/ie5.css" type="text/css" rel="stylesheet" /><![endif]-->
<!-- START OF Wayback CSS -->
<link rel="stylesheet" type="text/css"
                        href="<%= staticPrefix %>css/styles.css"
                        src="<%= staticPrefix %>css/styles.css" />
<!-- END OF Wayback CSS -->
<link href="<%= staticPrefix %>images/ukwa/favicon.ico" rel="shortcut icon">
<title>UK Web Archive</title>
</head><body class="js"><div class="header">

        <div class="banner">
                <div class="logo">
                        <h1>UK Web Archive</h1>
                        <a href="http://www.webarchive.org.uk/ukwa/" title="UK Web Archive home page"><img src="<%= staticPrefix %>images/ukwa/ukwa.jpg" title="UK Web Archive" alt="UK Web Archive"></a>
                </div>
                <div class="banner_image" id="banner_swf">
					<img src="<%= staticPrefix %>images/ukwa/banner.png" alt="">
                </div>
        </div>
</div>
<div class="breadcrumbs"><p>You are here:
        <span class="crumbs">
                <a href="http://www.webarchive.org.uk/ukwa/">Home</a> &gt; <%= fmt.format("UIGlobal.pageTitle") %>
                        </span>
</p></div>
<div class="main">
        <div class="british_library_logo">
                <p>Provided by:</p>
                <a href="http://www.bl.uk/" title="British Libary website"><img src="<%= staticPrefix %>images/ukwa/bl.jpg" title="British Library" alt="British Library logo"></a>
        </div><div class="main_content">
            <div class="content_extended" style="width:898px;">
        	<div class="content_panel search wayback" style="width:858px; min-height:250px; margin: 0 10px;">
         
        
        <!-- Start of Wayback code -->

		<table width="100%" border="0" cellpadding="0" cellspacing="5">

			<!-- GREEN BANNER -->
			<tr> 
				<td colspan="2" height="30" align="center" class="mainSecHeadW"> 
					<table width="100%" border="0" cellspacing="0" cellpadding="0">

						<tr class="mainBColor">
							<td colspan="2">
								<table border="0" width="100%" align="center">


									<!-- URL FORM -->
									<form action="<%= queryPrefix %>query" method="get">


										<tr>
											<td nowrap align="center"><img src="<%= staticPrefix %>images/shim.gif" width="1" height="20"> 

												<b class="mainBodyW">
													<font size="2" color="black" face="Arial, Helvetica, sans-serif">
														<%= fmt.format("UIGlobal.enterWebAddress") %>
													</font> 
													<input type="hidden" name="<%= WaybackRequest.REQUEST_TYPE %>" value="<%= WaybackRequest.REQUEST_CAPTURE_QUERY %>">
													<input type="text" name="<%= WaybackRequest.REQUEST_URL %>" value="http://" size="24" maxlength="256">
													&nbsp;
												</b> 
												<select name="<%= WaybackRequest.REQUEST_DATE %>" size="1">
													<option value="" selected><%= fmt.format("UIGlobal.selectYearAll") %></option>
													<option>2010</option>
													<option>2009</option>
													<option>2008</option>
													<option>2007</option>
													<option>2006</option>
													<option>2005</option>
													<option>2004</option>
													<option>2003</option>
													<option>2002</option>
													<option>2001</option>
													<option>2000</option>
													<option>1999</option>
													<option>1998</option>
													<option>1997</option>
													<option>1996</option>
												</select>
												&nbsp;
												<input type="submit" name="Submit" value="<%= fmt.format("UIGlobal.urlSearchButton") %>" align="absMiddle">
												&nbsp;
												<a href="<%= staticPrefix %>advanced_search.jsp" style="color:black;font-size:11px">
													<%= fmt.format("UIGlobal.advancedSearchLink") %>
												</a>

											</td>
										</tr>


									</form>
									<!-- /URL FORM -->
									  
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<!-- /GREEN BANNER -->
		</table>
<!-- /HEADER -->
