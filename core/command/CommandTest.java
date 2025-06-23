package com.coloryr.allmusic.server.core.command;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.music.api.APIMain;
import com.coloryr.allmusic.server.core.objs.music.SongInfoObj;
import com.coloryr.allmusic.server.core.utils.Function;

public class CommandTest extends ACommand {
    @Override
    public void execute(Object sender, String name, String[] args) {
        if (args.length != 2) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
            return;
        }
        if (Function.isInteger(args[1])) {
            AllMusic.side.sendMessage(sender, "§d[AllMusic3]§2正在测试解析" + args[1]);
            try {
                SongInfoObj info = AllMusic.getMusicApi().getMusic(args[1], "test", false);
                if (info == null) {
                    AllMusic.side.sendMessage(sender, "§d[AllMusic3]§2测试解析失败");
                    return;
                }
                AllMusic.side.sendMessage(sender, "§d[AllMusic3]§2音乐名称 " + info.getName());
                AllMusic.side.sendMessage(sender, "§d[AllMusic3]§2音乐作者 " + info.getAuthor());
                String url = AllMusic.getMusicApi().getPlayUrl(args[1]);
                AllMusic.side.sendMessage(sender, "§d[AllMusic3]§2播放地址 " + url);
            } catch (Exception e) {
                AllMusic.side.sendMessage(sender, "§d[AllMusic3]§2测试解析错误");
                e.printStackTrace();
            }
        } else {
            AllMusic.side.sendMessage(sender, "§d[AllMusic3]§2正在测试解析" + args[1]);

            new Thread(() -> {
                try {
                    SongInfoObj info = APIMain.getUrlMusic(args[1]);
                    AllMusic.side.runTask(() -> {
                        AllMusic.side.sendMessage(sender, "§d[AllMusic3]§2音乐解析类型：" + info.getMediaType() + " 音乐长度：" + info.getLength() / 1000);
                    });
                } catch (Exception e) {
                    AllMusic.side.runTask(() -> {
                        AllMusic.side.sendMessage(sender, "§d[AllMusic3]§2测试解析错误");
                        e.printStackTrace();
                    });
                }
            }).start();
        }
    }
}

