export WAYBACK_URL_PORT=8080
export CDX_INDEX_SERVER=http://localhost:9090/fc
export WAYBACK_EMBARGO=0
export WAYBACK_URL_PREFIX=http://localhost:8080/wayback-ukwa
export CDX_WHITELIST=$PWD/src/test/resources/test-whitelist.txt
export WAYBACK_HTTPFS_PREFIX=http://localhost:50070/webhdfs/v1/1_data/pulse

mvn tomcat7:run-war

