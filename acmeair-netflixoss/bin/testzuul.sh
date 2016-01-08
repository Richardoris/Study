#!/bin/sh

. ./env.sh

zuul_addr=$1

if [ -z "$zuul_addr" ]; then
  zuul_addr=$($docker_cmd ps | grep 'acmeair/zuul' | head -n 1 | cut -d' ' -f1 | xargs $docker_cmd inspect --format '{{.NetworkSettings.IPAddress}}')
fi

for i in `seq 0 1 5`
do
  curl -sL -w "%{http_code} %{url_effective}\\n" -o /dev/null -c cookie.txt --data "login=uid0@email.com&password=password" $zuul_addr/rest/api/login
  curl -sL -w "%{http_code} %{url_effective}\\n" -o /dev/null -b cookie.txt $zuul_addr/rest/api/customer/byid/uid0@email.com
  curl -sL -w "%{http_code} %{url_effective}\\n" -o /dev/null -b cookie.txt --data "fromAirport=CDG&toAirport=JFK&fromDate=2014/03/31&returnDate=2014/03/31&oneWay=false" $zuul_addr/rest/api/flights/queryflights
  curl -sL -w "%{http_code} %{url_effective}\\n" -o /dev/null -b cookie.txt $zuul_addr/rest/api/login/logout?login=uid0@email.com
done

rm cookie.txt
