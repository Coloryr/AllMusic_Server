import coloryr.allmusic.AllMusic;
import coloryr.allmusic.objs.CookieObj;
import coloryr.allmusic.objs.music.LyricItemObj;
import coloryr.allmusic.objs.music.LyricSaveObj;
import coloryr.allmusic.side.IMyLogger;

import java.util.Map;

public class RunTest implements IMyLogger {
    public static void main(String[] arg) {
        AllMusic.cookie = new CookieObj();
        AllMusic.log = new RunTest();
//        LyricSaveObj obj = AllMusic.getMusicApi().getLyric("2003496380");
//        if(obj.isHaveLyric())
//        {
//
//        }
        LyricSaveObj obj = AllMusic.getMusicApi().getLyric("28151022");
        if (obj.isHaveLyric()) {
            LyricItemObj item = obj.checkTime(15970);
            int time = 0;
            while (true) {
                boolean res = item.ktv(time);
                if (res) {
                    String temp = item.getKly();
                    System.out.println(temp);
                }
                time += 10;
            }
        }
    }

    @Override
    public void warning(String data) {

    }

    @Override
    public void info(String data) {

    }
}
