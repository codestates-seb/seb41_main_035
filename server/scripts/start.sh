#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

REPOSITORY=/home/ec2-user/lookatme/server
PROJECT_NAME=server

echo "> 새 애플리케이션 배포"
JAR_NAME=$(ls -tr ${REPOSITORY}/*.jar | tail -n 1)

echo "> JAR Name: ${JAR_NAME}"

echo "> ${JAR_NAME} 에 실행 권한 추가"

chmod +x ${JAR_NAME}

echo "> ${JAR_NAME} 실행"

IDLE_PROFILE=$(find_idle_profile) # SubShell을 호출해 함수의 결과값을 리턴받는다

echo "> ${JAR_NAME} 을 profile=${IDLE_PROFILE} 로 실행합니다"

nohup java -jar \
        -Dspring.config.location=classpath:/application.yml,/home/ec2-user/lookatme/server/application-dev.yml \
        -Dspring.profiles.active=dev,${IDLE_PROFILE} \
        ${JAR_NAME} > ${REPOSITORY}/nohup.out 2>&1 &