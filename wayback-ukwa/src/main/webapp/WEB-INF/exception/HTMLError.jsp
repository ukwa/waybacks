<%@ page language="java" pageEncoding="utf-8" contentType="text/html;charset=utf-8"
%><%@ page import="java.util.List"
%><%@ page import="java.util.Date"
%><%@ page import="java.util.Iterator"
%><%@ page import="org.archive.wayback.exception.WaybackException"
%><%@ page import="org.archive.wayback.ResultURIConverter"
%><%@ page import="org.archive.wayback.exception.ResourceNotInArchiveException"
%><%@ page import="org.archive.wayback.core.CaptureSearchResult"
%><%@ page import="org.archive.wayback.core.CaptureSearchResults"
%><%@ page import="org.archive.wayback.core.UIResults"
%><%@ page import="org.archive.wayback.core.WaybackRequest"
%><%@ page import="org.archive.wayback.util.StringFormatter"
%><%@ page import="org.archive.wayback.util.url.UrlOperations"
%><%@ page import="org.archive.wayback.partition.PartitionsToGraph"
%><%@ page import="org.archive.wayback.util.partition.Partitioner"
%><%@ page import="org.archive.wayback.util.partition.Partition"
%><%@ page import="org.archive.wayback.util.partition.PartitionSize"
%><%@ page import="org.archive.wayback.partition.PartitionPartitionMap"
%><%@page import="org.archive.wayback.exception.SpecificCaptureReplayException"
%><%
UIResults results = UIResults.extractException(request);
WaybackException e = results.getException();
WaybackRequest wbr = results.getWbRequest();
e.setupResponse(response);
String staticPrefix = wbr.getAccessPoint().getStaticPrefix();
String queryPrefix = wbr.getAccessPoint().getQueryPrefix();
String replayPrefix = wbr.getAccessPoint().getReplayPrefix();
String requestUrl = wbr.getRequestUrl();

StringFormatter fmt = results.getWbRequest().getFormatter();
%>
<jsp:include page="/WEB-INF/template/UI-header.jsp" flush="true" />

        <div id="positionHome">
            <section>
            <div id="error">

                <h2><%= fmt.format(e.getTitleKey()) %></h2>
                <p><%= fmt.escapeHtml(fmt.format(e.getMessageKey(),e.getMessage())) %></p>

<h3>Next steps</h3>

<%
if(e instanceof org.archive.wayback.exception.UnavailableForLegalReasonsException) {
	String ts = "20500101120000"; // Direct to most recent copy by default.
	if( wbr.getReplayTimestamp() != null) {
		ts = wbr.getReplayTimestamp();
	}
%>
			<p>
			If you are in a Legal Deposit Library Reading Room, you should be able to access this resource using a library computer, via the appropriate secure access gateway:
			</p>
			<ul>
			  <li><a href="https://bl.ldls.org.uk/welcome.html?<%= ts %>/<%= fmt.escapeHtml(requestUrl) %>">via the British Library's Secure Gateway</a></li>
			  <li><a href="https://llgc.ldls.org.uk/welcome.html?<%= ts %>/<%= fmt.escapeHtml(requestUrl) %>">via the National Library of Wales' Secure Gateway</a></li>
			  <li><a href="https://nls.ldls.org.uk/welcome.html?<%= ts %>/<%= fmt.escapeHtml(requestUrl) %>">via the National Library of Scotland's Secure Gateway</a></li>
			  <li><a href="https://cam.ldls.org.uk/<%= ts %>/<%= fmt.escapeHtml(requestUrl) %>">via the Cambridge University Library's Secure Gateway</a></li>
			  <li><a href="https://tcdlibrary.ldls.org.uk/<%= ts %>/<%= fmt.escapeHtml(requestUrl) %>">via the Trinity College Library's Secure Gateway</a></li>
			  <li><a href="https://bodleian.ldls.org.uk/<%= ts %>/<%= fmt.escapeHtml(requestUrl) %>">via the Bodleian Libraries' Secure Gateway</a></li>
			</ul>
<%
} else if(e instanceof ResourceNotInArchiveException) {
	ResourceNotInArchiveException niae = (ResourceNotInArchiveException) e;
	List<String> closeMatches = niae.getCloseMatches();
	if(closeMatches != null && !closeMatches.isEmpty()) {
%>
		        <p>Other possible close matches to try:</p>
		        <ul>
<%
		WaybackRequest tmp = wbr.clone();
		for(String closeMatch : closeMatches) {
			tmp.setRequestUrl(closeMatch);
			String link = queryPrefix + "query?" +
				tmp.getQueryArguments();
%>
			    <li><a href="<%= link %>"><%= closeMatch %></a></li>
		        </ul>
<%
		}
	}
} else if(e instanceof SpecificCaptureReplayException) {
	%>
	        <div class="wm-nav-link-div">
	        <%
	        SpecificCaptureReplayException scre = (SpecificCaptureReplayException) e;

	        CaptureSearchResult prev = scre.getPreviousResult();
	        CaptureSearchResult next = scre.getNextResult();
	        String dateFormat = "{0,date,MMMM dd, yyyy HH:mm:ss}";
	        ResultURIConverter conv = wbr.getAccessPoint().getUriConverter();
	        if((prev != null) && (next != null)) {
	                String safePrevReplay = fmt.escapeHtml(conv.makeReplayURI(prev.getCaptureTimestamp(),prev.getOriginalUrl()));
	                String safeNextReplay = fmt.escapeHtml(conv.makeReplayURI(next.getCaptureTimestamp(),next.getOriginalUrl()));
	                %>
	                Would you like to try the <a href="<%= safePrevReplay %>">previous</a> or <a href="<%= safeNextReplay %>">next</a> date?
	                <%
	        } else if (prev != null) {
	                String safePrevReplay = fmt.escapeHtml(conv.makeReplayURI(prev.getCaptureTimestamp(),prev.getOriginalUrl()));
	                %>
	                Would you like to try the <a href="<%= safePrevReplay %>">previous</a> date?
	                <%

	        } else if (next != null) {
	                String safeNextReplay = fmt.escapeHtml(conv.makeReplayURI(next.getCaptureTimestamp(),next.getOriginalUrl()));
	                %>
	                Would you like to try the <a href="<%= safeNextReplay %>">next</a> date?
	                <%
	        }
	        %>
	        </div>
	<%
}
%>

			<p>
			You may also wish to try:
			</p>
			<ul>

<%
	String parentUrl = UrlOperations.getUrlParentDir(requestUrl);
	if(parentUrl != null) {
		WaybackRequest tmp = wbr.clone();
		tmp.setRequestUrl(parentUrl);
		tmp.setUrlQueryRequest();
		String link = queryPrefix + "query?" +
			tmp.getQueryArguments();
		String escapedLink = fmt.escapeHtml(link);
		String escapedParentUrl = fmt.escapeHtml(parentUrl);
		%>
			    <li>Searching all pages under <a href="<%= escapedLink %>"><%= escapedParentUrl %></a></li>
		<%
	}
%>			
			
			  <li><a href="<%= fmt.escapeHtml(requestUrl) %>">Visiting the current, live site</a></li>
			  <li><a href="http://www.webarchive.org.uk/mementos/search/<%= fmt.escapeHtml(requestUrl) %>">Seeing if other archives hold copies of this URL</a></li>
			</ul>

            </div>
            </section>
            <div id="errorBorder"></div>

<jsp:include page="/WEB-INF/template/UI-footer.jsp" flush="true" />
