#!/usr/bin/env python

import os
import zlib
import shutil
import logging
import webhdfs
import requests
from datetime import datetime

UKWA_HDFS_CDX_DIR="/user/rcoram/CDX_WITH_ARK"
CDX_TMP="/wayback/ukwa-dls.tmp"
CDX_PATH="/wayback/cdx-index/ukwa-dls.cdx"
CDX_OLD="/wayback/ukwa-dls.old"

requests.packages.urllib3.disable_warnings()

LOGGING_FORMAT="[%(asctime)s] %(levelname)s: %(message)s"
logging.basicConfig(format=LOGGING_FORMAT, level=logging.DEBUG)
log = logging.getLogger("wayback-beta")
log.setLevel(logging.DEBUG)

w = webhdfs.API(prefix="http://194.66.232.90:14000/webhdfs/v1", user="rcoram")

cdx_dir = w.file(UKWA_HDFS_CDX_DIR)
hdfs_mtime = cdx_dir["FileStatus"]["modificationTime"]/1000
local_mtime = os.stat(CDX_PATH).st_mtime

if hdfs_mtime > local_mtime:
    log.info("Updating UKWA CDX.")
    with open(CDX_TMP, "wb") as o:
        j = w.list(UKWA_HDFS_CDX_DIR)
        log.info("Getting %s parts from HDFS." % len(j["FileStatuses"]["FileStatus"]))
        for file in j["FileStatuses"]["FileStatus"]:
            d = zlib.decompressobj(zlib.MAX_WBITS|32)
            if file["type"] != "FILE":
                continue
            r = w.openstream("%s/%s" % (UKWA_HDFS_CDX_DIR, file["pathSuffix"]))
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
        log.info("Removing old UKWA CDX.")
        os.remove(CDX_PATH)
    log.info("Putting new UKWA CDX in place.")
    shutil.move(CDX_TMP, CDX_PATH)
    os.utime(CDX_PATH, (hdfs_mtime, hdfs_mtime))
else:
    log.info("DLS CDX %s is up to date with %s" % (CDX_PATH,UKWA_HDFS_CDX_DIR))

