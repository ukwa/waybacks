<%@ page language="java" pageEncoding="utf-8" contentType="text/html;charset=utf-8"%>
<%@ page import="org.archive.wayback.exception.WaybackException" %>
<%@ page import="org.archive.wayback.core.UIResults" %>
<%@ page import="org.archive.wayback.util.StringFormatter" %>
<%
	UIResults results = UIResults.extractException(request);
	WaybackException e = results.getException();
	e.setupResponse(response);
%>
<%
	StringFormatter fmt = results.getWbRequest().getFormatter();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 
<html> 
	<head> 
		<meta http-equiv="Content-Type" content="text/html; charset=windows-1252"> 
		<link rel="stylesheet" HREF="/css/mess.css" TYPE="text/css"> 
		<script type="text/javascript">
			var gaJsHost = ( ( "https:" == document.location.protocol ) ? "https://ssl." : "http://www." );
			document.write( unescape( "%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E" ) );
		</script>
		<script type="text/javascript">
			try {
				var pageTracker = _gat._getTracker("UA-7571526-1");
				pageTracker._trackPageview();
			} catch( err ) {}
		</script>
		<script type="text/javascript" language="JavaScript" SRC="/scripts/javascript.js"></script> 
		<title>UK Web Archive</title> 
	</head> 
	<body bgcolor="#ffffff"> 
		<table cellspacing="0" border="0" width="730" align="center" cellpadding="0"> 
			<tr> 
				<td colspan=2>&nbsp;</td>
			</tr>
			<tr>
				<td width="365" align="left">
					<a href="/ukwa/">
						<img alt="UK Web Archive" src="/img/UKwebarchive.png"  border="0">
					</a>
				</td> 
				<td width="365"align="right">
					<a href="http://www.bl.uk">
						<img alt="British Library" src="/img/Providedby.gif"  border="0">
					</a>
				</td> 
			</tr> 
		</table> 
		<table width="735" border="0" cellspacing="2" cellpadding="2" align="center"> 
			<tr> 
				<td> 
					<table align="center" width="730" border="0" cellspacing="0" cellpadding="2"> 
						<tr> 
							<td bgcolor="#0C94AB"> 
								<div align="left">&nbsp;&nbsp;&nbsp;</div> 
							</td> 
							<td bgcolor="#0C94AB"> 
								<div align="right">&nbsp;&nbsp;&nbsp;</div> 
							</td> 
						</tr> 
					</table> 
				</td> 
			</tr>
			<tr>
				<td colspan=3>
					<h2><%= fmt.format(e.getTitleKey()) %></h2>
					<p><b><%= fmt.format(e.getMessageKey(),e.getMessage()) %></b></p>
					<hr size="1"> 
				</td>
			</tr>
			<tr>
				<td align="center">
					<a href="/ukwa/info/copyright" class="pages">&#124;&nbsp;&nbsp;Copyright notice</a>
					<a href="/ukwa/info/terms" class="pages">&#124;&nbsp;&nbsp;Terms of use&nbsp;&nbsp;&#124;</a>
					<a href="/ukwa/info/privacy" class="pages">Privacy statement&nbsp;&nbsp;&#124;</a>
				</td>
			</tr>
		</table>
	</body> 
</html> 