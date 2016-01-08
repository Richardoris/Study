#!/bin/sh

. ./env.sh

$docker_cmd run \
--rm -t \
--dns "$dns" \
$dns_search  \
--name loader${as_suffix} -h loader.loader.local.flyacmeair.net \
acmeair/loader${as_suffix}

