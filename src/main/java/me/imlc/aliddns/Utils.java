package me.imlc.aliddns;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    public String getPublicIpFromIpIpNet() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request req = new Request.Builder()
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36")
                .url("http://www.ipip.net/")
                .build();

        Response resp = client.newCall(req).execute();
        String body = resp.body().string();

        String regexp = "<li style=\"width: 20%\"><span>IP地址</span><a href=\".*\">(?<ip>.+)</a> </li>";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(body);
        matcher.find();
        String ip = matcher.group("ip");
        return ip;
    }

    public String getPublicIpFromIpCn() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request req = new Request.Builder()
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36")
                .url("https:/www.ip.cn")
                .build();

        Response resp = client.newCall(req).execute();
        String body = resp.body().string();

        String regexp = "<p>您现在的 IP：<code>(?<ip>.+)</code></p><p>所在地理位置";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(body);
        matcher.find();
        String ip = matcher.group("ip");
        return ip;
    }

    public String detectPublicIp() {

        try {
            return this.getPublicIpFromIpCn();
        } catch (IOException e) {
            logger.warn("Unable to get public ip from www.ip.cn");
        }

        try {
            return this.getPublicIpFromIpIpNet();
        } catch (IOException e) {
            logger.warn("Unable to get public ip from www.ipip.net");
        }

        throw new RuntimeException("Unable to get public ip and no source left");
    }

}
