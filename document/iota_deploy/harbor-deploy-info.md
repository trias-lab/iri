# ubuntu下部署harbor私有仓库 #

## docker安装 ##

```
sudo apt-get update
curl -s https://get.docker.com | sh
sudo docker version
sudo pip install docker-compose
```  
## Harbor安装部署 ##
```
wget http://harbor.orientsoft.cn/harbor-v1.5.0/harbor-offline-installer-v1.5.0.tgz
tar -zxvf harbor-offline-installer-v1.5.0.tgz
cd harbor/
修改harbor的配置文件harbor.cfg:
sudo vi harbor.cfg
(图中hostname位置修改成你自己harbor所在节点的ip)
```  
![](https://img-blog.csdnimg.cn/2018120612230231.png?raw=true)  
(图中harbor_admin_password位置可以修改成你想要的密码，也可以不改)
![](https://img-blog.csdnimg.cn/20181206122359168.png?raw=true)
```
安装harbor:
sudo ./install.sh
安装成功后，我们执行下面命令发现一些容器已经起来了，因为harbor是依赖于这些容器的：
```  
![](https://img-blog.csdnimg.cn/2018120612283573.png?raw=true)
这时候，我们在主机的浏览器上可以通过：http://（ip地址）来访问harbor页面，用户名：admin，密码就是你刚刚自己设置的，然后登录:  
![](https://img-blog.csdnimg.cn/20181206123142373.png?raw=true)
![](https://github.com/wangyh2016/storm/blob/master/harbor.png?raw=true)
这样你就可以在这个页面管理自己的仓库了.

## 简单使用 ##
那么，如何简单把自己的镜像push到harbor，或pull过来？
首先，你要设置自己的docker是信任该harbor地址的，这里填写你自己的ip：
sudo vi /etc/docker/daemon.json
![](https://github.com/wangyh2016/storm/blob/master/docker-daemon1.png?raw=true)
```  
保存后，重启docker服务
sudo systemctl daemon-reload
sudo systemctl restart docker
```  
先在刚刚的harbor页面先手动创建一个公开的runtime项目，等下我们就把镜像传到该项目下
![](https://github.com/wangyh2016/storm/blob/master/runtime.png?raw=true)  
```  
如何把自己docker中的某个镜像传过去呢？
我这里docker中已经有了一个tomcat镜像了，你如果刚装好docker还没有任何镜像的话，可以随便pull一个什么应用的镜像下来用：
我们首先要对该即将上传的镜像打个标签，tag的形式是：你的ip/项目名/镜像名
sudo docker tag tomcat 192.168.***.***/runtime/tomcat  
此时，再查看你的镜像，发现成功多出了个你刚刚打完标签的
```  
 ![](https://img-blog.csdnimg.cn/20181206130011361.png?raw=true)
 ``` 
 注意！现在你还不能直接去push，往后你哪个系统需要和自己的harbor仓库交互时，第一次的时候一定要先docker login一下你的harbor，用户名就是admin，密码就是你自己设置的harbor_admin密码：
```  
![](https://github.com/wangyh2016/storm/blob/master/harbor-login.png?raw=true)
登录成功后，以后你就可以将刚刚打好标签的镜像push到你的harbor了：
```    
sudo docker push 192.168.***.***/runtime/tomcat
稍等一会，你就可以在你的harbor界面下，发现项目runtime下有了：
```  
![](https://img-blog.csdnimg.cn/20181206130905487.png?raw=true)
```   
同样，你也可以从你的harbor中去pull你的镜像，很简单就是正常的docker命令：
sudo docker pull 192.168.***.***/runtime/tomcat
```   
