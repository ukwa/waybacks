Wayback for UKWA
================

This project build the Wayback we need for the live UKWA Wayback instance.
  i.e. as running at http://www.webarchive.org.uk/wayback/


Extensions:
 * Flowplayer.
 * Google Analytics embedding. [NOT REIMPLEMENTED YET]
 * [ARCHIVED] in page title? [NOT REIMPLEMENTED YET]
 * Controllable banner (minimise, move, etc) [NOT REIMPLEMENTED YET]
 * UK Theme in UI-header/footer JSPs, Toolbar branding, cross-linking, etc. [NOT REIMPLEMENTED YET]
 * Archive Query behaviour (i.e. wayback/archive/* returns XML not HTML). [NOT REIMPLEMENTED YET]
 * Memento access point (wayback/memento/) [NOT REIMPLEMENTED YET]
 * Welsh language access point (wayback/archive-cy)


Welsh Dates
-----------
To support a Welsh UI, we need not just a localized properties file, but also support for Welsh dates and so on. Wayback currently relies on the JVM Locales to support this, but Java does not support Welsh out of the box. However, the ICU4J toolkit can be installed into the JVM and enables automatic Welsh support for dates.

* See http://userguide.icu-project.org/icu4j-locale-service-provider

The current live server has had the ICU4J jars (icu4j-50_1.jar and icu4j-localespi-50_1.jar) installed in /usr/java/jdk1.6.0_12/jre/lib/ext. If Java is upgraded, these JARs will presumably have to be re-installed.


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
