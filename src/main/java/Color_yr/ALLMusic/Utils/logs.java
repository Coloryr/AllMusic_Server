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

    public static void log_write(String text) {
        try {
            FileWriter fw = new FileWriter(file, true);
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

    public void Init(File Dir) throws IOException {
        file = new File(Dir, "logs.log");
        if (!logs.file.exists()) {
            logs.file.createNewFile();
        }
    }
}
