package com.coloryr.allmusic.server.core.utils;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.IMusicApi;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class MusicApiLoader {
    public static List<IMusicApi> loadFromDirectory(File dir) {
        List<IMusicApi> instances = new ArrayList<>();
        if (!dir.exists() || !dir.isDirectory()) {
            return instances;
        }

        File[] jarFiles = dir.listFiles((d, name) -> name.endsWith(".jar"));
        if (jarFiles == null) return instances;

        for (File jarFile : jarFiles) {
            AllMusic.log.data("<light_purple>[AllMusic]<yellow>尝试加载api：" + jarFile.getName());
            instances.addAll(loadFromJar(jarFile));
        }
        return instances;
    }

    private static List<IMusicApi> loadFromJar(File jarFile) {
        List<IMusicApi> instances = new ArrayList<>();

        try (JarFile jar = new JarFile(jarFile)) {
            Enumeration<JarEntry> entries = jar.entries();
            List<String> classNames = new ArrayList<>();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (name.endsWith(".class")) {
                    String className = name.replace("/", ".")
                            .replace(".class", "");
                    classNames.add(className);
                }
            }

            URL url = jarFile.toURI().toURL();
            try (URLClassLoader classLoader = new URLClassLoader(new URL[]{url}, Thread.currentThread().getContextClassLoader())) {
                for (String className : classNames) {
                    try {
                        Class<?> clazz = classLoader.loadClass(className);
                        if (!clazz.isInterface() && IMusicApi.class.isAssignableFrom(clazz)) {
                            IMusicApi instance = (IMusicApi) clazz.getDeclaredConstructor().newInstance();
                            instances.add(instance);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return instances;
    }
}