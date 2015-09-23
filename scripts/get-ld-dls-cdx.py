#!/usr/bin/env python

import os
import zlib
import shutil
import logging
import webhdfs
import requests
from datetime import datetime

LD_HDFS_CDX_DIR="/wayback/cdx-index/20150610-warcs-in-dls-2014"
CDX_TMP="/wayback/ld-dls.tmp"
CDX_PATH="/wayback/cdx-index/20150610-warcs-in-dls-2014.cdx"

requests.packages.urllib3.disable_warnings()

LOGGING_FORMAT="[%(asctime)s] %(levelname)s: %(message)s"
logging.basicConfig(format=LOGGING_FORMAT, level=logging.DEBUG)
log = logging.getLogger("wayback-beta")
log.setLevel(logging.DEBUG)

w = webhdfs.API(prefix="http://194.66.232.90:14000/webhdfs/v1", user="rcoram")

cdx_dir = w.file(LD_HDFS_CDX_DIR)
hdfs_mtime = cdx_dir["FileStatus"]["modificationTime"]/1000
if os.path.exists(CDX_PATH):
    local_mtime = os.stat(CDX_PATH).st_mtime
else:
    local_mtime = 0

if hdfs_mtime > local_mtime:
    log.info("Updating LD CDX.")
    with open(CDX_TMP, "wb") as o:
        j = w.list(LD_HDFS_CDX_DIR)
        log.info("Getting %s parts from HDFS." % len(j["FileStatuses"]["FileStatus"]))
        for file in j["FileStatuses"]["FileStatus"]:
            d = zlib.decompressobj(zlib.MAX_WBITS|32)
            if file["type"] != "FILE":
                continue
            r = w.openstream("%s/%s" % (LD_HDFS_CDX_DIR, file["pathSuffix"]))
            while True:
                chunk = r.raw.read(4096)
                if not chunk:
                    r.close()
                    break
                dec = d.decompress(chunk)
                if len(dec) == 0:
                    r.close()
                    break
                o.write(dec)
                o.flush()
    if os.path.exists(CDX_PATH):
        log.info("Removing old LD CDX.")
        os.remove(CDX_PATH)
    log.info("Putting new LD CDX in place.")
    shutil.move(CDX_TMP, CDX_PATH)
    os.utime(CDX_PATH, (hdfs_mtime, hdfs_mtime))

