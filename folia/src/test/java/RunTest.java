import coloryr.allmusic.core.AllMusic;
import coloryr.allmusic.core.objs.CookieObj;
import coloryr.allmusic.core.objs.music.LyricItemObj;
import coloryr.allmusic.core.music.play.LyricSave;
import coloryr.allmusic.core.side.IMyLogger;

public class RunTest implements IMyLogger {
    public static void main(String[] arg) {
        AllMusic.cookie = new CookieObj();
        AllMusic.log = new RunTest();
//        LyricSaveObj obj = AllMusic.getMusicApi().getLyric("2003496380");
//        if(obj.isHaveLyric())
//        {
//
//        }
        //https://music.163.com/song?id=1422767271&userid=124183854
        LyricSave obj = AllMusic.getMusicApi().getLyric("1449794087");
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
