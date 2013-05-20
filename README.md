UKWA Waybacks
=============

This repository places our various Wayback installations under tighter control. It works using the 'WAR Overlay' method, which means that it take the official, vanilla Wayback WAR file and overlays our changes on the top. This keeps our configuration neatly separated (although a full branch may be worth considering in future).

As we have multiple installations, the 'wayback-ukwa' instance is the config for our default, public Wayback, and any others are then based upon this (as further overlays). For example, wayback-ldwa overlays the required Legal Deposit locking system.

To Do
-----

- Add top-level POM to capture common config and make full-builds reliable. DONE but needs testing.
- Override favicon.
- Add the other Wayback configurations.
- Add test harness, e.g. http://localhost:8080/wayback-ukwa/archive/xmlquery.jsp?url=http://www.bl.uk/
- Integrate Memento with Rewrite and Proxy mode
- Add hashes and statuses to Memento Timemaps http://tools.ietf.org/html/draft-snell-atompub-link-extensions-09#section-3.2


http://ldwa-bl.wayback.wa.bl.uk:8080/wayback/20040504230000/http://www.bl.uk/

Using the command-line tool
---------------------------

To build the tool:

    mvn clean install -Pcli-util


To run it, posting to a suitable Solr server (e.g. ../warc-solr-test-server):

    java -jar -Xmx512M target/warc-indexer-1.1.1-SNAPSHOT-jar-with-dependencies.jar -s http://localhost:8080/ -t output.warc.gz

If you now query Solr, you can explore your web archive contents.


