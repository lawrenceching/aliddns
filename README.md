# aliddns

A tool to implement DDNS via Ali DNS service.
Internet access is required for public ip detection.

aliddns will go to www.ip.cn and extract public ip.

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
docker run aliddns \
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
```shell script
java -jar aliddns.jar \
    --accessKeyId <ACCESS_KEY_ID> \
    --accessKeySecret <ACCESS_KEY_SECRET> \
    --rr foo \
    --domain example.com \
    --type A 
```

##### Update Ali DNS Record
```shell script
java -jar aliddns.jar \
    --accessKeyId <ACCESS_KEY_ID> \
    --accessKeySecret <ACCESS_KEY_SECRET> \
    --ip 192.168.0.1 \
    --rr foo \
    --domain example.com \
    --type A 
```
