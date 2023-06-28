package com.jmd.util;

import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class ImageUtils {

    public enum Type {
        PNG('P', 'N', 'G', '-', "PNG"),
        JPG('J', 'F', 'I', 'F', "JPG"),
        GIF('G', 'I', 'F', '-', "GIF"),
        WEBP('R', 'I', 'F', 'F', "WEBP");

        @Getter
        private final char a, b, c, d;

        @Getter
        private final String type;

        Type(char a, char b, char c, char d, String type) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.type = type;
        }
    }

    public static Type getImageType(byte[] data) {
        if (data == null || data.length <= 10) {
            return null;
        }
        if (Type.PNG.getA() == data[1] && Type.PNG.getB() == data[2] && Type.PNG.getC() == data[3]) {
            return Type.PNG;
        }
        if (Type.GIF.getA() == data[0] && Type.GIF.getB() == data[1] && Type.GIF.getC() == data[2]) {
            return Type.GIF;
        }
        if (Type.JPG.getA() == data[6] && Type.JPG.getB() == data[7] && Type.JPG.getC() == data[8] && Type.JPG.getD() == data[9]) {
            return Type.JPG;
        }
        if (Type.WEBP.getA() == data[0] && Type.WEBP.getB() == data[1] && Type.WEBP.getC() == data[2] && Type.JPG.getD() == data[3] &&
                Type.WEBP.getType().equals(new String(new char[]{(char) data[8], (char) data[9], (char) data[10], (char) data[11]}))) {
            return Type.WEBP;
        }
        return null;
    }

    // 直接保存图片
    public static long saveImageDirectly(byte[] bytes, String pathAndName) throws IOException {
        var file = new File(pathAndName);
        FileUtils.writeByteArrayToFile(file, bytes);
        if (file.exists() && file.isFile()) {
            return file.length();
        } else {
            return -1;
        }
    }

    // 通过OpenCV保存图片
    public static long saveImageByOpenCV(byte[] bytes, String pathAndName) throws IOException {
        var file = new File(pathAndName);
        FileUtils.createParentDirectories(file);
        var matByte = new MatOfByte(bytes);
        Mat mat = Imgcodecs.imdecode(matByte, Imgcodecs.IMREAD_UNCHANGED);
        Imgcodecs.imwrite(pathAndName, mat);
        if (file.exists() && file.isFile()) {
            return file.length();
        } else {
            return -1;
        }
    }

    // 保存为JPG
    public static long saveImageToJPG(byte[] bytes, float quality, String pathAndName) throws IOException {
        // 格式转换
        BufferedImage oriImg = ImageIO.read(new ByteArrayInputStream(bytes));
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


    public static Image getResourceImage(String path) throws IOException {
        var bytes = MyFileUtils.getResourceFileBytes(path);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        return ImageIO.read(bais);
    }

}
