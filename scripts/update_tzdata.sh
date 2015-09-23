#!/bin/bash
OSVER=$(cat /etc/redhat-release | grep -oP "release \d" | grep -oP "\d")

echo 
echo "---------------------------------------------------------------------------------------------------"
hostname
echo "OS version: ${OSVER}"

# get rpm
cd /tmp/
if [ "${OSVER}" -eq 5 ]; then
	wget ftp://ftp.wa.bl.uk/files/tzdata-2015d-1.el5.x86_64.rpm
	FILE=/tmp/tzdata-2015d-1.el5.x86_64.rpm

elif [ "${OSVER}" -eq 6 ]; then
	wget ftp://ftp.wa.bl.uk/files/tzdata-2015d-1.el6.noarch.rpm
	FILE=/tmp/tzdata-2015d-1.el6.noarch.rpm
fi

yum localinstall -y --nogpgcheck ${FILE}
