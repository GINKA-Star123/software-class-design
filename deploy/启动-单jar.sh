#!/usr/bin/env sh
cd "$(dirname "$0")"
if [ ! -f storyworkshop.jar ]; then
  echo "未找到 deploy/storyworkshop.jar。"
  echo "源码仓库请先执行 backend/mvnw -DskipTests package，再复制 backend/target/*.jar 到 deploy/storyworkshop.jar。"
  exit 1
fi
printf "请输入 MySQL root 密码（默认 123456，直接回车使用默认值）："
read -r DBPASS
if [ -z "$DBPASS" ]; then
  DBPASS=123456
fi
java -jar storyworkshop.jar --spring.profiles.active=standalone --spring.datasource.password="$DBPASS"
