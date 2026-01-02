import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.objs.CookieObj;
import com.coloryr.allmusic.server.core.objs.music.LyricItemObj;
import com.coloryr.allmusic.server.core.music.play.LyricSave;
import com.coloryr.allmusic.server.core.side.IMyLogger;

public class RunTest implements IMyLogger {
    public static void main(String[] arg) {
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
