export WAYBACK_URL_PORT=8080
export CDX_INDEX_SERVER=http://localhost:9090/fc
export WAYBACK_EMBARGO=0
export WAYBACK_EXCLUDE_FILE=src/main/resources/exclude.txt
export WAYBACK_URL_PREFIX=http://localhost:8080/wayback-ldwa
export CDX_WHITELIST=
export WAYBACK_HTTPFS_PREFIX=http://localhost:50070/webhdfs/v1/1_data/pulse
export WEB_ARCHIVE_NAME="The British Library Legal Deposit Web Archive"

mvn -Djava.util.logging.config.file=src/test/resources/logging.properties package tomcat7:run-war

