#!/bin/bash
# 进入项目目录
cd /data/server/open-job/Open-Job/

# 管理员
sudo su

source /etc/profile

# 从仓库拉去项目
git pull --rebase

#拉取项目可能需要时间,等2秒钟
sleep 2

# maven打包 --先clean在打包过程中跳过测试,节省时间
mvnd clean package -Dmaven.skip.test=true

# 打包可能需要一点时间,等十秒钟
sleep 10

#jar包文件路径及名称（目录按照各自配置）
APP_DASHBOARD_NAME=/data/server/open-job/Open-Job/job-dashboard/target/job-dashboard.jar
APP_SAMPLE_NAME=/data/server/open-job/Open-Job/job-sample/target/job-sample.jar

#日志文件路径及名称（目录按照各自配置）
LOG_DASHBOARD_FILE=/data/server/open-job/dashboard.log
LOG_SAMPLE_FILE=/data/server/open-job/node.log

#查询进程，并杀掉当前jar/java程序

sample_pid=`ps -ef|grep $APP_SAMPLE_NAME | grep -v grep | awk '{print $2}'`
kill -9 $sample_pid
echo "$sample_pid进程终止成功"

sleep 2

#判断jar包文件是否存在，如果存在启动jar包，并时时查看启动日志

if test -e $APP_SAMPLE_NAME
then
echo '文件存在,开始启动此程序...'

# 启动jar包，指向日志文件，2>&1 & 表示打开或指向同一个日志文件
#nohup java -jar $APP_NAME > $LOG_FILE 2>&1 &

nohup java -jar -Xms128m -XX:PermSize=128m -XX:MaxPermSize=128m -Dspring.profiles.active=prod $APP_SAMPLE_NAME > $LOG_SAMPLE_FILE 2>&1 &

#nohup java -jar -Xms128m -XX:PermSize=128m -XX:MaxPermSize=128m -Dspring.profiles.active=prod /data/server/open-idea/Open-Idea/idea-server/target/idea-server.jar > /data/server/open-idea/dashboard.log 2>&1 &

#实时查看启动日志（此处正在想办法启动成功后退出）
#tail -f $LOG_FILE

#输出启动成功（上面的查看日志没有退出，所以执行不了，可以去掉）

echo "$APP_SAMPLE_NAME 启动成功..."
else
echo "$LOG_SAMPLE_FILE 文件不存在,请检查。"
fi

#查询进程，并杀掉当前jar/java程序

dashboard_pid=`ps -ef|grep $APP_DASHBOARD_NAME | grep -v grep | awk '{print $2}'`
kill -9 $dashboard_pid
echo "$dashboard_pid进程终止成功"

sleep 2

#判断jar包文件是否存在，如果存在启动jar包，并时时查看启动日志

if test -e $APP_DASHBOARD_NAME
then
echo '文件存在,开始启动此程序...'

# 启动jar包，指向日志文件，2>&1 & 表示打开或指向同一个日志文件
#nohup java -jar $APP_NAME > $LOG_FILE 2>&1 &

nohup java -jar -Xms128m -XX:PermSize=128m -XX:MaxPermSize=128m -Dspring.profiles.active=prod $APP_DASHBOARD_NAME > $LOG_DASHBOARD_FILE 2>&1 &

#nohup java -jar -Xms128m -XX:PermSize=128m -XX:MaxPermSize=128m -Dspring.profiles.active=prod /data/server/open-idea/Open-Idea/idea-server/target/idea-server.jar > /data/server/open-idea/dashboard.log 2>&1 &

#实时查看启动日志（此处正在想办法启动成功后退出）
#tail -f $LOG_FILE

#输出启动成功（上面的查看日志没有退出，所以执行不了，可以去掉）

echo "$APP_DASHBOARD_NAME 启动成功..."
else
echo "$APP_DASHBOARD_NAME 文件不存在,请检查。"
fi
