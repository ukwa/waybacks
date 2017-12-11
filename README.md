UKWA Waybacks
=============

This repository places our various Wayback installations under tighter control. It works using the 'WAR Overlay' method, which means that it take the official, vanilla Wayback WAR file and overlays our changes on the top. This keeps our configuration neatly separated (although a full branch may be worth considering in future).

 * `wayback-ukwa` takes the 'vanilla' OpenWayback distribution and patches it for use as the Open UK Web Archive Wayback service.
 * `wayback-qa` takes `wayback-ukwa` and removes the whitelist part (i.e. let everything be visible)
 * `wayback-ldwa` takes `wayback-ukwa` and adds in the LDL Reading Room playback additions (no whitelist as for `wayback-qa`, but with the single-concurrent-usage lock and set up to use the DLS back end systems).
 

Installation
------------

### General build instructions ###

Note that the UKWA and LDWA builds are slightly different. By default

    mvn clean install

will build each module, and each creates a different WAR for a different deployment context. Each is intended to pick up the necessary configuration from the local environment variables.

Note that it is important that the exclusion list, exclude.txt, is manually copied into place after the 'mvn clean install'. This means the exclusion list is outside of the build, so it can be updated separately without further WAR builds. Where appropriate there is an exclude.txt file in each installation. The built wayback service makes use of this exclusion list via the corresponding environment variable, typically set in /etc/sysconfig/tomcat-wayback.

Until we switch to Docker, deployment of a minor update looks something like:

    $ ssh oukwa-wayback
    $ cd /root/github/waybacks/
    $ git pull
    $ mvn clean install
    $ cd /opt/tomcat_instances/wayback/webapps/
    $ cp /root/github/waybacks/wayback-ukwa/target/wayback.war ROOT.war
    

### Developer build/test instructions ###

As we're using WAR overlays, IDEs can struggle to run the service using their dedicated 'Run on server' tools. However, we can run using Maven. By default 

    mvn clean install tomcat7:run-war

will create and run a WAR suitable for local deployment and testing, and then run it under Tomcat 7. To better control the logging, you can do something like:

    mvn -Djava.util.logging.config.file=src/main/resources/logging.properties clean install tomcat7:run-war
    
Actual configuration is done via environment variables. See [docker-compose.yml](docker-compose.yml) or [this Eclipse screenshot](docs/eclipse-environment-vars.png) for examples suitable for (on-site) testing purposes.

See `wayback-ukwa/run-war-internal.sh` and related scripts for example environment variable settings that should work. Once running, you need to go to the full path including access point, and test using URLs that are covered by the CDX whitelist and not excluded by the exclude file.

e.g. http://localhost:8080/wayback-ukwa/archive/query?type=urlquery&url=http%3A%2F%2Fwww.bl.uk

Requirements
------------

Uses Java 7 and Tomcat 7, plus...

### Welsh Dates ###

To support a Welsh UI, we need not just a localized properties file, but also support for Welsh dates and so on. Wayback currently relies on the JVM Locales to support this, but Java does not support Welsh out of the box. However, the ICU4J toolkit can be installed into the JVM and enables automatic Welsh support for dates. Note, this Welsh language support is installed with the Java service, not the wayback service. Plus, our latest Java installation service installs the icu4j 54_1_1 jars by default.

* See http://userguide.icu-project.org/icu4j-locale-service-provider

The current live server has had the ICU4J jars (icu4j-50_1.jar and icu4j-localespi-50_1.jar) installed in /usr/java/jdk1.6.0_12/jre/lib/ext. If Java is upgraded, these JARs will presumably have to be re-installed.


UKWA Wayback Modifications
--------------------------

The UKWA modifications include:

- JSP/UI modifications:
    - Flowplayer hook.
    - Google Analytics code embedding.
    - UKWA Theme in UI-header/footer JSPs, Toolbar branding, etc.
    - Cross-linking between languages
    - [ARCHIVED] in page title.
    - Links to main site, e.g. disclaimer, T&Cs
- Bean/system modifications:
    - Memento access point (archive/memento/ & archive/list/) enabled.
    - Blocked selected websites via exclude.txt
    - White-list for open access websites, HTTP 451 for reading-room-only content.
    - Welsh language access point (wayback/archive-cy) (n.b. prettyMonths is hard-coded English.)
    - OutbackCDX and HDFS/DLS backends via `RemoteCollection.xml`

Note http://faq.web.archive.org/page-without-wayback-code/ - the id_ suffix thing avoids any re-writing and returns raw responses.

Note also a wierd issue - with Apache 2.2 mod_proxy the return of the 451 code failed and it returns 500 instead. This may be fixed, at least in Apache 2.4 (see [here](http://stackoverflow.com/questions/24775340/apache-reverse-proxy-changes-status-code)), but needs a status message for Apache 2.2, i.e.

    HTTP/1.1 451 Unavailable for Legal Reasons
    
would work, but 

    HTTP/1.1 451

Gets turned into a `500`. To report a `451` properly you need Apache Tomcat 7.0.74 or later (see [here](https://bz.apache.org/bugzilla/show_bug.cgi?id=53602)).

Access Control
--------------

The Open UK Web Archive Wayback service uses two access control mechanisms; an exclude file and an inclusion white-list.

The exclude file silently blocks any matching resources, in case we need to comply with e.g. some kind of take-down order and cannot make certain content available under any conditions. Obviously, this type of information should not be in the indexes, but this mechanism enables us to implement a block without having to modify large indexes immediately.

The white-list, however, is used to declare the subset of our content that is open access. i.e. if it's not in the white-list, you need to come to a reading room to see it. In this case, we emit a [HTTP 451](https://en.wikipedia.org/wiki/HTTP_451) status code with an appropriate message. This allows us to be transparent about our holdings, and offer links to discover related holdings in other web archives.

NOTE: All indexed URLs are visible to URL prefix queries, because the system is not set up to filter UrlSearchResults (see
org.archive.wayback.resourceindex.RemoteResourceIndex.documentToSearchResults(Document, ObjectFilter<CaptureSearchResult>) for details). i.e. if there are any URLs that should not be discoverable, they should be removed from the backing index.

### Concurrent-Use Lock ###

As required by the legislation, we restrict access to each Work in the Non-Print Legal Deposit collection to one user at a time, emulating the existence of one copy per library.  This is achieved through a specialised locking [servlet filter](http://www.oracle.com/technetwork/java/filters-137243.html) that intercepts all requests and locks resources to specific user sessions, based on the user JSESSIONID cookie.

A dedicated page, hosted at `/archive/pages/maintainSessions.jsp` can be used to examine and release locks. Access to this page is restricted to users who have the `administrator` role, via HTTP Basic authentication. It looks like this:

![Maintain Sessions view](./docs/ukwa-npld-lock-session-view-ui.png)

When the users' desktop sessions are closed, the client system can call `/archive/logoff` to force the locks to be released. In case  this doesn't work, all locks are automatically released in the midnight.


To Do
-----

- Add top-level POM to capture common config and make full-builds reliable. DONE but needs testing.
- Override favicon.
- Add the other Wayback configurations.
- Add test harness, e.g. http://localhost:8080/wayback-ukwa/archive/xmlquery.jsp?url=http://www.bl.uk/
- Integrate Memento with Rewrite and Proxy mode
- Add hashes and statuses to Memento Timemaps http://tools.ietf.org/html/draft-snell-atompub-link-extensions-09#section-3.2
- Re-implement controllable banner (minimise, move, main-frame-only, etc)
- Better cross-linking to/from main site too?
- Improve/replace the Captures page (which is struggling with the scale) consider BubbleCalendar or a variation based on Mementos.
- Note that WEB_ARCHIVE_NAME should be set to e.g. "The British Library Legal Deposit Web Archive" for wayback-ldwa.



