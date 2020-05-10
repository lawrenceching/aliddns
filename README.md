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