package com.coloryr.allmusic.server.core.command.sub;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.command.ACommand;
import okhttp3.Cookie;

import java.util.ArrayList;
import java.util.HashMap;
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
            Map<String, Cookie> list1 = new HashMap<>();
            for (String item : cookies) {
                String[] cookieitem = item.split("=");
                if (cookieitem.length == 1) {
                    if (list1.containsKey(cookieitem[0])) {
                        continue;
                    }
                    list1.put(cookieitem[0], new Cookie.Builder()
                            .name(cookieitem[0])
                            .domain("163.com")
                            .expiresAt(Long.MAX_VALUE)
                            .build());
                } else {
                    list1.put(cookieitem[0], new Cookie.Builder()
                            .name(cookieitem[0])
                            .value(cookieitem[1])
                            .domain("163.com")
                            .expiresAt(Long.MAX_VALUE)
                            .build());
                }
            }
            AllMusic.cookie.cookieStore.put("music.163.com", new ArrayList<>(list1.values()));

            AllMusic.saveCookie();
        } catch (Exception e) {
            AllMusic.side.sendMessage(sender, "§d[AllMusic3]§c错误的cookie");
            e.printStackTrace();
        }
    }
}
