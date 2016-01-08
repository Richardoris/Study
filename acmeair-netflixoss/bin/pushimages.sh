#!/bin/sh

help()
{
 cat << HELP
   pushimages.sh -remoteServer <the IP of the docker repository> -tag <image tag>  

   positional arguments:
     remoteServer           the IP of the docker repository;
     tag                    image tag;
   example:
     pushimages.sh -remoteServer 10.128.17.216:5000 -tag 1.0.0  
HELP
   exit 0
}

if [ $# -eq 0 ]
    then
        help;
        exit 1
fi

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

docker tag acmeair/webapp $remoteServer/acmeair-webapp:$tag
docker push  $remoteServer/acmeair-webapp:$tag 

docker tag acmeair/auth-service $remoteServer/acmeair-auth-service:$tag
docker push $remoteServer/acmeair-auth-service:$tag  
   
docker tag acmeair/eureka $remoteServer/acmeair-eureka:$tag
docker push $remoteServer/acmeair-eureka:$tag
    
docker tag acmeair/zuul $remoteServer/acmeair-zuul:$tag           
docker push $remoteServer/acmeair-zuul:$tag

docker tag acmeair/tomcat $remoteServer/acmeair-tomcat:$tag         
docker push $remoteServer/acmeair-tomcat:$tag 

docker tag acmeair/loader $remoteServer/acmeair-loader:$tag             
docker push $remoteServer/acmeair-loader:$tag 

docker tag acmeair/cassandra $remoteServer/acmeair-cassandra:$tag          
docker push $remoteServer/acmeair-cassandra:$tag

docker tag acmeair/base $remoteServer/acmeair-base:$tag
docker push $remoteServer/acmeair-base:$tag

docker tag ubuntu $remoteServer/ubuntu:$tag
docker push $remoteServer/ubuntu:$tag

docker tag acmeair/java-agent $remoteServer/acmeair-java-agent:$tag             
docker push $remoteServer/acmeair-java-agent:$tag 

docker tag acmeair/appd-java-agent $remoteServer/acmeair-appd-java-agent:$tag             
docker push $remoteServer/acmeair-appd-java-agent:$tag 
