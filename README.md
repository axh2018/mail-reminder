# mail-reminder

#### 介绍
邮件提醒小助手

#### 软件架构
`springboot`，` docker`，` docker-compose`

#### 安装教程
1.  安装`git`， `docker`， `docker-compose`


    `sudo apt install git curl vim -y`
    
    `sudo curl -sSL https://get.docker.com/ | sh`
    
    `sudo curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose`
    
2.  克隆仓库


    `git clone https://github.com/axh2018/mail-reminder.git`
    
3.  进入`mail-reminder`目录


    `cd mail-reminder `
    
4. 编辑`application.yml`将邮箱发件人配置，收件人信息和城市按需填写

   （城市编码见`adcode.xlsl`  填写时注意`yaml`格式对齐）
   
   `vim src/main/resources/application.yml`

5. `build`镜像


   `docker build -t mail-reminder .`

6. 运行


   `docker-compose up -d`
   停止
   
   `docker-compose down`
   查看日志
   
   `docker logs mail-reminder `

#### 使用说明

1.  `docker build`出来的镜像330M左右
2.  应用后台内存占用100M左右

#### 参与贡献

1.  `Fork` 本仓库
2.  提交代码
3.  新建 `Pull Request`
