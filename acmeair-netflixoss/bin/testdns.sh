#!/bin/sh

. ./checklinuxversion.sh
. ./env.sh

if command_exists dig;then
	dig @172.17.42.1 +short zuul.*.local.flyacmeair.net
	dig @172.17.42.1 +short eureka.*.local.flyacmeair.net
	if [ "-mysql" = "${as_suffix}" ];then
		dig @172.17.42.1 +short mysql1.*.local.flyacmeair.net
	fi
	dig @172.17.42.1 +short cassandra1.*.local.flyacmeair.net
	dig @172.17.42.1 +short webapp1.*.local.flyacmeair.net
	dig @172.17.42.1 +short auth1.*.local.flyacmeair.net
else
	echo 1 
	echo 1 
	echo 1 
	echo 1 
	echo 1
fi
