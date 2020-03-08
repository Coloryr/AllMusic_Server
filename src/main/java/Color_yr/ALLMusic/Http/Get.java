package Color_yr.ALLMusic.Http;

import Color_yr.ALLMusic.ALLMusic;;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Get {

    public static String realData(String path, String data) {
        try {
            data = URLEncoder.encode(data, "UTF-8");
            URL realUrl = new URL(path + data);
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(4 * 1000);
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.connect();
            InputStream inputStream;
            if (connection.getResponseCode() >= 400) {
                inputStream = connection.getErrorStream();
            } else {
                inputStream = connection.getInputStream();
            }
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString(StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            ALLMusic.log.warning("§d[ALLMusic]§c获取网页错误");
            e.printStackTrace();
        }
        return null;
    }
}
