Wayback for UKWA
================

This project build the Wayback we need for the live UKWA Wayback instance.
  i.e. as running at http://www.webarchive.org.uk/wayback/

This is a WAR overlay project, where each Maven module takes an existing WAR and overlays the necessary changes for each class of deployment.

 * `wayback-ukwa` takes the 'vanilla' OpenWayback distribution and patches it for use as the Open UK Web Archive Wayback service.
 * `wayback-qa` takes `wayback-ukwa` and removes the whitelist part (i.e. let everything be visible)
 * `wayback-ldwa` takes `wayback-ukwa` and adds in the LDL Reading Room playback additions (no whitelist as for `wayback-qa`, but with the single-concurrent-usage lock and set up to use the DLS back end systems).


Build Instructions
------------------

As we're using WAR overlays, IDEs can struggle to run the service using their dedicated 'Run on server' tools. However, we can run using Maven. By default 

    mvn clean install tomcat7:run-war

will create a WAR suitable for local deployment and testing, and then run it under Tomcat 7. To better control the logging, you can do something like:

    mvn -Djava.util.logging.config.file=src/main/resources/logging.properties clean install tomcat7:run-war
    
Actual configuration is done via environment variables. See [docker-compose.yml](docker-compose.yml) or [this Eclipse screenshot](docs/eclipse-environment-vars.png) for examples suitable for (on-site) testing purposes.


Access Control
--------------

The Open UK Web Archive Wayback service uses two access control mechanisms; an exclude file and an inclusion white-list.

The exclude file silently blocks any matching resources, in case we need to comply with e.g. some kind of take-down order and cannot make certain content available under any conditions. Obviously, this type of information should not be in the indexes, but this mechanism enables us to implement a block without having to modify large indexes immediately.

The white-list, however, is used to declare the subset of our content that is open access. i.e. if it's not in the white-list, you need to come to a reading room to see it. In this case, we emit a [HTTP 451](https://en.wikipedia.org/wiki/HTTP_451) status code with an appropriate message. This allows us to be transparent about our holdings, and offer links to discover related holdings in other web archives.

NOTE: All indexed URLs are visible to URL prefix queries, because the system is not set up to filter UrlSearchResults (see
org.archive.wayback.resourceindex.RemoteResourceIndex.documentToSearchResults(Document, ObjectFilter<CaptureSearchResult>) for details). i.e. if there are any URLs that should not be discoverable, they should be removed from the backing index.


Extensions
----------

The UKWA modifications include:

 * Flowplayer.
 * Google Analytics embedding.
 * UK Theme in UI-header/footer JSPs, Toolbar branding, etc.
 * Archive Query behaviour (i.e. wayback/archive/* returns XML not HTML).
     * http://www.webarchive.org.uk/wayback/archive/*/http://www.bbc.co.uk
     * Not sure this is what we really want.
     * http://www.webarchive.org.uk/waybackhdfs/archive/xmlquery?url=http://www.nytimes.com
     * http://inkdroid.org/journal/2012/05/03/way-way-back/
     * REMOVED.
 * Memento access point (wayback/memento/ & wayback/list/)
 * Blocked selected websites via exclude.txt
 * White-list for open access websites, HTTP 451 for reading-room-only content.
 * Welsh language access point (wayback/archive-cy)
 * Cross-linking between languages
 * [ARCHIVED] in page title.
 * Embed the warning from resources, rather than directly in the page.
 * prettyMonths is hard-coded English.
 * Welsh translation of 'ARCHIVED'.
 * Welsh disclaimer.

 
 * LATER Re-implement controllable banner (minimise, move, main-frame-only, etc)
 * LATER Better cross-linking to/from main site too?
 * LATER Improve/replace the Captures page (which is struggling with the scale) consider BubbleCalendar or a variation based on Mementos.


Note http://faq.web.archive.org/page-without-wayback-code/ - the id_ suffix thing avoids any re-writing and returns raw responses.



Welsh Dates
-----------
To support a Welsh UI, we need not just a localized properties file, but also support for Welsh dates and so on. Wayback currently relies on the JVM Locales to support this, but Java does not support Welsh out of the box. However, the ICU4J toolkit can be installed into the JVM and enables automatic Welsh support for dates.

* See http://userguide.icu-project.org/icu4j-locale-service-provider

The current live server has had the ICU4J jars (icu4j-50_1.jar and icu4j-localespi-50_1.jar) installed in /usr/java/jdk1.6.0_12/jre/lib/ext. If Java is upgraded, these JARs will presumably have to be re-installed.

Memento Problems
----------------

 * "format" flag has changed, and ORE.jsp needed changing to keep up. Should use MementoConstants.
 * Memento TimeBundle leads to a re-direct loop.

Start here:
  882  curl -v "http://www.webarchive.org.uk/waybackhdfs/memento/timegate/http://www.bbc.co.uk/"
  883  curl -V "http://www.webarchive.org.uk/waybackhdfs/list/timebundle/http://www.bbc.co.uk/"
  884  curl -v "http://www.webarchive.org.uk/waybackhdfs/list/timebundle/http://www.bbc.co.uk/"
Goes back to:
  885  curl -v "http://www.webarchive.org.uk/waybackhdfs/memento/timemap/rdf/http://www.bbc.co.uk/"
Instead of:
  886  curl -v "http://www.webarchive.org.uk/waybackhdfs/list/timemap/rdf/http://www.bbc.co.uk/"


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
