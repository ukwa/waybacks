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
<!-- Google Analytics -->
<script type="text/javascript">

  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-7571526-1']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();

</script>
<!-- Flowplayer overrides -->
<script type="text/javascript">
	if( typeof flowplayer != "function" ) {
		var fScript = document.createElement( "script" );
		fScript.setAttribute( "type", "text/javascript" );
		fScript.setAttribute( "src", "//www.webarchive.org.uk/flowplayer/flowplayer-3.1.4.min.js" );
		document.getElementsByTagName( "head" )[0].appendChild( fScript );
	}
	var dScript= document.createElement( "script" );
	var oDomain = "<%=wbRequest.getRequestUrl()%>"; 

	dScript.setAttribute( "type", "text/javascript" );

	regEx = new RegExp( "https?:/+([^/]+)/.*" );
	oDomain = regEx.exec( oDomain );
	dScript.setAttribute( "src", "//www.webarchive.org.uk/flowplayer/" + oDomain[ 1 ] + ".js" );
	document.getElementsByTagName( "head" )[0].appendChild( dScript );
	// And fire:
	var oldOnload = window.onload;
    window.onload = function()
    {
        if( oldOnload ) oldOnload();
        // Attempt to change the page title:
        document.title = "[<%= fmt.format("UIGlobal.ARCHIVED") %>] " + document.title;
        // Initiate the flowplayer override:
		if( typeof streamVideo == "function" ) streamVideo();
    }
</script>
<!-- END HEAD INSERT -->
