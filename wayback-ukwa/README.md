Wayback for UKWA
================

This project build the Wayback we need for the live UKWA Wayback instance.
  i.e. as running at http://www.webarchive.org.uk/wayback/


Notes on Legal Deposit Wayback
------------------------------

The main addition is that LDWA mmbeds a special 'locking filter' at the servlet level to provide resource-level locking (see web.xml). Also provides a basic UI where those locks can be inspected.

Additionally:

- Does not link to e.g. the UKWA website or other things outside the secure environment.
- Prevents the Content-Disposition header from being passed through (see [ArchivalUrlReplay.xml](waybacks/wayback-ldwa/src/main/webapp/WEB-INF/ArchivalUrlReplay.xml)) as downloads are not supported.
- Does not use the SURT-based whitelist (so everything is whitelisted) (via environment variable)
- Embargo is set to seven days. (via environment variable)