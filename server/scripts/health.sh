#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh
source ${ABSDIR}/switch.sh

IDLE_PORT=$(find_idle_port)

echo "> Health Check Start!"
echo "> IDLE_PORT: $IDLE_PORT"
echo "> curl -k -s https://localhost:$IDLE_PORT/profile"
sleep 10

for RETRY_COUNT in {1..10}
do
  RESPONSE=$(curl -k -s https://localhost:${IDLE_PORT}/profile)
  UP_COUNT=$(echo ${RESPONSE} | grep 'real' | wc -l)

  if [ ${UP_COUNT} -ge 1 ] # real 문자열이 있는지 검증
  then
    echo "> Health Check 성공"
    switch_proxy # 정상적으로 배포 성공하면 다음 배포시 다른 포트를 사용하기 위해 포트 변경!!!
    break
  else
    echo "> Health Check의 응답을 알 수 없거나, 실행 상태가 아닙니다"
    echo "> Health Check: ${RESPONSE}"
  fi

  if [ ${RETRY_COUNT} -eq 10 ]
  then
    echo "> Health Check 실패"
    echo "> Nginx에 연결하지 않고 배포를 종료합니다"
    exit 1
  fi

  echo "> Health Check 연결 실패. 재시도..."
  sleep 10
done