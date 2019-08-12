# k8s启动集群步骤 #

## 启动单个集群 ##

通过yaml创建集群，新建deployment文件，iota-deploy.yaml内容如下:

```
apiVersion: extensions/v1beta1
kind: Deployment
metadata:
name: trias-cli-deployment
  labels:
    app: trias-cli
spec:
  replicas: 1
  selector:
    matchLabels:
      app: trias-cli
  template:
    metadata:
      labels:
        app: trias-cli
    spec:
      restartPolicy: Always
      containers:
      - name: trias-cli
        image: 172.31.23.215:5000/trias-cli:StreamNet_v1.0.6 
```  

新建service文件，iota-service.yaml  

```
apiVersion: v1
kind: Service
metadata:
  name: trias-cli-service
spec:
    ports:
      - port: 4999
        targetPort: 4999
        name: "trias-cli"
        protocol: TCP
    selector:
        app: trias-cli
    externalIPs:
    - 172.31.28.12
```

创建集群 

```
sudo kubectl create -f iota-deploy.yaml  
sudo kubectl create -f iota-service.yaml
```

注意:  
spec.selector.matchLabels.app和spec.template.metadata.labels.app要一致  
service文件中的spec.selector.app 和deployment中labels要对应  

最后通过 clusterip 和port访问集群

## 启动服务相互调用的两个集群 ##

### 集群2访问集群1中的服务接口 ###

创建集群1

新建iota-server.yaml文件

```
apiVersion: apps/v1beta1
kind: Deployment
metadata:
  name: iota-server
  namespace: default
  labels: 
    app: iota-dt
spec: 
  template: 
    metadata: 
      labels: 
        app: iota-dt
    spec: 
      containers: 
      - name: iota-dt
        image: iota-node:dev
        ports: 
        - containerPort: 14700
          name: iotaport
        - containerPort: 13700
          name: nerighborport
        volumeMounts: 
        - mountPath: /iri/data
          name: iota-data
      volumes: 
      - name: iota-data
        hostPath: 
          path: /data/iri/stest 
---
apiVersion: v1
kind: Service
metadata: 
   name: iota-dt
   namespace: default
spec:
  selector: 
    app: iota-dt
  ports: 
  - name: iotaserverport
    port: 14700
    targetPort: iotaport
```

```
sudo kubectl apply -f iota_server.yaml
```

创建集群2

新建iota-cli-server.yaml文件

```
kind: Deployment
apiVersion: extensions/v1beta1
metadata:
  name: iota-cli
  namespace: default
  labels: 
    app: iota-cli-dt
spec: 
  template: 
    metadata: 
      labels: 
        app: iota-cli-dt
    spec: 
      containers: 
      - name: iota-cli-dt
        image: iota-cli:v1
        imagePullPolicy: IfNotPresent
        ports: 
        - containerPort: 5000
          name: iotacliport
        env: 
        - name: ENABLE_BATCHING
          value: 'true'
        - name: HOST_IP
          value: '10.105.229.214:14700'
---
apiVersion: v1
kind: Service
metadata: 
   name: iota-cli-dt
   namespace: default
spec:
  type: NodePort
  selector: 
    app: iota-cli-dt
  ports: 
  - name: iotacliserverport
    port: 5000
    targetPort: iotacliport
```

创建集群。

```
sudo kubectl apply -f iota-cli-server.yaml;
```

最后通过 iota-cli的clusterip 和port访问集群.
