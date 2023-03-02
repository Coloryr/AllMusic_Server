package coloryr.allmusic.core.utils;

import coloryr.allmusic.core.AllMusic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class Logs {
    private static final Date date = new Date();
    public static File file;
    private static FileWriter fw;
    private static PrintWriter pw;

    public static void logWrite(String text) {
        try {
            if (fw == null)
                fw = new FileWriter(file, true);
            String year = String.format("%tF", date);
            String time = String.format("%tT", date);
            String write = "[" + year + "]" + "[" + time + "]" + text;
            if (pw == null)
                pw = new PrintWriter(fw);
            pw.println(write);
            pw.flush();
            fw.flush();

        } catch (IOException e) {
            AllMusic.log.warning("§c日志文件写入失败");
            e.printStackTrace();
        }
    }

    public static void stop() throws IOException {
        if (fw != null)
            pw.close();
        if (fw != null)
            fw.close();
    }
}
