rm *.log
git pull
nohup mvn spring-boot:run > nginxWebUI.log &
tail -f nginxWebUI.log