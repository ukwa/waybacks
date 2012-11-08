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
