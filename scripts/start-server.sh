#!/bin/bash

echo "--------------- 배포 시작 ---------------"

cd /home/ubuntu/server || exit 1

# 기존 컨테이너 정리
docker compose down --remove-orphans

# 최신 이미지 pull
docker compose pull

# 주석 처리되지 않은 모든 서비스 실행
docker compose up -d

echo "--------------- 배포 완료 ---------------"
