package com.jmd.util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {

    // 将字符串复制到剪切板
    public static void setClipboardText(String writeMe) {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection(writeMe);
        clip.setContents(tText, null);
    }

    // 解析URL
    public static ArrayList<String> expandUrl(String url) {
        ArrayList<String> urls = new ArrayList<String>();
        // 解析{a-c}
        Pattern stringPatt = Pattern.compile("\\{([a-z])-([a-z])\\}");
        Matcher stringMatch = stringPatt.matcher(url);
        if (stringMatch.find()) {
            int startCharCode = Character.codePointAt(stringMatch.group(1), 0);
            int stopCharCode = Character.codePointAt(stringMatch.group(2), 0);
            int charCode;
            for (charCode = startCharCode; charCode <= stopCharCode; ++charCode) {
                char c = (char) charCode;
                String r = stringMatch.group(0).replaceAll("\\{", "\\\\{").replaceAll("\\}", "\\\\}");
                urls.add(url.replaceAll(r, String.valueOf(c)));
            }
        }
        // 解析{1-3}
        Pattern numberPatt = Pattern.compile("\\{(\\d+)-(\\d+)\\}");
        Matcher numberMatch = numberPatt.matcher(url);
        if (numberMatch.find()) {
            int stop = Integer.parseInt(numberMatch.group(2));
            for (int i = Integer.parseInt(numberMatch.group(1)); i <= stop; i++) {
                urls.add(url.replace(numberMatch.group(0), String.valueOf(i)));
            }
        }
        if (urls.size() == 0) {
            urls.add(url);
        }
        return urls;
    }

    // URL方言
    public static String getDialectUrl(String tileName, String oriUrl, int z, long x, long y) {
        String url = null;
        if (tileName.indexOf("Bing") == 0) {
            url = oriUrl.replaceAll("\\{&&&&&\\}", GeoUtils.xyz2Bing(z, x, y));
        } else if (tileName.indexOf("Tencent") == 0) {
            var y_all = Math.pow(2, z) - 1;
            var new_y = y_all - y;
            url = oriUrl
                    .replace("{z}", String.valueOf(z))
                    .replace("{x}", String.valueOf(x))
                    .replace("{-y}", String.valueOf(new_y));
        } else {
            url = oriUrl.replace("{z}", String.valueOf(z)).replace("{x}", String.valueOf(x)).replace("{y}",
                    String.valueOf(y));
        }
        return url;
    }

    // 生成n位数字字母混合字符串
    public static String generateCharMixed(int n) {
        String[] chars = {
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
                "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
                "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
                "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
        };
        var res = new StringBuilder();
        for (var i = 0; i < n; i++) {
            var num = (int) Math.round(Math.random() * n);
            res.append(chars[num]);
        }
        return res.toString();
    }

    // 生成n位数字字母混合字符串
    public static String generateCharMixedExt(int n) {
        String[] chars = {
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "+", "=",
                "~", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")",
                "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
                "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
                "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
        };
        var res = new StringBuilder();
        for (var i = 0; i < n; i++) {
            var num = (int) Math.round(Math.random() * n);
            res.append(chars[num]);
        }
        return res.toString();
    }

    public static boolean isMac() {
        return System.getProperties().getProperty("os.name").toUpperCase().contains("MAC OS");
    }

    public static boolean isWindows() {
        return System.getProperties().getProperty("os.name").toUpperCase().contains("WINDOWS");
    }

    public static boolean isWindows10() {
        return System.getProperties().getProperty("os.name").toUpperCase().contains("WINDOWS 10");
    }

    public static boolean isWindows11() {
        return System.getProperties().getProperty("os.name").toUpperCase().contains("WINDOWS 11");
    }

}
