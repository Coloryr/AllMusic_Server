package Color_yr.ALLmusic_BC;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class logs {
	public static File file;

	public static Boolean Socket_log = false;
	public static Boolean Group_log = false;
	public static Boolean Send_log = false;
	public static Boolean Error_log = false;

	public void log_write(String text) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(file, true);
			Date date = new Date();
			String year = String.format("%tF", date);
			String time = String.format("%tT", date);
			String write = "[" + year + "]" + "[" + time + "]" + text;
			PrintWriter pw = new PrintWriter(fw);
			pw.println(write);
			pw.flush();
			fw.flush();
			pw.close();
			fw.close();
		} catch (FileNotFoundException e) {
			Minecraft_QQ.log.warning("§d[Minecraft_QQ]§c日志文件写入失败" + e);
		} catch (IOException e) {
			Minecraft_QQ.log.warning("§d[Minecraft_QQ]§c日志文件写入失败" + e);
		}
	}
}
