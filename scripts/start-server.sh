#!/bin/bash

echo "--------------- 서버 배포 시작 -----------------"

docker stop mountaincc-server || true
docker rm mountaincc-server || true
docker pull 058264467328.dkr.ecr.ap-northeast-2.amazonaws.com/mountaincc-server:latest
docker run -d --name mountaincc-server -p 8080:8080 058264467328.dkr.ecr.ap-northeast-2.amazonaws.com/mountaincc-server:latest

echo "--------------- 서버 배포 끝 -----------------"