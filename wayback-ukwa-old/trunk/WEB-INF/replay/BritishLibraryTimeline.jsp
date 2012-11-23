<%@ page language="java" pageEncoding="utf-8" contentType="text/html;charset=utf-8"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.ParseException" %>
<%@ page import="org.archive.wayback.WaybackConstants" %>
<%@ page import="org.archive.wayback.core.CaptureSearchResult" %>
<%@ page import="org.archive.wayback.core.CaptureSearchResults" %>
<%@ page import="org.archive.wayback.core.UIResults" %>
<%@ page import="org.archive.wayback.core.WaybackRequest" %>
<%@ page import="org.archive.wayback.query.resultspartitioner.ResultsTimelinePartitionsFactory" %>
<%@ page import="org.archive.wayback.query.resultspartitioner.ResultsPartition" %>
<%@ page import="org.archive.wayback.util.StringFormatter" %>
<%
/*
Added to get instance information from webarchive database.
*/
String returnURL = null;
String cookieURL = null;
String r = request.getHeader("referer");

if ( r != null && r.contains("/ukwa/target/") ) {
		returnURL = r;
		cookieURL = r.replace(":","-replace-");
		Cookie cookie1 = new Cookie("returnURL", cookieURL);
    	response.addCookie(cookie1); 
} else {
	Cookie[] cookies = request.getCookies();
	for(int i = 0; i < cookies.length; i++) { 
		if (cookies[i].getName().equals("returnURL")) {
   			returnURL = cookies[i].getValue().replace("-replace-",":");
   		}
	} 
}
/*
End
*/
String contextRoot = request.getScheme() + "://" + request.getServerName() + ":"
        + request.getServerPort() + request.getContextPath();

UIResults results = UIResults.extractReplay(request);
WaybackRequest wbRequest = results.getWbRequest();
StringFormatter fmt = wbRequest.getFormatter();
CaptureSearchResults cResults = results.getCaptureResults();
%>
	<script type='text/javascript'>
		//alert('test');
	</script>
<%
String exactDateStr = results.getResult().getCaptureTimestamp();
Date exactDate = results.getResult().getCaptureDate();
String searchUrl = wbRequest.getRequestUrl();
String resolution = wbRequest.getTimelineResolution();

if(resolution == null) {
        resolution = WaybackRequest.REQUEST_RESOLUTION_AUTO;
}
String metaChecked = "";
if(wbRequest.isMetaMode()) {
        metaChecked = "checked";
}

CaptureSearchResult first = null;
CaptureSearchResult prev = null;
CaptureSearchResult next = null;
CaptureSearchResult last = null;

long resultCount = cResults.getReturnedCount();
int resultIndex = 1;
Iterator<CaptureSearchResult> it = cResults.iterator();
while(it.hasNext()) {
        CaptureSearchResult res = it.next();
        Date resDate = res.getCaptureDate();

        int compared = resDate.compareTo(exactDate);
        if(compared < 0) {
                resultIndex++;
                prev = res;
                if(first == null) {
                        first = res;
                }
        } else if(compared > 0) {
                last = res;
                if(next == null) {
                        next = res;
                }
        }
}
// string to indicate which select option is currently active
String yearsOptSelected = "";
String monthsOptSelected = "";
String daysOptSelected = "";
String hoursOptSelected = "";
String autoOptSelected = "";

String minResolution = ResultsTimelinePartitionsFactory.getMinResolution(cResults);

String optimal = "";
if(minResolution.equals(WaybackRequest.REQUEST_RESOLUTION_HOURS)) {
        optimal = fmt.format("TimelineView.timeRange.hours");
} else if(minResolution.equals(WaybackRequest.REQUEST_RESOLUTION_DAYS)) {
        optimal = fmt.format("TimelineView.timeRange.days");
} else if(minResolution.equals(WaybackRequest.REQUEST_RESOLUTION_MONTHS)) {
        optimal = fmt.format("TimelineView.timeRange.months");
} else if(minResolution.equals(WaybackRequest.REQUEST_RESOLUTION_TWO_MONTHS)) {
          optimal = fmt.format("TimelineView.timeRange.twomonths");
} else if(minResolution.equals(WaybackRequest.REQUEST_RESOLUTION_YEARS)) {
        optimal = fmt.format("TimelineView.timeRange.years");
} else {
        optimal = fmt.format("TimelineView.timeRange.unknown");
}
String autoOptString = fmt.format("TimelineView.timeRange.auto",optimal);

ArrayList<ResultsPartition> partitions;
if(resolution.equals(WaybackRequest.REQUEST_RESOLUTION_HOURS)) {
        hoursOptSelected = "selected";
        partitions = ResultsTimelinePartitionsFactory.getHour(cResults,wbRequest);
} else if(resolution.equals(WaybackRequest.REQUEST_RESOLUTION_DAYS)) {
        daysOptSelected = "selected";
        partitions = ResultsTimelinePartitionsFactory.getDay(cResults,wbRequest);
} else if(resolution.equals(WaybackRequest.REQUEST_RESOLUTION_MONTHS)) {
        monthsOptSelected = "selected";
        partitions = ResultsTimelinePartitionsFactory.getMonth(cResults,wbRequest);
} else if(resolution.equals(WaybackRequest.REQUEST_RESOLUTION_TWO_MONTHS)) {
          monthsOptSelected = "selected";
          partitions = ResultsTimelinePartitionsFactory.getTwoMonth(cResults,wbRequest);
} else if(resolution.equals(WaybackRequest.REQUEST_RESOLUTION_YEARS)) {
        yearsOptSelected = "selected";
        partitions = ResultsTimelinePartitionsFactory.getYear(cResults,wbRequest);
} else {
        autoOptSelected = "selected";
        partitions = ResultsTimelinePartitionsFactory.getAuto(cResults,wbRequest);
}
int numPartitions = partitions.size();
ResultsPartition firstP = (ResultsPartition) partitions.get(0);
ResultsPartition lastP = (ResultsPartition) partitions.get(numPartitions -1);

String firstDate = firstP.getTitle();
String lastDate = lastP.getTitle();
String titleString = "";
%>
<!--
  ======================================
  BEGIN Wayback INSERTED TIMELINE BANNER

  The following HTML has been inserted
  by the Wayback application to enhance
  the viewing experience, and was not
  part of the original archived content.
  ======================================
-->

<link rel="stylesheet" type="text/css" href="<%= contextRoot %>/css/bl_uk.css" />
<script type="text/javascript" src="<%= contextRoot %>/js/bl_uk.js"></script>
<div id="timeline_header" title="Drag to move this header">
        <span>UK WEB ARCHIVE</span>
        <img id="timeline_vertical" src="<%=contextRoot%>/images/banner_down.png" title="Click to show Timeline" />
</div>
<div id="timeline_banner">
<!--
<div id="wm-ipp" style="color:#0099CC;background-color:white;font-size:10px !important;font-family:Verdana, Arial, Helvetica, sans-serif;padding:5px;width:99%" >
-->
<div id="wm-ipp">
<table cellspacing="0" border="0" cellpadding="0"  width="100%" class="wayback">
        <tr>
                <td width="1" nowrap></td>
                <td width="10%"><a style="display:inline; background: url(); padding: 0 0 0 0;" wmSpecial="1" href="http://www.webarchive.org.uk/ukwa/" ><img style="display: inline;" alt="UK Web Archive" src="/images/ukwa.jpg" border="0"></a>
<br>
<span class="date"><%= fmt.format("TimelineView.viewingVersionDate",exactDate) %></span>&nbsp;&nbsp;
                        <!-- Viewing -->
                        <!--
                        <table cellspacing="0" border="0" cellpadding="0" width="100%" class="wayback">
                                <tr>
                                        <td>
                                                <span class="date"><%= fmt.format("TimelineView.viewingVersion",resultIndex,resultCount) %>&nbsp;</span>
                                        </td>
                                </tr>
                                <tr>
                                        <td nowrap><span class="date"><%= fmt.format("TimelineView.viewingVersionDate",exactDate) %></span>&nbsp;&nbsp;</td>
                                </tr>
                        </table>
                        -->
                </td>
                <td width="80%" align="center">
                        <table class="wayback">
                                <tr>
                                        <td width="50%"></td>
                                        <td>
                                                <table cellspacing="0" border="0" cellpadding="0"  width="100%" class="wayback">
                                                        <tr>
                                                                <td width="48%" nowrap><span><%= firstDate %></span></td>
                                                                <td align="center" valign="bottom" nowrap><img style="display: inline;" wmSpecial="1" src="<%= contextRoot %>/images/mark.png"></td>
                                                                <td width="48%" nowrap align="right"><span><%= lastDate %></span></td>
                                                        </tr>
                                                </table>
                                        </td>
                                        <td width="50%"></td>
                                </tr>
                                <tr>
                                        <td nowrap align="right"><%
                                                titleString = "";
                                                if(first != null) {
                                                        titleString = "title=\"" +
                                                                fmt.format("TimelineView.firstVersionTitle",
                                                                        first.getCaptureDate()) + "\"";
                                                        %><a wmSpecial="1" href="<%= results.resultToReplayUrl(first) %>"><%
                                                }
                                                %><img style="display: inline;" <%= titleString %> wmSpecial="1" border=0 width=19 height=20 src="<%= contextRoot %>/images/first.jpg"><%
                                                if(first != null) {
                                                        %></a><%
                                                }
                                                titleString = "";
                                                if(prev != null) {
                                                        titleString = "title=\"" +
                                                                fmt.format("TimelineView.prevVersionTitle",
                                                                                prev.getCaptureDate()) + "\"";
                                                        %><a wmSpecial="1" href="<%= results.resultToReplayUrl(prev) %>"><%
                                                }
                                                %><img style="display: inline;" <%= titleString %> wmSpecial="1" border=0 width=13 height=20 src="<%= contextRoot %>/images/prev.jpg"><%
                                                if(first != null) {
														%></a><%
                                                }
                                        %></td>
                                        <td nowrap><%

        for(int i = 0; i < numPartitions; i++) {
                ResultsPartition partition = (ResultsPartition) partitions.get(i);
                ArrayList partitionResults = partition.getMatches();
                int numResults = partitionResults.size();
                String imageUrl = contextRoot + "/images/line.jpg";
                String replayUrl = null;
                String prettyDateTime = null;
                if(numResults == 1) {
                        imageUrl = contextRoot + "/images/mark_one.png";
                        CaptureSearchResult result = (CaptureSearchResult) partitionResults.get(0);
                        replayUrl = results.resultToReplayUrl(result);
                        prettyDateTime = fmt.format("TimelineView.markDateTitle",result.getCaptureDate());

                } else if (numResults > 1) {
                        imageUrl = contextRoot + "/images/mark_several.png";
                        CaptureSearchResult result = (CaptureSearchResult) partitionResults.get(numResults - 1);
                        replayUrl = results.resultToReplayUrl(result);
                        prettyDateTime = fmt.format("TimelineView.markDateTitle",result.getCaptureDate());

                }
                if((i > 0) && (i < numPartitions)) {

%><img style="display: inline;" wmSpecial="1" border=0 width=1 height=16 src="<%= contextRoot %>/images/linemark.jpg"><%

                }

                if(replayUrl == null) {

%><img style="display: inline;" wmSpecial="1" border=0 width=7 height=16 src="<%= imageUrl %>"><%

                } else {

%><a wmSpecial="1" href="<%= replayUrl %>"><img style="display: inline;" wmSpecial="1" border=0 width=7 height=16 title="<%= prettyDateTime %>" src="<%= imageUrl %>"></a><%

                }
        }

%></td>
                                        <td nowrap><%
                                                titleString = "";
                                                if(next != null) {
                                                        titleString = "title=\"" +
                                                                fmt.format("TimelineView.nextVersionTitle",
                                                                        next.getCaptureDate()) + "\"";
                                                        %><a wmSpecial="1" href="<%= results.resultToReplayUrl(next) %>"><%
                                                }
                                                %><img style="display: inline;" wmSpecial="1" <%= titleString %> border=0 width=13 height=20 src="<%= contextRoot %>/images/next.jpg"><%
                                                if(first != null) {
                                                        %></a><%
                                                }
                                                titleString = "";
                                                if(last != null) {
                                                        titleString = "title=\"" +
                                                                fmt.format("TimelineView.lastVersionTitle",
                                                                        last.getCaptureDate()) + "\"";
                                                        %><a wmSpecial="1" href="<%= results.resultToReplayUrl(last) %>"><%
                                                }
                                                %><img style="display: inline;" wmSpecial="1" <%= titleString %> border=0 width=19 height=20 src="<%= contextRoot %>/images/last.jpg"><%
                                                if(first != null) {
                                                        %></a><%
                                                }
                                        %></td>
                                </tr>
                                <tr><td height="10" colspan="3"></td></tr>
                                <tr>
                                        <td colspan="3" align="center">
                                        	<br>
                                        	<span id="bl_disclaimer" class="message">External links, forms and search boxes may not function within archived websites.</span>
                                        <%
                                        if ( returnURL != null ) {
                                        %>
                                        	<span id="bl_linktotep">Return link</span>
							                <script language="javascript">
							                	sp = document.getElementById('bl_linktotep');
							                	sp.innerHTML = "<a href='<%=returnURL%>'>Return to instance list</a>";
							                </script>
							            <%
							            }
							            %>  
							           </td>
                                </tr>
                        </table>
                </td>
                <td align="right" width="10%" valign="top">
                        <!-- Resolution -->
                        <!--
                         need to get cookie data passing set up before this can be re-enabled:
                        <form wmSpecial="1" name="timeline" method="GET" target="_top" action="<%= contextRoot + "/frameset" %>">
                                <input type="hidden" name="url" value="<%= searchUrl %>">
                                <input type="hidden" name="exactdate" value="<%= exactDateStr %>">
                                <input type="hidden" name="type" value="urlclosestquery">
                                <%= fmt.format("TimelineView.timeRange") %>
                                <select NAME="resolution" SIZE="1" onChange="changeResolution()">
                                        <option <%= yearsOptSelected %> value="years">
                                                <%= fmt.format("TimelineView.timeRange.years") %>
                                        </option>
                                        <option <%= monthsOptSelected %> value="months">
                                                <%= fmt.format("TimelineView.timeRange.months") %>
                                        </option>
                                        <option <%= daysOptSelected %>  value="days">
                                                <%= fmt.format("TimelineView.timeRange.days") %>
                                        </option>
                                        <option <%= hoursOptSelected %> value="hours">
                                                <%= fmt.format("TimelineView.timeRange.hours") %>
                                        </option>
                                        <option <%= autoOptSelected %> value="auto"><%= autoOptString %></option>
                                </select>&nbsp;<%=
                                        fmt.format("TimelineView.metaDataCheck")
                                %><input type="checkbox" name="<%= WaybackRequest.REQUEST_META_MODE%>" value="<%= WaybackRequest.REQUEST_YES %>" <%=
                                        metaChecked
                                %> onClick="changeMeta()">&nbsp;
                        </form>
      <a wmSpecial="1" href="<%= contextRoot %>/help.jsp" target="_top"><%=
      fmt.format("UIGlobal.helpLink")
      %></a>
-->
<a style="display:inline; background: url(); padding: 0 0 0 0;"  href="http://www.bl.uk" border="0"><img style="display: inline;" alt='The British Library' src='/img/Providedby.gif'  border="0"></a>
                </td>
                <!--
                <td>
                        <img style="display: inline;" wmSpecial="1" alt='' height='1' src='<%= contextRoot %>/images/1px.gif' width='5'>
                </td>
                -->
        </tr>
        <!--
        <tr><td width="1"></td><td nowrap>
                <span class="date"><%= fmt.format("TimelineView.viewingVersionDate",exactDate) %></span>&nbsp;&nbsp;
        </td>
        <td colspan="2" align="center">
                <span id="bl_disclaimer" class="message">External links, forms and search boxes may not function within these archived websites.</span><br>
        </td>
        </tr>
        -->
</table>
</div>
</div>
<script type="text/javascript">
        function getFrameArea( frame )
        {
                if( frame.innerWidth ) {
                        return frame.innerWidth * frame.innerHeight;
                }
                if( frame.document.documentElement && frame.document.documentElement.clientHeight ) {
                        return frame.document.documentElement.clientWidth * frame.document.documentElement.clientHeight;
                }
                if( frame.document.body ) {
                        return frame.document.body.clientWidth * frame.document.body.clientHeight;
                }
                return 0;
        }
/* Hides the Timeline if we're not in the largest frame */
        if( parent != window ) {
                largestArea = 0;
                largestFrame = null;
                for( i=0; i < top.frames.length; i++ ) {
                        frame = top.frames[ i ];
                        area = getFrameArea( frame );
                        if( area > largestArea ) {
                                largestFrame = frame;
                                largestArea = area;
                        }
                }
                if( self != largestFrame ) {
                        document.getElementById( "wm-ipp" ).style.display = "none";
                        document.getElementById( "timeline_banner" ).style.display = "none";
                        document.getElementById( "timeline_header" ).style.display = "none";
                }
        }
        initSlide( "<%=contextRoot%>/images/banner_up.png", "<%=contextRoot%>/images/banner_down.png");
        if( document.cookie && document.cookie.substring( document.cookie.indexOf( "hidden" ) + 7, document.cookie.indexOf( "hidden" ) + 11 ) != "true" ) {
                slideBanner( false );
        }
        dragAttach( document.getElementById( "timeline_header" ) );
        var oldOnload = window.onload;
        window.onload = function()
        {
                if( oldOnload ) oldOnload();
                var timeline = document.getElementById( "wm-ipp" );
                var elements = document.getElementsByTagName( '*' );

/* Checks whether the date is before March 2008 for removal of '?' parameters */
                var dateString = document.location.href.substring( document.location.href.indexOf( "/archive/" ) + 9,  document.location.href.indexOf( "/archive/" ) + 17 );
                var year = parseInt( dateString.substring( 0, 4 ) );
                var month = parseInt( dateString.substring( 4, 6 ) );
                var regx = new RegExp( "\\?.*\\=.*", "g" );
                for( i=0; i < elements.length; i++ ) {
                        if( year < 2008 || ( year == 2008 && month < 3 ) ) {
                                if( ( elements[ i ].tagName == "A" || elements[ i ].tagName == "LINK" || elements[ i ].tagName == "AREA" ) && elements[ i ].href.indexOf( "?" ) != -1 ) {
                                        elements[ i ].href = elements[ i ].href.replace( regx, "" );
                                }
                                if( elements[ i ].tagName == "IMG" && elements[ i ].src.indexOf( "?" ) != -1 ) {
                                        elements[ i ].src = elements[ i ].src.replace( regx, "" );
                                }
                                if( elements[ i ].tagName == "META" && elements[ i ].httpEquiv.toLowerCase() == "refresh" && elements[ i ].content.indexOf( "?" ) != -1  ) {
                                        document.location.href = elements[ i ].content.substring( elements[ i ].content.indexOf( "http" ), elements[ i ].content.lastIndexOf( "?" ) );
                                }
                        }
                        if( elements[ i ].tagName == "OBJECT" && elements[ i ].type == "application/x-shockwave-flash" ) {
                                var newObject = elements[ i ];
                                var newObjectParent = elements[ i ].parentNode;
                                var wmode = document.createElement( "PARAM" );
                                wmode.setAttribute( "name", "wmode" );
                                wmode.setAttribute( "value", "transparent" );
                                newObject.appendChild( wmode );
                                newObject.setAttribute( "wmode", "transparent" );
                                elements[ i ].parentNode.removeChild( elements[ i ] );
                                newObjectParent.appendChild( newObject );
                        }

                }
        }
</script>
<script type="text/javascript">
        var gaJsHost = ( ( "https:" == document.location.protocol) ? "https://ssl." : "http://www." );
        document.write( unescape( "%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E" ) );
</script>
<script type="text/javascript">
        try{
                var pageTracker = _gat._getTracker( "UA-7571526-1" );
                pageTracker._trackPageview();
        } catch( err ) {}
</script>
<script>
//document.getElementsByClassName("video")[0].innerHTML=

</script>
<!--
  ======================================
  END Wayback INSERTED TIMELINE BANNER
  ======================================
-->
                        

                                
														