package Color_yr.AllMusic.Utils;

import Color_yr.AllMusic.AllMusic;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.Socket;

public class RunApi {
    private static Process Process;

    public static boolean runAPI(File DataFolder) {
        try {
            if (isPortUsing()) {
                if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                    Process = Runtime.getRuntime().exec("cmd /c start " + System.getProperty("user.dir") + "\\" + DataFolder + "\\NeteaseCloudMusicApi\\start.cmd");
                } else {
                    Process = Runtime.getRuntime().exec("bash " + System.getProperty("user.dir") + "\\" + DataFolder + "\\NeteaseCloudMusicApi\\start.sh");
                }
                Thread.sleep(500);
                return true;
            } else {
                AllMusic.log.info("§d[AllMusic]§cApi开始失败,端口已被占用,可能是已经启动");
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isPortUsing() {
        try {
            Socket socket = new Socket();
            socket.bind(new InetSocketAddress(4000));
            socket.close();
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    public static void stop() {
        if (Process != null && Process.isAlive()) {
            Process.destroyForcibly();
        }
    }
}
