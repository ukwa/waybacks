UKWA Waybacks
=============

This repository places our various Wayback installations under tighter control. It works using the 'WAR Overlay' method, which means that it take the official, vanilla Wayback WAR file and overlays our changes on the top. This keeps our configuration neatly separated (although a full branch may be worth considering in future).

As we have multiple installations, the 'wayback-ukwa' instance is the config for our default, public Wayback, and any others are then based upon this (as further overlays). For example, wayback-ldwa overlays the required Legal Deposit locking system.

Installation
------------

Note that the UKWA and LDWA builds are slightly different. For UKWA, by default

    mvn clean install

will create a WAR suitable for local deployment and testing. The production version is built using

    mvn clean install -Pproduction

and that version expects to be deployed at webarchive.org.uk/wayback/. There is also a version that deploys as /wayback-beta/, for live system test prior to production launch.

    mvn clean install -Pproduction-beta

which is otherwise identical to the production build.

The LDWA Wayback is always built in production mode, and is intended to pick up the necessary configuration from the local environment variables.

Welsh Dates
-----------
To support a Welsh UI, we need not just a localized properties file, but also support for Welsh dates and so on. Wayback currently relies on the JVM Locales to support this, but Java does not support Welsh out of the box. However, the ICU4J toolkit can be installed into the JVM and enables automatic Welsh support for dates.

* See http://userguide.icu-project.org/icu4j-locale-service-provider

The current live server has had the ICU4J jars (icu4j-50_1.jar and icu4j-localespi-50_1.jar) installed in /usr/java/jdk1.6.0_12/jre/lib/ext. If Java is upgraded, these JARs will presumably have to be re-installed.


To Do
-----

- Add top-level POM to capture common config and make full-builds reliable. DONE but needs testing.
- Override favicon.
- Add the other Wayback configurations.
- Add test harness, e.g. http://localhost:8080/wayback-ukwa/archive/xmlquery.jsp?url=http://www.bl.uk/
- Integrate Memento with Rewrite and Proxy mode
- Add hashes and statuses to Memento Timemaps http://tools.ietf.org/html/draft-snell-atompub-link-extensions-09#section-3.2


http://ldwa-bl.wayback.wa.bl.uk:8080/wayback/20040504230000/http://www.bl.uk/

