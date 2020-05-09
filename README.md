# nginxWebUI

#### 介绍
nginx网页配置工具


#### 功能说明
本项目是基于springBoot的web系统,数据库使用sqlite,因此服务器上不需要配置任何数据库

其中orm使用了本人自己开源的sqlHelper项目,可以像mongodb一样使用sql数据库,解放开发者对sql数据库表结构的维护工作,有兴趣的可以了解一下https://gitee.com/cym1102/sqlHelper

本项目可以使用WebUI配置nginx的各项功能,包括端口转发,反向代理,ssl证书配置,负载均衡等,最终生成nginx.conf配置文件并覆盖目标配置文件,完成nginx的功能配置. 

nginx本身功能复杂,本项目并不能涵盖nginx所有功能,只能配置常用功能,更高级的功能配置仍然需要在最终生成的nginx.conf中进行手动编写.

#### 安装说明
以Ubuntu操作系统为例:

1.安装nginx,maven,git
```
apt install nginx maven git
```

2.克隆仓库
```
git clone https://gitee.com/cym1102/sqlHelper.git
```

3.配置端口
可修改\nginxWebUI\src\main\resources\application.yml文件
配置server.port: 8080项为需要的端口

4.启动,停止,重启
在\nginxWebUI\目录下,赋予脚本文件可执行权限: chmod 777 *.sh
执行脚本:
./start.sh 启动项目
./stop.sh 停止项目
restart.sh 重启项目

#### 使用说明
