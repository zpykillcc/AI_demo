# AI_demo

 SpringBoot+MySQL实现登录的后端功能  端口号8081

 前端地址  [zpykillcc/vue_login (github.com)](https://github.com/zpykillcc/vue_login) 

++++++++++

**管理工具：**maven

**IDE：** IDEA

**数据库：** MySQL

**测试工具：**Postman

++++++++++++++





## 创建数据库

端口3306

+ 按Win+R打开“运行”，输入cmd

 ![cmd](https://www.makerhu.com/posts/5b2ca0db/20210629231452.png) 



+ 输入`mysql -u root -p`后输入密码，登录MySQL

[![登录MySQL](https://www.makerhu.com/posts/5b2ca0db/20210629231749.png)](https://www.makerhu.com/posts/5b2ca0db/20210629231749.png)



+ 创建数据库`create database logindemo`**logindemo**为数据库名，根据你的情况修改

[![创建数据库](https://www.makerhu.com/posts/5b2ca0db/20210629232246.png)](https://www.makerhu.com/posts/5b2ca0db/20210629232246.png)



+ 进入数据库use logindemo 

 ![进入数据库](https://www.makerhu.com/posts/5b2ca0db/20210629232535.png) 



+ 

```mysql
CREATE TABLE user
(
    uid int(10) primary key NOT NULL AUTO_INCREMENT,
    uname varchar(30) NOT NULL,
    password varchar(255) NOT NULL,
    avator varchar(1000),
    UNIQUE (uname)
);
```



## 简要功能

完成登陆、注册、上传头像、上传，User静态资源获取等

同时与算法测Stable diffusion交互获取虚拟形象，与facenet交互进行人脸录入和人脸识别登录



