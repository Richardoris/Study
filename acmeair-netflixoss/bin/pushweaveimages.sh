#!/bin/sh

help()
{
 cat << HELP
   pushweaveimages.sh -remoteServer <the IP of the docker repository> -tag <image tag>  

   positional arguments:
     remoteServer           the IP of the docker repository;
     tag                    image tag;
   example:
     pushweaveimages.sh -remoteServer 10.128.17.216 -tag 1.0.1  
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

docker tag weaveworks/weaveexec:$tag $remoteServer:5000/weaveworks-weaveexec:$tag
docker push $remoteServer:5000/weaveworks-weaveexec:$tag

docker tag weaveworks/weavedns:$tag $remoteServer:5000/weaveworks-weavedns:$tag
docker push $remoteServer:5000/weaveworks-weavedns:$tag

docker tag weaveworks/weave:$tag $remoteServer:5000/weaveworks-weave:$tag
docker push $remoteServer:5000/weaveworks-weave:$tag
