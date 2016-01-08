#!/bin/sh

. ./env.sh

max=$($docker_cmd ps -a | grep 'mysql[0-9]\+ *$' | sed 's/.*mysql\([0-9]\+\).*/\1/' | sort -n | tail -n 1)
num=$($docker_cmd run --rm ubuntu expr $max + 1)
$docker_cmd run \
-d -t \
--dns "$dns" \
$dns_search \
--name mysql${num} -h mysql${num}.mysql.local.flyacmeair.net \
acmeair/mysql

