package com.jmd.util;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;

public class FontUtils {

    public static Font getIconFont(String name, float size) {
        return getLocalFont("iconfont/" + name, Font.PLAIN, size);
    }

    // 雅黑-Consolas
    public static Font YaHeiConsolas(float size) {
        return getLocalFont("YaHei.Consolas.1.12.ttf", Font.PLAIN, size);
    }

    // 思源黑体-中文
    public static Font SourceHanSansCNNormal(float size) {
        return getLocalFont("SourceHanSansCN-Normal.ttf", Font.PLAIN, size);
    }

    // 获取本地字体
    private static Font getLocalFont(String name, int style, float size) {
        Font definedFont = null;
        try {
            var fontFile = MyFileUtils.getResourceFile("assets/font/" + name);
            definedFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(style, size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        return definedFont;
    }

}
