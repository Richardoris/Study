#!/bin/sh

help()
{
 cat << HELP
   pullimages.sh -remoteServer <the IP of the docker repository> -tag <image tag>  

   positional arguments:
     remoteServer           the IP of the docker repository;
     tag                    image tag;
   example:
     pullimages.sh -remoteServer 10.128.17.216 -tag 1.0.0  
     pullimages.sh -remoteServer 10.128.17.216:5000 
     pullimages.sh -tag 1.0.0
     pullimages.sh 
HELP
   exit 0
}

while [ -n "$1" ]; do
case $1 in
   -h) help;shift 1;; # function help is called
   -remoteServer) remoteServer=$2;shift 2;;
   -tag) tag=$2;shift 2;;
   -*) echo "error: no such option $1. -h for help";exit 1;;
   *) help;break;;
esac
done

echo "remoteServer=$remoteServer, tag=$tag"
if [ "" = "$remoteServer" ]; then
    remoteServer="docker.oneapm.me"
fi
if [ "" != "$tag" ]; then
    tag=":$tag"
fi

docker pull $remoteServer/acmeair/webapp$tag    
docker tag $remoteServer/acmeair/webapp$tag acmeair/webapp

docker pull $remoteServer/acmeair/auth-service$tag    
docker tag $remoteServer/acmeair/auth-service$tag acmeair/auth-service

docker pull $remoteServer/acmeair/eureka$tag    
docker tag $remoteServer/acmeair/eureka$tag acmeair/eureka

docker pull $remoteServer/acmeair/zuul$tag    
docker tag $remoteServer/acmeair/zuul$tag acmeair/zuul

docker pull $remoteServer/acmeair/loader$tag   
docker tag $remoteServer/acmeair/loader$tag acmeair/loader

docker pull $remoteServer/acmeair/cassandra$tag   
docker tag $remoteServer/acmeair/cassandra$tag acmeair/cassandra

docker pull $remoteServer/acmeair/base  
docker tag $remoteServer/acmeair/base acmeair/base

docker pull $remoteServer/ubuntu:14.04
docker tag $remoteServer/ubuntu:14.04 ubuntu:14.04
