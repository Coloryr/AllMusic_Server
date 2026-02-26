package com.coloryr.allmusic.server.core.command.sub;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.IMusicApi;
import com.coloryr.allmusic.server.core.command.ACommand;
import com.coloryr.allmusic.server.core.objs.music.SongInfoObj;

public class CommandTest extends ACommand {
    @Override
    public void execute(Object sender, String name, String[] args) {
        if (args.length < 2) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
            return;
        }

        String musicID = null;
        IMusicApi api = null;

        if (args.length == 2) {
            api = AllMusic.MUSIC_APIS.get(AllMusic.getConfig().defaultApi);
            musicID = args[1];
        } else if (args.length == 3) {
            api = AllMusic.MUSIC_APIS.get(args[1]);
            musicID = args[2];
        } else {
            AllMusic.side.sendMessage(sender, "§d[AllMusic3]§2错误的指令");
        }

        if (api == null) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().musicPlay.error2);
            return;
        }

        if (api.checkId(musicID)) {
            AllMusic.side.sendMessage(sender, "§d[AllMusic3]§2正在测试解析" + args[1]);
            try {
                SongInfoObj info = api.getMusic(args[1], "test", false);
                if (info == null) {
                    AllMusic.side.sendMessage(sender, "§d[AllMusic3]§2测试解析失败");
                    return;
                }
                AllMusic.side.sendMessage(sender, "§d[AllMusic3]§2音乐名称 " + info.getName());
                AllMusic.side.sendMessage(sender, "§d[AllMusic3]§2音乐作者 " + info.getAuthor());
                String url = api.getPlayUrl(args[1]);
                AllMusic.side.sendMessage(sender, "§d[AllMusic3]§2播放地址 " + url);
            } catch (Exception e) {
                AllMusic.side.sendMessage(sender, "§d[AllMusic3]§2测试解析错误");
                e.printStackTrace();
            }
        }
    }
}

