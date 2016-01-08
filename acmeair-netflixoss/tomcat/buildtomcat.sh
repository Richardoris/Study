#!/bin/sh
readonly tomcatpwd=$(cd "$(dirname "$0")"; pwd)
#change current work dir,because env.sh need load checkversion.sh 
cd $tomcatpwd/../bin/
. $tomcatpwd/../bin/env.sh
cd $tomcatpwd

sed 's/password=""/password="'`$pwgen_cmd -s 32 1`'"/' tomcat-users.xml.template > tomcat-users.xml

$docker_cmd build -t acmeair/tomcat .

