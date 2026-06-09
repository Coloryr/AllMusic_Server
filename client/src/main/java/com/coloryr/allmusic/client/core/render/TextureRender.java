package com.coloryr.allmusic.client.core.render;

import com.coloryr.allmusic.codec.HudPosType;

public abstract class TextureRender {
    public int width;
    public int height;

    public TextureRender(String texture) {

    }

    /**
     * 绘制图片
     *
     * @param x     X坐标
     * @param y     Y坐标
     * @param alpha 透明度
     */
    public abstract void drawPic(float x, float y, float alpha);

    public abstract void drawPic(float x, float y, float width, float alpha);

    public abstract void drawPic(float x, float y, float width, float height, HudPosType dir, float alpha);
}
