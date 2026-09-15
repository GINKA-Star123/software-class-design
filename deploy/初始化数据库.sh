#!/usr/bin/env sh
cd "$(dirname "$0")"
printf "请输入 MySQL root 密码（默认 123456，直接回车使用默认值）："
read -r DBPASS
if [ -z "$DBPASS" ]; then
  DBPASS=123456
fi
mysql -uroot "-p$DBPASS" < ../database/schema.sql || exit 1
mysql -uroot "-p$DBPASS" < ../database/seed.sql || exit 1
mysql -uroot "-p$DBPASS" < ../database/sample_stories.sql || exit 1
echo "数据库初始化完成。"
