# aliddns

![Java CI with Maven](https://github.com/lawrenceching/aliddns/workflows/Java%20CI%20with%20Maven/badge.svg)

A tool to implement DDNS via Ali DNS service.
Internet access is required for public ip detection.

aliddns will go to www.ip.cn(or other websites that can echo user's ip) and extract public ip.

```text
usage: aliddns
    --accessKeyId <arg>       Aliyun Access Key Id
    --accessKeySecret <arg>   Aliyun Access Key Secret
 -d,--daemon                  Run in daemon mode
    --domain <arg>            Domain, in domain "www.example.com", domain
                              is "example.com"
    --ip <arg>                ip
    --rr <arg>                Record, in domain "www.example.com", record
                              is "www"
 -t,--interval <arg>          Interval in seconds
    --type <arg>              Domain type
```

## Get Started

### Docker 
```bash
docker run lawrenceching/aliddns \
  -d -t 60 \
  --accessKeyId <ACCESS_KEY_ID> \
  --accessKeySecret <ACCESS_KEY_SECRET> \
  --rr foo --domain example.com --type A
```

### Kubernetes

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: aliddns
  labels:
    app: aliddns
spec:
  replicas: 1
  selector:
    matchLabels:
      app: aliddns
  template:
    metadata:
      labels:
        app: aliddns
    spec:
      containers:
        - name: aliddns
          image: lawrenceching/aliddns
          imagePullPolicy: Always
          args: ["-d", "-t", "300", "--accessKeyId", "<ACCESS_KEY_ID>", "--accessKeySecret", "<ACCESS_KEY_SECRET>", "--rr", "foo", "--domain", "example.com", "--type", "A"]
```

### Usage Case

##### DDNS

Add "--daemon" or "-d" argument to run aliddns in daemon mode.
aliddns will detech your current public ip and update AliDNS internally.
```shell script
java -jar aliddns.jar \
    --daemon \
    --accessKeyId <ACCESS_KEY_ID> \
    --accessKeySecret <ACCESS_KEY_SECRET> \
    --rr foo \
    --domain example.com \
    --type A 
```

##### Update Ali DNS Record with public ip
Or you can run aliddns as a command line tool without "-d" option.
So you can integrate aliddns with crontab in Linux or windows timer system. Or create a CronJob in Kubernetes. 
```shell script
java -jar aliddns.jar \
    --accessKeyId <ACCESS_KEY_ID> \
    --accessKeySecret <ACCESS_KEY_SECRET> \
    --rr foo \
    --domain example.com \
    --type A 
```

##### Update Ali DNS Record
By giving "--ip", you can set AliDNS for whatever ip you want.
```shell script
java -jar aliddns.jar \
    --accessKeyId <ACCESS_KEY_ID> \
    --accessKeySecret <ACCESS_KEY_SECRET> \
    --ip 192.168.0.1 \
    --rr foo \
    --domain example.com \
    --type A 
```
