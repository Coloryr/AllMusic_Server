package com.coloryr.allmusic.server.core.command;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.objs.UserCookie;
import com.google.gson.reflect.TypeToken;
import okhttp3.Cookie;

import java.util.ArrayList;
import java.util.List;


public class CommandCookie extends ACommand {
    @Override
    public void ex(Object sender, String name, String[] args) {
        if (args.length != 2) {
            AllMusic.side.sendMessage(sender, "§d[AllMusic3]§c没有手机验证码");
            return;
        }
        AllMusic.side.sendMessage(sender, "§d[AllMusic3]§d登录网易云账户");
        try {
            String cookie = args[1];
            String[] cookies = cookie.split(";");
            ArrayList<Cookie> list1 = new ArrayList<>();
            for(String item : cookies) {
                String[] cookieitem = item.split("=");
                if (cookieitem.length == 1) {
                    list1.add(new Cookie.Builder()
                            .name(cookieitem[0])
                            .domain("163.com")
                            .expiresAt(Long.MAX_VALUE)
                            .build());
                } else {
                    list1.add(new Cookie.Builder()
                            .name(cookieitem[0])
                            .value(cookieitem[1])
                            .domain("163.com")
                            .expiresAt(Long.MAX_VALUE)
                            .build());
                }
            }
            AllMusic.cookie.cookieStore.put("music.163.com", list1);

            AllMusic.saveCookie();
        } catch (Exception e) {
            AllMusic.side.sendMessage(sender, "§d[AllMusic3]§c错误的cookie");
            e.printStackTrace();
        }
    }
}
