package Color_yr.AllMusic.Http;

import Color_yr.AllMusic.AllMusic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class HttpRequest {

    public static File downloadNet(String urlStr, String fileName, String savePath) throws MalformedURLException {
        // 下载网络文件
        int bytesum = 0;
        int byteread = 0;

        URL url = new URL(urlStr + fileName);

        try {
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            FileOutputStream fs = new FileOutputStream(savePath + "\\" + fileName);

            int index = 0;
            byte[] buffer = new byte[1204];
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                if (index >= 5000) {
                    AllMusic.log.info("已经下载了 " + bytesum + " byte");
                    index = 0;
                }
                fs.write(buffer, 0, byteread);
                index++;
            }

            inStream.close();
            fs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new File(savePath + "\\" + fileName);
    }

}