#!/bin/sh
set -e
. ./env.sh
#clean cache
echo "Delete cache..."
rm -f ../auth-service/acmeair-auth-service-*.war
rm -f ../auth-service-liberty/acmeair-auth-service*.war
rm -f ../webapp/acmeair-webapp-*.war
rm -f ../webapp-liberty/acmeair-webapp-*.war
rm -f ../loader/acmeair-loader-*.zip
rm -f ../loader/acmeair-cql.txt 
rm -f ../loader/acmeair.sql

if [ "-mysql" != "${as_suffix}" ];then
   #not mysql jar_war 
   echo "Copy Cassandra Impl"
   cp ../workspace/acmeair-auth-service-tc7/build/libs/acmeair-auth-service-tc7-0.1.0-SNAPSHOT.war ../auth-service/
   cp ../workspace/acmeair-auth-service-tc7/build/libs/acmeair-auth-service-tc7-0.1.0-SNAPSHOT.war ../auth-service-liberty/
   cp ../workspace/acmeair-webapp-tc7/build/libs/acmeair-webapp-tc7-0.1.0-SNAPSHOT.war ../webapp/
   cp ../workspace/acmeair-webapp-tc7/build/libs/acmeair-webapp-tc7-0.1.0-SNAPSHOT.war ../webapp-liberty/
   cp ../workspace/acmeair-loader/build/distributions/acmeair-loader-0.1.0-SNAPSHOT.zip ../loader/
   cp ../workspace/acmeair-services-astyanax/src/main/resources/acmeair-cql.txt ../loader/
   exit 0
fi
#mysql jar_war
echo "Copy MySQL Impl"
cp ../workspace/acmeair-auth-service-jpa-tc7/build/libs/acmeair-auth-service-jpa-tc7-0.1.0-SNAPSHOT.war ../auth-service/acmeair-auth-service-tc7-0.1.0-SNAPSHOT.war
cp ../workspace/acmeair-webapp-jpa-tc7/build/libs/acmeair-webapp-jpa-tc7-0.1.0-SNAPSHOT.war ../webapp/acmeair-webapp-tc7-0.1.0-SNAPSHOT.war
cp ../workspace/acmeair-loader-jpa/build/distributions/acmeair-loader-jpa-0.1.0-SNAPSHOT.zip ../loader/acmeair-loader-0.1.0-SNAPSHOT.zip
cp ../workspace/acmeair-services-astyanax/src/main/resources/acmeair-cql.txt ../loader/
