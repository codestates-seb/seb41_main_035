#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

function switch_proxy() {
  IDLE_PORT=$(find_idle_port)

  echo "> 전환할 Port: $IDLE_PORT"
  echo "> Port 전환"
  # Nginx가 변경할 프록시 주소 생성해서 파이프라인으로 넘김 | 덮어씌우기
  echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc
  echo "> Nginx Reload"
  sudo service nginx reload # restart(끊김 O) != reload(끊김 X)
}