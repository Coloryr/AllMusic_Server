package Color_yr.AllMusic.MusicAPI.MusicAPILocal.GetMusicSearch;

import Color_yr.AllMusic.AllMusic;
import Color_yr.AllMusic.Http.Res;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class PostSearch {
    public static Res realData(String ID) {
        try {
            ID = URLEncoder.encode(ID, "UTF-8");
            URL realUrl = new URL("http://music.163.com/api/search/get/web?csrf_token=");
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(4 * 1000);
            connection.setDoOutput(true);
//            connection.setRequestProperty("Host", "music.163.com");
//            connection.setRequestProperty("Origin", "http://music.163.com");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.90 Safari/537.36");
//            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            connection.setRequestProperty("Referer", "http://music.163.com/search/");

            OutputStream out = connection.getOutputStream();
            String PostData = "type=1&offset=0&limit=50&s=" + ID;
            connection.connect();
            out.write(PostData.getBytes());
            out.flush();
            out.close();
            InputStream inputStream;
            boolean ok;
            if (connection.getResponseCode() >= 400) {
                inputStream = connection.getErrorStream();
                ok = false;
            } else {
                inputStream = connection.getInputStream();
                ok = true;
            }
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
