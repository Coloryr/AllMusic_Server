package Color_yr.ALLMusic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

class logs {
    static File file;
    static FileWriter fw;
    static Date date = new Date();

    public static void log_write(String text) {
        try {
            fw = new FileWriter(file, true);
            String year = String.format("%tF", date);
            String time = String.format("%tT", date);
            String write = "[" + year + "]" + "[" + time + "]" + text;
            PrintWriter pw = new PrintWriter(fw);
            pw.println(write);
            pw.flush();
            fw.flush();
            pw.close();
            fw.close();
        } catch (IOException e) {
            ALLMusic.log.warning("§d[AllMusic]§c日志文件写入失败");
            e.printStackTrace();
        }
    }
}
