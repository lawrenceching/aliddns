package me.imlc.aliddns;

import com.aliyuncs.AcsRequest;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.*;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class AliDns {

    private Logger logger = LoggerFactory.getLogger(AliDns.class);

    private IAcsClient client;
    private String regionId;
    private IClientProfile profile;

    public AliDns(String accessKeyId, String accessKeySecret) {
        regionId = "cn-hangzhou"; //必填固定值，必须为“cn-hanghou”
        profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        // 若报Can not find endpoint to access异常，请添加以下此行代码
        // DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Alidns", "alidns.aliyuncs.com");
        client = new DefaultAcsClient(profile);
    }

    public List<DescribeDomainRecordsResponse.Record> listDomainRecords(String domain, String rr) throws ClientException {

        DescribeDomainRecordsRequest request = new DescribeDomainRecordsRequest();
        request.setDomainName(domain);
        attachCommonRequestParameters(request);

        DescribeDomainRecordsResponse response = client.getAcsResponse(request);
        return response.getDomainRecords().stream()
                .filter(r -> r.getRR().equals(rr))
                .collect(Collectors.toList());
    }

    public void deleteDomainRecord(String recordId) throws ClientException {
        DeleteDomainRecordRequest request = new DeleteDomainRecordRequest();
        attachCommonRequestParameters(request);

        request.setRecordId(recordId);
        DeleteDomainRecordResponse response = client.getAcsResponse(request);
        logger.debug("Delete domain record: recordId={} requestId={}", response.getRecordId(), response.getRequestId());
    }

    public boolean isExist(String rr, String domain, String ip, String type) throws ClientException {
        CheckDomainRecordRequest request = new CheckDomainRecordRequest();
        request.setDomainName(domain);
        request.setRR(rr);
        request.setType(type);
        request.setValue(ip);
        request.setProtocol(ProtocolType.HTTPS); //指定访问协议
        request.setMethod(MethodType.POST); //指定请求方法
        request.setRegionId("cn-hangzhou");//指定要访问的Region,仅对当前请求生效，不改变client的默认设置。

        CheckDomainRecordResponse response;
        response = client.getAcsResponse(request);
        return response.getIsExist();
    }

    public void setDns(String rr, String domain, String ip, String type) throws ClientException {
        logger.info(I18n.startToSetDns(rr + "." + domain, ip));

        if(isExist(rr, domain, ip, type)) {
            logger.info("{}.{} -> {} is already exist, will not set same record again", rr, domain, ip);
            return;
        }

        List<DescribeDomainRecordsResponse.Record> records = listDomainRecords(domain, rr);

        records.stream().filter(record -> !record.getValue().equals(ip))
                .forEach((record) -> {
                    try {
                        deleteDomainRecord(record.getRecordId());
                    } catch (Throwable e) {
                        String message = String.format("Unable to delete record: domain=%s, rr=%s value=%s recordId=%s",
                                record.getDomainName(),
                                record.getRR(),
                                record.getValue(),
                                record.getRecordId());
                        logger.error(message, e);
                    }
                });

        AddDomainRecordRequest request = new AddDomainRecordRequest();

        request.setDomainName(domain);
        request.setRR(rr);
        request.setType(type);
        request.setValue(ip);

        attachCommonRequestParameters(request);

        AddDomainRecordResponse response = client.getAcsResponse(request);
        logger.info(I18n.setDnsSuccessfully(response.getRequestId(), response.getRecordId()));
    }

    private static void attachCommonRequestParameters(AcsRequest request) {
        request.setProtocol(ProtocolType.HTTPS); //指定访问协议
        request.setMethod(MethodType.POST); //指定请求方法
        request.setRegionId("cn-hangzhou");//指定要访问的Region,仅对当前请求生效，不改变client的默认设置。
    }

}
