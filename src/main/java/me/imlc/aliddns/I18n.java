package me.imlc.aliddns;

import java.util.Optional;

public class I18n {

    public static final String ZH_CN = "zh_CN";

    public static String startToSetDns(String domain, String ip) {

        if(Optional.ofNullable(System.getenv("LANG")).orElse("").contains(ZH_CN)) {
            return String.format("正常尝试设置 DNS %s -> %s", domain, ip);
        } else {
            return String.format("Start to set DNS %s -> %s", domain, ip);
        }
    }

    public static String setDnsSuccessfully(String requestId, String recordId) {

        if(Optional.ofNullable(System.getenv("LANG")).orElse("").contains(ZH_CN)) {
            return String.format("成功设置 DNS, requestId=%s, recordId=%s", requestId, recordId);
        } else {
            return String.format("Set DNS record successfully, requestId=%s, recordId=%s", requestId, recordId);
        }
    }

}
