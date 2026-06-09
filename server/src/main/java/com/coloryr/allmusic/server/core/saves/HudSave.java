package com.coloryr.allmusic.server.core.saves;

import com.coloryr.allmusic.codec.HudPosObj;
import com.coloryr.allmusic.server.core.AllMusic;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HudSave {

    public static final Map<String, HudPosObj> HudList = new ConcurrentHashMap<>();
    public static HudPosObj defaultHud;
    private static File players;
    private static File defaultFile;

    public static void init(File file) throws IOException {
        players = new File(file, "players");
        players.mkdirs();

        defaultFile = new File(file, "hud.json");
        if (!defaultFile.exists()) {
            defaultFile.createNewFile();
        }
    }

    public static void saveDefaultHud() {
        try {
            String data = AllMusic.gson.toJson(defaultHud);
            FileOutputStream out = new FileOutputStream(defaultFile);
            OutputStreamWriter write = new OutputStreamWriter(
                    out, StandardCharsets.UTF_8);
            write.write(data);
            write.close();
            out.close();
        } catch (Exception e) {
            AllMusic.log.data("<light_purple>[AllMusic]<red>配置文件hud.json保存错误");
            e.printStackTrace();
        }
    }

    public static void defaultHudCheck() {
        if (defaultHud == null || defaultHud.check()) {
            defaultHud = HudPosObj.make();
            AllMusic.log.data("<light_purple>[AllMusic]<red>配置文件hud.json错误，已覆盖");
            saveDefaultHud();
        }
    }

    public static void loadHud() throws IOException {
        File[] list = players.listFiles();
        if (list != null) {
            for (File item : list) {
                if (item.isFile() && item.getName().endsWith(".json")) {
                    try {
                        InputStreamReader reader = new InputStreamReader(
                                Files.newInputStream(item.toPath()), StandardCharsets.UTF_8);
                        BufferedReader bf = new BufferedReader(reader);
                        HudPosObj hud = AllMusic.gson.fromJson(bf, HudPosObj.class);
                        bf.close();
                        reader.close();

                        String name = item.getName().replace(".json", "").toLowerCase(Locale.ROOT);
                        if (hud != null) {
                            HudList.put(name, hud);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        InputStreamReader reader = new InputStreamReader(
                Files.newInputStream(defaultFile.toPath()), StandardCharsets.UTF_8);
        BufferedReader bf = new BufferedReader(reader);
        defaultHud = AllMusic.gson.fromJson(bf, HudPosObj.class);
        bf.close();
        reader.close();
        defaultHudCheck();
    }

    private static void saveHud(String name) {
        try {
            HudPosObj hud = HudList.get(name);
            if (hud == null) {
                return;
            }
            String data = AllMusic.gson.toJson(hud);
            File file = new File(players, name + ".json");
            FileOutputStream out = new FileOutputStream(file);
            OutputStreamWriter write = new OutputStreamWriter(
                    out, StandardCharsets.UTF_8);
            write.write(data);
            write.close();
        } catch (Exception e) {
            AllMusic.log.data("<light_purple>[AllMusic]<red>玩家信息保存错误");
            e.printStackTrace();
        }
    }

    /**
     * 检查玩家是否在数据库
     *
     * @param name 用户名
     * @return 结果
     */
    public static boolean check(String name) {
        try {
            name = name.toLowerCase(Locale.ROOT);
            return HudList.containsKey(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static HudPosObj getOrNew(String name) {
        name = name.toLowerCase(Locale.ROOT);
        if (HudList.containsKey(name)) {
            return HudList.get(name);
        }

        HudPosObj hud = defaultHud.copy();
        HudList.put(name, hud);
        String finalName = name;
        SaveTask.task(() -> update(finalName, hud));

        return hud;
    }

    /**
     * 更新玩家Hud数据
     *
     * @param name 用户名
     * @param hud  Hud数据
     */
    public static void update(String name, HudPosObj hud) {
        name = name.toLowerCase(Locale.ROOT);
        HudList.put(name, hud);
        saveHud(name);
    }

    /**
     * 读取所有数据
     */
    public static HudPosObj readHud(String name) {
        name = name.toLowerCase(Locale.ROOT);
        return HudList.get(name);
    }
}
