#!/bin/bash

echo "--------------- 배포 시작 ---------------"

cd /home/ubuntu/server || exit 1

# 기존 컨테이너 정리
docker compose down

# 최신 이미지 pull
docker compose pull

# Redis + Spring 서비스만 실행
docker compose up -d redis spring

echo "--------------- 배포 완료 ---------------"
