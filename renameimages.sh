#!/bin/bash
images=`docker images | grep "oneapm" | awk '{print $1}'`
for i in $images
do
    newname=`echo "$i" | cut -d / -f 2-3`
    docker tag $i $newname
    docker rmi -f $i 
done
