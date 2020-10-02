FROM ubuntu:20.04
MAINTAINER Chenyimeng "cym1102@qq.com"
RUN apt-get clean && apt-get update &&\
	apt-get install -y nginx &&\
	apt-get install -y curl &&\
	apt-get install -y wget	&&\
	cd /tmp &&\
	wget https://kkfileview.keking.cn/server-jre-8u251-linux-x64.tar.gz &&\
	tar -zxf /tmp/server-jre-8u251-linux-x64.tar.gz && mv /tmp/jdk1.8.0_251 /usr/local/ &&\
ENV JAVA_HOME /usr/local/jdk1.8.0_251
ENV CLASSPATH $JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
ENV PATH $PATH:$JAVA_HOME/bin
ADD target/nginxWebUI-*.jar /home/
ADD nginxWebUI.sh /home/
RUN chmod 777 /home/nginxWebUI.sh
ENTRYPOINT ["sh","-c", "/home/nginxWebUI.sh ${BOOT_OPTIONS} && tail -f /dev/null"]
