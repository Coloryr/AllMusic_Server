package Color_yr.AllMusic.API;

import Color_yr.AllMusic.MusicAPI.SongInfo.SongInfo;
import Color_yr.AllMusic.MusicAPI.SongLyric.LyricSave;
import Color_yr.AllMusic.MusicAPI.SongSearch.SearchPage;

public interface IMusicAPI {
    SongInfo GetMusic(String ID, String player, boolean isList);

    String GetPlayUrl(String ID);

    void SetList(String ID, Object sender);

    LyricSave getLyric(String ID);

    SearchPage Search(String[] name);

    String GetListMusic();
}
