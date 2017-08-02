export WAYBACK_URL_HOST=localhost
export WAYBACK_URL_PORT=8080
export WAYBACK_URL_PREFIX=http://localhost:8080/wayback-ukwa

export CDX_INDEX_SERVER=http://192.168.45.21:8080/data-heritrix
export WAYBACK_HTTPFS_PREFIX=http://hdfs.gtw.wa.bl.uk:14000/webhdfs/v1

export WAYBACK_EXCLUDE_FILE=src/main/resources/exclude.txt
export CDX_WHITELIST=src/test/resources/include.txt
export WAYBACK_EMBARGO=-1

mvn -Dmaven.tomcat.port=8080 -Djava.util.logging.config.file=src/test/resources/logging.properties clean package tomcat7:run-war
