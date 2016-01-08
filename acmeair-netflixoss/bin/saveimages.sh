#!/bin/sh

#help()
#{
# cat << HELP
#   pullimages.sh -remoteServer <the IP of the docker repository> -tag <image tag>  

#   positional arguments:
#     remoteServer           the IP of the docker repository;
#     tag                    image tag;
#   example:
#     pullimages.sh -remoteServer 10.128.17.216 -tag 1.0.0  
#     pullimages.sh -remoteServer 10.128.17.216:5000 
#     pullimages.sh -tag 1.0.0
#     pullimages.sh 
#HELP
#   exit 0
#}

#while [ -n "$1" ]; do
#case $1 in
#   -h) help;shift 1;; # function help is called
#   -remoteServer) remoteServer=$2;shift 2;;
#   -tag) tag=$2;shift 2;;
#   -*) echo "error: no such option $1. -h for help";exit 1;;
#   *) help;break;;
#esac
#done

#echo "remoteServer=$remoteServer, tag=$tag"
#if [ "" = "$remoteServer" ]; then
#    remoteServer="docker.oneapm.me"
#fi
#if [ "" != "$tag" ]; then
#    tag=":$tag"
#fi

#docker pull $remoteServer/acmeair/webapp$tag    
#docker tag $remoteServer/acmeair/webapp$tag acmeair/webapp
docker images | grep "acmeair/webapp" | awk '{print $3}' | xargs docker save -o webapp.tar

#docker pull $remoteServer/acmeair/auth-service$tag    
#docker tag $remoteServer/acmeair/auth-service$tag acmeair/auth-service
docker images | grep "acmeair/auth" | awk '{print $3}' | xargs docker save -o auth.tar

#docker pull $remoteServer/acmeair/eureka$tag    
#docker tag $remoteServer/acmeair/eureka$tag acmeair/eureka
docker images | grep "acmeair/eureka"  | awk '{print $3}' | xargs docker save -o eureka.tar

#docker pull $remoteServer/acmeair/zuul$tag    
#docker tag $remoteServer/acmeair/zuul$tag acmeair/zuul
docker images | grep "acmeair/zuul" | awk '{print $3}' |  xargs docker save -o zuul.tar

#docker pull $remoteServer/acmeair/loader$tag   
#docker tag $remoteServer/acmeair/loader$tag acmeair/loader
docker images | grep "acmeair/loader" | awk '{print $3}' |  xargs docker save -o loader.tar

#docker pull $remoteServer/acmeair/cassandra$tag   
#docker tag $remoteServer/acmeair/cassandra$tag acmeair/cassandra
docker images | grep "cassandra" | awk '{print $3}' |  xargs docker save -o cassandra.tar

#docker pull $remoteServer/acmeair/base  
#docker tag $remoteServer/acmeair/base acmeair/base
docker images | grep "acmeair/base" | awk '{print $3}' |  xargs docker save -o base.tar

#docker pull $remoteServer/ubuntu:14.04
#docker tag $remoteServer/ubuntu:14.04 ubuntu:14.04
#docker images | grep "ubuntu:14.04" | awk '{print $3}' |  xargs docker save -o ubuntu.tar
