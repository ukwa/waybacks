<%@   page language="java" pageEncoding="utf-8" contentType="text/html;charset=utf-8"
%><%@ page import="org.archive.wayback.ResultURIConverter"
%><%@ page import="org.archive.wayback.core.UIResults"
%><%@ page import="org.archive.wayback.core.WaybackRequest"
%><%@ page import="org.archive.wayback.util.StringFormatter"
%><%@ page import="org.archive.wayback.util.url.UrlOperations"
%><%
UIResults results = UIResults.extractReplay(request);
WaybackRequest wbRequest = results.getWbRequest();
ResultURIConverter uriConverter = results.getURIConverter();
StringFormatter fmt = wbRequest.getFormatter();

String staticPrefix = results.getStaticPrefix();
String queryPrefix = results.getQueryPrefix();
String replayPrefix = results.getReplayPrefix();

String timestampedPrefix = fmt.escapeHtml(queryPrefix + wbRequest.getReplayTimestamp() + "/");

String searchUrl = UrlOperations.stripDefaultPortFromUrl(wbRequest.getRequestUrl());
String searchUrlJS = fmt.escapeJavaScript(searchUrl);


%>
<!-- BEGIN HEAD INSERT -->
<!-- Experimental JavaScript overrides for improving playback. -->

<!-- Declare playback context info where JavaScript can access it: -->
 
<script type="text/javascript">
  window.waybackPrefix = "<%= replayPrefix %>";
  window.waybackTimestamp = "<%= wbRequest.getReplayTimestamp() %>";
  window.waybackReplayPrefix = "<%= timestampedPrefix %>";
  window.waybackOriginalUrl = "<%= searchUrlJS %>";
  // Derive the host/base url:
  pathArray = window.waybackOriginalUrl.split( '/' );
  protocol = pathArray[0];
  host = pathArray[2];
  window.waybackOriginalHost = protocol + '//' + host;  
</script>

<!-- Include JavaScript function overrides -->
 <script type="text/javascript" src="<%= staticPrefix %>js/javascript-overrides.js"></script>
 
<!-- END HEAD INSERT -->
