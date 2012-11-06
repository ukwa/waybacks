Wayback for UKWA
================

This project build the Wayback we need for the live UKWA Wayback instance.
  i.e. as running at http://www.webarchive.org.uk/wayback/


Differences
-----------

Accesss points? 'archive'.


Diff live < > svn

< #Exception.accessControl.title=Blocked Content
< Exception.accessControl.title=Not In Archive
< #Exception.accessControl.message=Access to this content has been blocked. {0}
< Exception.accessControl.message=The document you requested is not in this archive.
HTMLError
---
diff -r wayback/WEB-INF/exception/XMLError.jsp target/waybackhdfs/WEB-INF/exception/XMLError.jsp
11c11
< response.setStatus(e.getStatus());
---
> //response.setStatus(e.getStatus());
----
diff -r wayback/WEB-INF/replay/ArchiveCSSComment.jsp target/waybackhdfs/WEB-INF/replay/ArchiveCSSComment.jsp
----
Only in wayback/WEB-INF/replay: BritishLibraryTimeline.jsp
diff -r wayback/WEB-INF/replay/DisclaimChooser.jsp target/waybackhdfs/WEB-INF/replay/DisclaimChooser.jsp
13,14c13,14
<       %><jsp:include page="/WEB-INF/replay/BritishLibraryTimeline.jsp" flush="true" /><%
< }%>
---
>       %><jsp:include page="/WEB-INF/replay/Toolbar.jsp" flush="true" /><%
> }%>
----
Only in target/waybackhdfs/WEB-INF/replay: ProxyInfo.jsp
diff -r wayback/WEB-INF/replay/Toolbar.jsp target/waybackhdfs/WEB-INF/replay/Toolbar.jsp
----
diff -r wayback/WEB-INF/replay/UrlRedirectNotice.jsp target/waybackhdfs/WEB-INF/replay/UrlRedirectNotice.jsp
----
Only in wayback/WEB-INF/replay: analysis.css
----
Only in wayback/css: bl_uk.css
Only in wayback/images: banner_down.png
Only in wayback/images: banner_up.png
Only in wayback/images: mark.png
Only in wayback/images: mark_one.png
diff -r wayback/index.jsp target/waybackhdfs/index.jsp
----
