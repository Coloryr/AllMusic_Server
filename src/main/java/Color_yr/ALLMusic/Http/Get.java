package Color_yr.ALLMusic.Http;

import Color_yr.ALLMusic.ALLMusic;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Header;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Get {

    public static int getMusicTime(String music) {
        try {
            URL urlfile = new URL(music);
            URLConnection con = urlfile.openConnection();
            int b = con.getContentLength();// 得到音乐文件的总长度
            BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
            Bitstream bt = new Bitstream(bis);
            Header h = bt.readFrame();
            return (int) h.total_ms(b) / 1000;
        } catch (Exception e) {
            ALLMusic.log.warning("§d[ALLMusic]§c获取音乐长度错误");
            e.printStackTrace();
        }
        return 0;
    }

    public static String realURL(String path) {
        try {
            URL realUrl = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.connect();
            connection.getContent();
            return connection.getURL().toString();
        } catch (Exception e) {
            ALLMusic.log.warning("§d[ALLMusic]§c获取网页错误");
            e.printStackTrace();
        }
        return null;
    }

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
