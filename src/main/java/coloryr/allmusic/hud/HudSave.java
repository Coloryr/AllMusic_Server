package coloryr.allmusic.hud;

import coloryr.allmusic.AllMusic;
import coloryr.allmusic.hud.obj.SaveOBJ;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HudSave {
    private static final Map<String, SaveOBJ> huds = new ConcurrentHashMap<>();

    public static SaveOBJ get(String name) {
        if (!huds.containsKey(name)) {
            SaveOBJ obj = AllMusic.getConfig().getDefaultHud().copy();
            huds.put(name, obj);
            DataSql.task(() -> DataSql.addUser(name, obj));
            return obj;
        }
        return huds.get(name);
    }

    public static void add1(String name, SaveOBJ hud) {
        huds.put(name, hud);
    }

    public static void add(String name, SaveOBJ hud) {
        huds.put(name, hud);
        DataSql.task(() -> DataSql.addUser(name, hud));
    }

    public static void save() {
        for (Map.Entry<String, SaveOBJ> item : huds.entrySet()) {
            DataSql.addUser(item.getKey(), item.getValue());
        }
    }
}
