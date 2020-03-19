package Color_yr.ALLMusic.Utils;

import Color_yr.ALLMusic.ALLMusic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class logs {
    public static File file;
    private static Date date = new Date();
    private static FileWriter fw;
    private static PrintWriter pw;

    public static void log_write(String text) {
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
            ALLMusic.log.warning("§c日志文件写入失败");
            e.printStackTrace();
        }
    }

    public void Init(File Dir) throws IOException {
        file = new File(Dir, "logs.log");
        if (!logs.file.exists()) {
            logs.file.createNewFile();
        }
    }

    public static void stop() throws IOException {
        if (fw != null)
            pw.close();
        if (fw != null)
            fw.close();
    }
}
