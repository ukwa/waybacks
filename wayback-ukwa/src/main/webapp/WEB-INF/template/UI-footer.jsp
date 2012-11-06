<%@ page language="java" pageEncoding="utf-8" contentType="text/html;charset=utf-8"%>
<%@ page import="org.archive.wayback.core.UIResults" %>
<%@ page import="org.archive.wayback.util.StringFormatter" %>
<%
UIResults results = UIResults.getGeneric(request);
StringFormatter fmt = results.getWbRequest().getFormatter();

String staticPrefix = results.getStaticPrefix();
String queryPrefix = results.getQueryPrefix();
String replayPrefix = results.getReplayPrefix();
%>
<!-- FOOTER -->
</div>
        </div>
                </div>
                        </div>
                        <div class="footer">
                                <ol class="footer_nav">
                                        <li><a href="http://www.webarchive.org.uk/ukwa/info/copyright" title="Copyright notice">Copyright notice</a>|</li>
                                        <li><a href="http://www.webarchive.org.uk/ukwa/info/terms" title="Terms of use">Terms of use</a>|</li>
                                        <li><a href="http://www.webarchive.org.uk/ukwa/info/privacy" title="Privacy statement">Privacy statement</a></li>
                                </ol>
                        </div>
<script src="http://www.webarchive.org.uk/images/combined_js.js" type="text/javascript"></script>

                        <script type="text/javascript">
				 var urlString = document.location.href;
                        	 if(urlString.indexOf('www.havering.nhs.uk') != -1) {
                                        var str = document.location.href.replace("www.havering.nhs.uk","havering.live.rss-hosting.co.uk");
                                        //alert(str);
                                        window.location = str;
                        	 }

                                var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
                                document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
                        </script><script src="error_files/ga.js" type="text/javascript"></script>
                        <script type="text/javascript">
                                try {
                                var pageTracker = _gat._getTracker("UA-7571526-1");
                                pageTracker._trackPageview();
                                } catch(err) {}
                        </script>
                        <script type="text/javascript">
                                function GetCookie(name) {
                                var arg=name+"=";
                                var alen=arg.length;
                                var clen=document.cookie.length;
                                var i=0;
                                while (i<clen) {
                                        var j=i+alen;
                                        if (document.cookie.substring(i,j)==arg)
                                        return "here";
                                        i=document.cookie.indexOf(" ",i)+1;
                                        if (i==0) break;
                                        }
                                return null;
                        }
                        </script>
                        
        <!-- Wayback default footer 
		<div align="center">
			<hr noshade size="1" align="center">
			
			<p>
				<a href="<%= staticPrefix %>">
					<%= fmt.format("UIGlobal.homeLink") %>
				</a> |
				<a href="<%= staticPrefix %>help.jsp">
					<%= fmt.format("UIGlobal.helpLink") %>
				</a>
			</p>
		</div>
         -->
	</body>
</html>
<!-- /FOOTER -->
