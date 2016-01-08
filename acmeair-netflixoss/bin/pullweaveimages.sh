#!/bin/sh

help()
{
 cat << HELP
   pullweaveimages.sh -remoteServer <the IP of the docker repository> -tag <image tag>  

   positional arguments:
     remoteServer           the IP of the docker repository;
     tag                    image tag;
   example:
     pullweaveimages.sh -remoteServer 10.128.17.216 -tag 1.0.1  
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

docker pull $remoteServer:5000/weaveworks-weaveexec:$tag
docker tag $remoteServer:5000/weaveworks-weaveexec:$tag weaveworks/weaveexec:$tag

docker pull $remoteServer:5000/weaveworks-weave:$tag
docker tag $remoteServer:5000/weaveworks-weave:$tag weaveworks/weave:$tag

docker pull $remoteServer:5000/weaveworks-weavedns:$tag
docker tag $remoteServer:5000/weaveworks-weavedns:$tag weaveworks/weavedns:$tag
