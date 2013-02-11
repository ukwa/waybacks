UKWA Waybacks
=============

This repository places our various Wayback installations under tighter control. It works using the 'WAR Overlay' method, which means that it take the official, vanilla Wayback WAR file and overlays our changes on the top. This keeps our configuration neatly separated (although a full branch may be worth considering in future).

As we have multiple installations, the 'wayback-ukwa' instance is the config for our default, public Wayback, and any others are then based upon this (as further overlays). For example, wayback-ldwa overlays the required Legal Deposit locking system.

To Do
-----

- Add top-level POM to capture common config and make full-builds reliable. DONE but needs testing.
- Override favicon.
- Add the other Wayback configurations.


http://ldwa-bl.wayback.wa.bl.uk:8080/wayback/20040504230000/http://www.bl.uk/

