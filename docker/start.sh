#!/bin/sh

# Deploy selected WAR
cp /waybacks/wayback-qa/target/*.war /opt/tomcat/webapps/ROOT.war

# Boot Tomcat:
/opt/tomcat/bin/startup.sh && tail -F /opt/tomcat/logs/catalina.out

