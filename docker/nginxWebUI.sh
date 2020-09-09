#不存在目标jar,就释放jar
name = "1.9.8";
echo ${name};
if [ ! -f "/home/nginxWebUI/nginxWebUI-${name}.jar" ]; then 
    if [ ! -d "/home/nginxWebUI/" ]; then
       mkdir /home/nginxWebUI/
    fi
    mv /home/nginxWebUI-*.jar /home/nginxWebUI/
fi

echo "执行文件：$0";
echo "参数为：$1";

#启动jar
nohup java -jar -Xmx64m /home/nginxWebUI/nginxWebUI-${name}.jar $1 > /dev/null &