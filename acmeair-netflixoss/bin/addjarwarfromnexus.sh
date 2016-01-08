#!/bin/sh
set -e
. ./env.sh

usage()
{
cat <<EOF
usage: $0 options
This script will download AcmeAir source zip from Nexus and copy to docker build folders.
OPTIONS:
   -h      Show this message
   -a      Nexus address
EOF
}

# Read parameters from the Command Line
Nexus_address=
Down_Param=

while getopts "ha:" OPTION
do
     case $OPTION in
         h)
             usage
             exit 1
             ;;
	 a) 
	     Nexus_address=$OPTARG
	     Down_Param="-a $Nexus_address"
	     ;;
     esac
done

#clean cache
echo "Delete cache..."
rm -f ../auth-service/acmeair-auth-service-*.war
rm -f ../auth-service-liberty/acmeair-auth-service*.war
rm -f ../webapp/acmeair-webapp-*.war
rm -f ../webapp-liberty/acmeair-webapp-*.war
rm -f ../loader/acmeair-loader-*.zip
#rm -f ../loader/acmeair-cql.txt 
#rm -f ../loader/acmeair.sql

echo "DownLoad from nexus..."
mkdir -p ../build/
./pullzipfromnexus.sh $Down_Param -i acmeairnetflix:acmeairnetflix:LATEST -p zip > ../build/acmeair-netflixoss.zip

echo "UnZip download file..."
unzip ../build/acmeair-netflixoss.zip -d ../build/


if [ "-mysql" != "${as_suffix}" ];then
   #not mysql jar_war 
   echo "Copy Cassandra Impl"
   cp ../build/acmeair-auth-service-tc7-*.war ../auth-service/acmeair-auth-service-tc7.war
   cp ../build/acmeair-auth-service-tc7-*.war ../auth-service-liberty/acmeair-auth-service-tc7.war
   cp ../build/acmeair-webapp-tc7-*.war ../webapp/acmeair-webapp-tc7.war
   cp ../build/acmeair-webapp-tc7-*.war ../webapp-liberty/acmeair-webapp-tc7.war
   cp ../build/acmeair-loader-*.zip ../loader/
   #remove mysql loader jar
   rm -rf ../loader/acmeair-loader-jpa-*.zip && mv ../loader/acmeair-loader-*.zip ../loader/acmeair-loader.zip
   #cp ../workspace/acmeair-services-astyanax/src/main/resources/acmeair-cql.txt ../loader/
   exit 0
fi
#mysql jar_war
echo "Copy MySQL Impl"
cp ../build/acmeair-auth-service-jpa-tc7-*.war ../auth-service/acmeair-auth-service-tc7.war
cp ../build/acmeair-webapp-jpa-tc7-*.war ../webapp/acmeair-webapp-tc7.war
cp ../build/acmeair-loader-jpa-*.zip ../loader/acmeair-loader.zip
#cp ../workspace/acmeair-services-astyanax/src/main/resources/acmeair-cql.txt ../loader/
