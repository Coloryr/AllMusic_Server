package com.coloryr.allmusic.server.core.music.api;

import com.coloryr.allmusic.server.core.AllMusic;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MyCookieJar implements CookieJar {
    @Override
    public void saveFromResponse(HttpUrl httpUrl, @NotNull List<Cookie> list) {
        ArrayList<Cookie> cookies = AllMusic.cookie.cookieStore.get("music.163.com");
        if (cookies == null) {
            cookies = new ArrayList<>();
        }
        for (Cookie item : list) {
            for (Cookie item1 : cookies) {
                if (item.name().equalsIgnoreCase(item1.name())) {
                    cookies.remove(item1);
                    break;
                }
            }
            cookies.add(item);
        }
        AllMusic.cookie.cookieStore.put("music.163.com", cookies);
        AllMusic.saveCookie();
    }

    @Override
    public @NotNull List<Cookie> loadForRequest(HttpUrl httpUrl) {
        List<Cookie> cookies = AllMusic.cookie.cookieStore.get("music.163.com");
        return cookies != null ? cookies : new ArrayList<>();
    }
}
