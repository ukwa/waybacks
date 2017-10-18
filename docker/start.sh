#!/bin/sh

# Deploy selected WAR
cp /waybacks/wayback-${UKWA_OWB_VERSION}/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Boot Tomcat:
/usr/local/tomcat/bin/startup.sh && tail -F /usr/local/tomcat/logs/catalina.out

