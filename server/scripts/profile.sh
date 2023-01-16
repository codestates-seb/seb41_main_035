#!/usr/bin/env bash

DOMAIN=http://ec2-13-125-30-88.ap-northeast-2.compute.amazonaws.com/auth/profile

# 쉬고있는 profile 찾기
function find_idle_profile() {

    # 현재 애플리케이션이 몇번 포트로 실행되고 있는지 확인
    RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" $DOMAIN)

    if [ "${RESPONSE_CODE}" -ge 400 ] || [ "${RESPONSE_CODE}" -eq 000 ] # 400보다 크거나, 000(TimeOut)이면 -> Error 발생
    then
      CURRENT_PROFILE=real2 # 에러 발생 시 real1 포트로 보내도록 세팅
    else # 정상 상태(200) 이라면
      CURRENT_PROFILE=$(curl -s $DOMAIN) # 사이트에서 현재 사용중인 포트를 응답해줌(real1/real2)
    fi

    if [ "${CURRENT_PROFILE}" == real1 ]
    then
      IDLE_PROFILE=real2;
    else
      IDLE_PROFILE=real1
    fi

    echo "${IDLE_PROFILE}"
}

# 쉬고 있는 profile의 port 찾기
function find_idle_port() {
  IDLE_PROFILE=$(find_idle_profile) # SubShell 호출

  if [ "${IDLE_PROFILE}" == real1 ]
  then
    echo "8081"
  else
    echo "8082"
  fi
}

# 현재 운영중인 포트
function find_current_port() {
  IDLE_PROFILE=$(find_idle_profile) # SubShell 호출

  if [ "${IDLE_PROFILE}" == real1 ]
  then
    echo "8082"
  else
    echo "8081"
  fi
}
