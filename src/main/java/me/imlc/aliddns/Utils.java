package me.imlc.aliddns;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static String detectPublicIp() {
        OkHttpClient client = new OkHttpClient();

        Request req = new Request.Builder()
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36")
                .url("https:/www.ip.cn")
                .build();

        try {
            Response resp = client.newCall(req).execute();
            String body = resp.body().string();

            String regexp = "<p>您现在的 IP：<code>(?<ip>.+)</code></p><p>所在地理位置";
            Pattern pattern = Pattern.compile(regexp);
            Matcher matcher = pattern.matcher(body);
            matcher.find();
            String ip = matcher.group("ip");
            return ip;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
