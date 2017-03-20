#!/bin/sh

# Deploy selected WAR
cp /waybacks/wayback-${UKWA_OWB_VERSION}/target/*.war /opt/tomcat/webapps/ROOT.war

# Boot Tomcat:
/opt/tomcat/bin/startup.sh && tail -F /opt/tomcat/logs/catalina.out

