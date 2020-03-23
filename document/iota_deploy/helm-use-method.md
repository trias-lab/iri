# HELM 安装使用 #

## 用途 ##

作为 Kubernetes 的一个包管理工具，Helm具有如下功能: 
  - 创建新的 chart
  - chart 打包成 tgz 格式
  - 上传 chart 到 chart 仓库或从仓库中下载 chart
  - 在Kubernetes集群中安装或卸载 chart
  - 管理用Helm安装的 chart 的发布周期
  
## 重要概念 ##

Helm 有三个重要概念: 
  - chart：包含了创建Kubernetes的一个应用实例的必要信息
  - config：包含了应用发布配置信息
  - release：是一个 chart 及其配置的一个运行实例
 ![](https://github.com/wangyh2016/mytest/blob/master/images/helm-str.png?raw=true)

Helm Client 是用户命令行工具，其主要负责如下: 
  - 本地 chart 开发
  - 仓库管理
  - 与 Tiller sever 交互
  - 发送预安装的 chart
  - 查询 release 信息
  - 要求升级或卸载已存在的 release
 
Tiller Server是一个部署在Kubernetes集群内部的 server，其与 Helm client、Kubernetes API server 进行交互。Tiller server 主要负责如下: 

  - 监听来自 Helm client 的请求
  - 通过 chart 及其配置构建一次发布
  - 安装 chart 到Kubernetes集群，并跟踪随后的发布
  - 通过与Kubernetes交互升级或卸载 chart
  - 简单的说，client 管理 charts，而 server 管理发布 release


## 安装 ##
打开[helm](https://github.com/helm/helm/releases)根据对应的操作系统下载stable版本，解压后将可执行文件helm拷贝到/usr/bin目录下即可，这样Helm客户端就在这台机器上安装完成了。
安装tiller
```bash
helm init --upgrade --tiller-image cnych/tiller:XXXX --stable-repo-url https://cnych.github.io/kube-charts-mirror/ 
根据下载的helm版本安装对应的tiller版本
 ```  
安装完成之后，执行helm version
![](https://github.com/wangyh2016/mytest/blob/master/images/helm-n.png?raw=true)

## 使用 ##

现在了尝试创建一个 Chart：
![](https://github.com/wangyh2016/mytest/blob/master/images/hello-install.png?raw=true)
通过查看templates目录下面的deployment.yaml文件可以看出默认创建的 Chart 是一个 nginx 服务，具体的每个文件是干什么用的，我们可以前往 Helm 官方文档进行查看，后面会和大家详细讲解的。比如这里我们来安装 1.7.9 这个版本的 nginx，则我们更改 value.yaml 文件下面的 image tag 即可，将默认的 stable 更改为 1.7.9，为了测试方便，我们把 Service 的类型也改成 NodePort
![](https://github.com/wangyh2016/mytest/blob/master/images/temp_modify.png?raw=true)
现在尝试安装下这个 Chart :
![](https://github.com/wangyh2016/mytest/blob/master/images/hello-install.png?raw=true)
![](https://github.com/wangyh2016/mytest/blob/master/images/svc-pods.png?raw=true)
等到 Pod 创建完成后，我们可以根据创建的 Service 的 NodePort 来访问该服务了，然后在浏览器中打开"http://ipaddress:32355" 就可以正常的访问刚刚部署的 nginx 应用了。
![](https://github.com/wangyh2016/mytest/blob/master/images/result.png?raw=true)

查看release: 
![](https://github.com/wangyh2016/mytest/blob/master/images/release.png?raw=true)
打包chart: 
```bash
helm package hello-helm
```  


