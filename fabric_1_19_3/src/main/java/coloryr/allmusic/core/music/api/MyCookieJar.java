package coloryr.allmusic.core.music.api;

import coloryr.allmusic.core.AllMusic;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MyCookieJar implements CookieJar {
    @Override
    public void saveFromResponse(HttpUrl httpUrl, @NotNull List<Cookie> list) {
        if (AllMusic.cookie.cookieStore.containsKey(httpUrl.host())) {
            List<Cookie> cookies = AllMusic.cookie.cookieStore.get(httpUrl.host());
            for (Cookie item : list) {
                for (Cookie item1 : cookies) {
                    if (item.name().equalsIgnoreCase(item1.name())) {
                        cookies.remove(item1);
                        break;
                    }
                }
                cookies.add(item);
            }
            AllMusic.cookie.cookieStore.put(httpUrl.host(), cookies);
        } else {
            AllMusic.cookie.cookieStore.put(httpUrl.host(), list);
        }
        AllMusic.saveCookie();
    }

    @Override
    public @NotNull List<Cookie> loadForRequest(HttpUrl httpUrl) {
        List<Cookie> cookies = AllMusic.cookie.cookieStore.get(httpUrl.host());
        return cookies != null ? cookies : new CopyOnWriteArrayList<>();
    }
}
