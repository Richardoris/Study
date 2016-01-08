#!/bin/sh

################################################################################
# Copyright (c) 2014 IBM Corp.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
################################################################################

if ! [ -e license-prompt.accepted ]; then
	echo "Please run acceptlicenses.sh before trying to build images"
	exit 1
fi
. ./env.sh
echo "Change Config..."
if [ "-mysql" != "${as_suffix}" ];then
   echo "as_suffix=-mysql" >> env.sh
fi
chmod +x *.sh

usage()
{
cat <<EOF
usage: $0 options
This script will download AcmeAir source zip from Nexus and copy to docker build folders.
OPTIONS:
   -h      Show this message
   -d      Docker registry url
   -n      Build images use Nexus artifact,if parameter is not null, will use the parameter to fetch the nexus artifact
EOF
}

CopyFlag=
while getopts "hnd:" OPTION
do
     case $OPTION in
         h)
             usage
             exit 1
             ;;
         d)
	     REGISTRY_URL="$OPTARG/"
	     ;;
	 n) 
	     echo "**************** Copy the built files ..." 
	     pwd=$(cd "$(dirname "$0")"; pwd) 
	     cd $pwd
	     if [ "$OPTARG" != "" ];then
		PARAM="-a $OPTARG"
	     fi
	     ./addjarwarfromnexus.sh $PARAM 
	     CopyFlag="false"
	     ;;
	 ?)
	     useage
	     exit 1
	     ;;
     esac
done

$docker_cmd pull ${REGISTRY_URL}crosbymichael/skydns
$docker_cmd tag ${REGISTRY_URL}crosbymichael/skydns rosbymichael/skydns
$docker_cmd pull ${REGISTRY_URL}crosbymichael/skydock
$docker_cmd tag ${REGISTRY_URL}crosbymichael/skydock rosbymichael/skydock

if [ ! -f "id_rsa" ]; then
  ssh-keygen -f id_rsa -P '' -t rsa
  chmod 600 id_rsa
fi

echo "**************** Copy the key ..."
cp id_rsa.pub ../base
if [ "$CopyFlag" = "" ];then
    echo "**************** Copy the built files ..." 
    pwd=$(cd "$(dirname "$0")"; pwd)
    cd $pwd
    ./addjarwar.sh
fi
cd ..
#echo "**************** Building acmeair/pwgen"
#$docker_cmd build -t acmeair/pwgen pwgen
#echo "**************** Building acmeair/base"
#$docker_cmd build -t acmeair/base base
echo "**************** pulling  acmeair/pwgen"
$docker_cmd pull ${REGISTRY_URL}acmeair/pwgen
$docker_cmd tag ${REGISTRY_URL}acmeair/pwgen acmeair/pwgen
echo "**************** pulling  acmeair/base"
$docker_cmd pull ${REGISTRY_URL}acmeair/base
$docker_cmd tag ${REGISTRY_URL}acmeair/base acmeair/base
echo "**************** Building acmeair/mysql"
$docker_cmd build -t acmeair/mysql mysql
echo "**************** Building acmeair/loader"
$docker_cmd build -t acmeair/loader-mysql loader
echo "**************** Building acmeair/tomcat"
cd tomcat
./buildtomcat.sh
cd ..
echo "**************** Building acmeair/zuul"
$docker_cmd build -t acmeair/zuul zuul
echo "**************** Building acmeair/eureka"
$docker_cmd build -t acmeair/eureka eureka
echo "**************** Building acmeair/auth-service"
$docker_cmd build -t acmeair/auth-service-mysql auth-service
echo "**************** Building acmeair/webapp"
$docker_cmd build -t acmeair/webapp-mysql webapp
#echo "**************** Building acmeair/microscaler"
#cd microscaler
#./buildmicroscaler.sh
#cd ..
#echo "**************** Building acmeair/microscaler-agent"
#$docker_cmd build -t acmeair/microscaler-agent microscaler-agent
#echo "**************** Building acmeair/asgard"
#$docker_cmd build -t acmeair/asgard asgard
#echo "**************** Building acmeair/ibmjava"
#$docker_cmd build -t acmeair/ibmjava ibmjava
#echo "Building acmeair/liberty"
#$docker_cmd build -t acmeair/liberty liberty
#echo "Building acmeair/auth-service-liberty"
#$docker_cmd build -t acmeair/auth-service-liberty auth-service-liberty
#echo "Building acmeair/webapp-liberty"
#$docker_cmd build -t acmeair/webapp-liberty webapp-liberty
