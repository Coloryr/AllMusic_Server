package Color_yr.AllMusic.Http;

import Color_yr.AllMusic.AllMusic;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

public class HttpClientUtil {
    private static HttpClient client;
    private static RequestConfig defaultConfig;

    static {
        try {
            defaultConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD)
                    .setConnectTimeout(5000).setSocketTimeout(5000)
                    .setConnectionRequestTimeout(5000).build();
            class AnyTrustStrategy implements TrustStrategy {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) {
                    return true;
                }
            }
            RegistryBuilder registryBuilder = RegistryBuilder.create();
            ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
            registryBuilder.register("http", plainSF);
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(trustStore, new AnyTrustStrategy()).build();
            LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext);
            registryBuilder.register("https", sslSF);
            Registry registry = registryBuilder.build();
            PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
            connManager.setMaxTotal(200);
            connManager.setDefaultMaxPerRoute(200);
            client = HttpClientBuilder.create().setConnectionManager(connManager).setDefaultCookieStore(new BasicCookieStore()).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Res realData(String path, String data) {
        try {
            data = URLEncoder.encode(data, "UTF-8");
            HttpGet get = new HttpGet(path + data);
            get.setConfig(defaultConfig);
            get.setHeader("Content-Type", "application/json;charset=UTF-8");
            get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.90 Safari/537.36");
            HttpResponse response = client.execute(get);
            InputStream inputStream = response.getEntity().getContent();
            boolean ok = response.getStatusLine().getStatusCode() == 200;
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            String data1 = result.toString(StandardCharsets.UTF_8.name());
            if (!ok) {
                AllMusic.log.warning("§d[AllMusic]§c服务器返回错误：" + data1);
            }
            return new Res(data1, ok);
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c获取网页错误");
            e.printStackTrace();
        }
        return null;
    }
}
