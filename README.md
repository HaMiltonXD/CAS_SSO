# CAS单点登录系统结构说明

本项目包含四个程序：

- `cas-tomcat-5.3.14`：是Tomcat服务器的WAR文件包，需要被部署到Tomcat服务器的webapps目录下。启动CAS服务端需要进入`bin`目录下，启动`startup.bat`。
- `sso_client1/sso_client2`：是CAS单点登陆系统的客户端，启动对应的文件并访问对应端口就可以使用。**端口配置请前往位于resources目录下的`application.properties`文件中进行**
- `UsrManagement`：负责与MySQL进行数据交互，启动可执行文件后依照终端指示即可进行对应操作。
