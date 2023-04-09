package com.jmd.util;

import com.luciad.imageio.webp.WebPWriteParam;
import org.apache.commons.io.FileUtils;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

public class CommonUtils {

    // 将字符串复制到剪切板
    public static void setClipboardText(String writeMe) {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection(writeMe);
        clip.setContents(tText, null);
    }

    // 对象转文件
    public static void saveObj2File(Object obj, String filePath) throws IOException {
        FileOutputStream fos = new FileOutputStream(filePath);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fos);
        objectOutputStream.writeObject(obj);
        objectOutputStream.close();
        fos.close();
    }

    // 文件转对象
    public static Object readFile2Obj(String filePath) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(filePath);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object obj = ois.readObject();
        fis.close();
        return obj;
    }

    // 文件转对象
    public static Object readFile2Obj(File file) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object obj = ois.readObject();
        fis.close();
        return obj;
    }

    // 检查并更正文件路径
    public static String checkFilePathAndName(String pathAndName) {
        return pathAndName.replace("\\", "/");
    }

    // 保存PNG
    public static long savePNG(byte[] pngBytes, String pathAndName) throws IOException {
        var file = new File(pathAndName);
        FileUtils.writeByteArrayToFile(file, pngBytes);
        if (file.exists() && file.isFile()) {
            return file.length();
        } else {
            return -1;
        }
    }

    // 保存PNG为WEBP
    public static long savePNG2WEBP(byte[] pngBytes, String pathAndName) throws IOException {
        // Obtain an image to encode from somewhere
        var image = ImageIO.read(new ByteArrayInputStream(pngBytes));
        // Obtain a WebP ImageWriter instance
        var writer = ImageIO.getImageWritersByMIMEType("image/webp").next();
        // Configure encoding parameters
        var writeParam = new WebPWriteParam(writer.getLocale());
        writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        writeParam.setCompressionType(writeParam.getCompressionTypes()[WebPWriteParam.LOSSLESS_COMPRESSION]);
        // Configure the output on the ImageOutputStream
        var file = new File(pathAndName);
        FileUtils.createParentDirectories(file);
        writer.setOutput(new FileImageOutputStream(file));
        writer.write(null, new IIOImage(image, null, null), writeParam);
        writer.dispose();
        if (file.exists() && file.isFile()) {
            return file.length();
        } else {
            return -1;
        }
    }

    // 保存PNG为JPG
    public static long savePNG2JPG(byte[] pngBytes, float quality, String pathAndName) throws IOException {
        // 格式转换
        BufferedImage oriImg = ImageIO.read(new ByteArrayInputStream(pngBytes));
        BufferedImage newImg = new BufferedImage(oriImg.getWidth(), oriImg.getHeight(), BufferedImage.TYPE_INT_RGB);
        newImg.createGraphics().drawImage(oriImg, 0, 0, Color.WHITE, null);
        // 压缩图片
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
        ImageWriteParam iwp = writer.getDefaultWriteParam();
        iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        iwp.setCompressionQuality(quality);
        // 输出
        var file = new File(pathAndName);
        FileUtils.createParentDirectories(file);
        writer.setOutput(new FileImageOutputStream(file));
        writer.write(null, new IIOImage(newImg, null, null), iwp);
        writer.dispose();
        if (file.exists() && file.isFile()) {
            return file.length();
        } else {
            return -1;
        }
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

    // 静态方法：生成n位数字字母混合字符串
    public static String generateCharMixed(int n) {
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
