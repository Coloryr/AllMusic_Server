import coloryr.allmusic.AllMusic;
import coloryr.allmusic.objs.CookieObj;
import coloryr.allmusic.objs.music.LyricItemObj;
import coloryr.allmusic.music.play.LyricSave;
import coloryr.allmusic.side.IMyLogger;

public class RunTest implements IMyLogger {
    public static void main(String[] arg) {
        AllMusic.cookie = new CookieObj();
        AllMusic.log = new RunTest();
//        LyricSaveObj obj = AllMusic.getMusicApi().getLyric("2003496380");
//        if(obj.isHaveLyric())
//        {
//
//        }
        LyricSave obj = AllMusic.getMusicApi().getLyric("28151022");
        if (obj.isHaveLyric()) {
            int time = 0;
            while (true) {
                boolean res = obj.checkTime(time, true);
                if (res) {
                    String temp = obj.getKly();
                    if (temp != null)
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
