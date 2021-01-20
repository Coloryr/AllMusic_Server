package Color_yr.AllMusic.Utils;

import Color_yr.AllMusic.AllMusic;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.*;
import java.util.Enumeration;

public class UnZip {

    public static void unZip(File zipFile, String outDir) {
        ZipFile zip = null;
        try {
            File outFileDir = new File(outDir);
            if (!outFileDir.exists()) {
                outFileDir.mkdirs();
            }

            zip = new ZipFile(zipFile);
            for (Enumeration enumeration = zip.getEntries(); enumeration.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) enumeration.nextElement();
                String zipEntryName = entry.getName();
                InputStream in = zip.getInputStream(entry);

                if (entry.isDirectory()) {
                    File fileDir = new File(outDir + zipEntryName);
                    fileDir.mkdir();
                    continue;
                }

                File file = new File(outDir, zipEntryName);
                OutputStream out = new FileOutputStream(file);
                byte[] buff = new byte[1024];
                int len;
                while ((len = in.read(buff)) > 0) {
                    out.write(buff, 0, len);
                }

                in.close();
                out.close();
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c解压失败");
            e.printStackTrace();
        } finally {
            if (zip != null) {
                try {
                    zip.close();
                    zipFile.delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
