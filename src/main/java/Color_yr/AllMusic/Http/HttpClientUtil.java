package Color_yr.AllMusic.Http;

import Color_yr.AllMusic.AllMusic;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HttpClientUtil {

    private static OkHttpClient client;
    private static final int CONNECT_TIMEOUT = 5;
    private static final int READ_TIMEOUT = 7;
    static {
        try {
            synchronized (OkHttpClient.class) {
                client = new OkHttpClient.Builder()
                        .cookieJar(new CookieJar() {
                            @Override
                            public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {
                                AllMusic.Cookie.cookieStore.put(httpUrl.host(), list);
                                AllMusic.saveCookie();
                            }

                            @NotNull
                            @Override
                            public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
                                List<Cookie> cookies = AllMusic.Cookie.cookieStore.get(httpUrl.host());
                                return cookies != null ? cookies : new ArrayList<>();
                            }
                        })
                        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Res realData(String path, String data) {
        try {
            data = URLEncoder.encode(data, "UTF-8");
            Request request = new Request.Builder().url(path + data)
                    .addHeader("Content-Type", "application/json;charset=UTF-8")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.90 Safari/537.36")
                    .build();
            Response response = client.newCall(request).execute();
            int httpCode = response.code();
            ResponseBody body = response.body();
            if (body == null) {
                AllMusic.log.warning("§d[AllMusic]§c获取网页错误");
                return null;
            }
            InputStream inputStream = body.byteStream();
            boolean ok = httpCode == 200;
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
