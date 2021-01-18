package Color_yr.AllMusic.Http;

import Color_yr.AllMusic.AllMusic;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class HttpGet {
    private static final HttpClient client;
    private static final RequestConfig defaultConfig;

    static {
        client = HttpClientBuilder.create().build();
        defaultConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD)
                .setConnectTimeout(2000).setSocketTimeout(2000)
                .setConnectionRequestTimeout(2000).build();
    }

    public static Res realData(String path, String data) {
        try {
            data = URLEncoder.encode(data, "UTF-8");
            org.apache.http.client.methods.HttpGet get = new org.apache.http.client.methods.HttpGet(path + data);
            get.setConfig(defaultConfig);
            get.setHeader("Content-Type", "application/json;charset=UTF-8");
            get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.90 Safari/537.36");
            HttpResponse response = client.execute(get);
            InputStream inputStream = response.getEntity().getContent();
            boolean ok = response.getStatusLine().getStatusCode() == 200;
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            String data1 = result.toString(StandardCharsets.UTF_8.name());
            if (!ok) {
                AllMusic.log.warning("§c服务器返回错误：" + data1);
            }
            return new Res(data1, ok);
        } catch (Exception e) {
            AllMusic.log.warning("§c获取网页错误");
            e.printStackTrace();
        }
        return null;
    }
}
