/*
 * This file is part of adventure-platform-fabric, licensed under the MIT License.
 *
 * Copyright (c) 2021 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.coloryr.allmusic.server.adventure.impl;

import java.lang.reflect.Method;
import java.net.URL;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

/**
 * Via i509VCB, a trick to get Brig onto the Knot classpath in order to properly mix in.
 *
 * <p>YOU SHOULD ONLY USE THIS CLASS DURING "preLaunch" and ONLY TARGET A CLASS WHICH IS NOT ANY CLASS YOU MIXIN TO.
 * This will likely not work on Gson because FabricLoader has some special logic related to Gson.</p>
 *
 * <p>Original on GitHub at <a href="https://github.com/i509VCB/Fabric-Junkkyard/blob/ce278daa93804697c745a51af06ec812896ec2ad/src/main/java/me/i509/junkkyard/hacks/PreLaunchHacks.java">i509VCB/Fabric-Junkkyard</a></p>
 */
public class AdventurePrelaunch implements PreLaunchEntrypoint {
  @Override
  public void onPreLaunch() {
    // 在 Fabric 环境中，不需要特殊处理
    if (isFabricEnvironment()) {
      System.out.println("Adventure platform running in Fabric environment");
      return;
    }

    // 非 Fabric 环境，执行原来的逻辑
    try {
      hackilyLoadForMixin("com.mojang.authlib.UserAuthentication");
    } catch (final Exception e) {
      throw new RuntimeException("Failed to setup Adventure platform", e);
    }
  }

  private static boolean isFabricEnvironment() {
    try {
      Class.forName("net.fabricmc.loader.api.FabricLoader");
      return true;
    } catch (ClassNotFoundException e) {
      return false;
    }
  }

  static void hackilyLoadForMixin(final String pathOfAClass) throws Exception {
    final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    final URL url = Class.forName(pathOfAClass).getProtectionDomain().getCodeSource().getLocation();

    // 尝试添加 URL
    try {
      Method addURLMethod = findAddURLMethod(classLoader);
      if (addURLMethod != null) {
        addURLMethod.invoke(classLoader, url);
      }
    } catch (Exception e) {
      System.err.println("Failed to add URL, but continuing: " + e.getMessage());
    }
  }

  private static Method findAddURLMethod(ClassLoader classLoader) {
    Class<?> clazz = classLoader.getClass();
    while (clazz != null) {
      try {
        Method method = clazz.getDeclaredMethod("addURL", URL.class);
        method.setAccessible(true);
        return method;
      } catch (NoSuchMethodException e) {
        clazz = clazz.getSuperclass();
      }
    }
    return null;
  }
}
