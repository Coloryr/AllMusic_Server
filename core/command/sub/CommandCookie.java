package com.coloryr.allmusic.server.core.command.sub;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.command.ACommand;
import com.coloryr.allmusic.server.core.objs.MyCookie;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CommandCookie extends ACommand {
    @Override
    public void execute(Object sender, String name, String[] args) {
        if (args.length != 2) {
            AllMusic.side.sendMessage(sender, "§d[AllMusic3]§c没有Cookie数据");
            return;
        }
        if (AllMusic.side.isPlayer(sender)) {
            AllMusic.side.sendMessage(sender, "§d[AllMusic3]§c请在控制台上操作");
            return;
        }
        AllMusic.side.sendMessage(sender, "§d[AllMusic3]§d设置网易云账户，请点一首歌测试是否生效");
        try {
            String cookie = args[1];
            String[] cookies = cookie.split(";");
            List<MyCookie> list = new ArrayList<>();
            for (String item : cookies) {
                String[] cookieitem = item.split("=");
                MyCookie cookie1 = new MyCookie();
                if (cookieitem.length == 1) {
                    cookie1.key = cookieitem[0];
                    cookie1.value = "";
                } else {
                    cookie1.key = cookieitem[0];
                    cookie1.value = cookieitem[1];
                }
                list.add(cookie1);
            }
            AllMusic.cookie.cookieStore.put("music.163.com", list);

            AllMusic.saveCookie();
        } catch (Exception e) {
            AllMusic.side.sendMessage(sender, "§d[AllMusic3]§c错误的cookie");
            e.printStackTrace();
        }
    }
}
