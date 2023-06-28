package com.jmd.task;

import org.apache.commons.io.FileUtils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Range;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import com.jmd.common.StaticVar;

import lombok.Getter;

import java.io.File;
import java.io.IOException;

public class TileMergeMatWrap {

    private Mat des = null;

    private int width;
    private int height;

    @Getter
    private long allPixel = 0L;
    @Getter
    private long runPixel = 0L;

    public void init(int width, int height) {
        this.width = width;
        this.height = height;
        /*
         * CV_8uc1 单颜色通道 8位</br>
         * CV_8uc2 2颜色通道 16位</br>
         * CV_8uc3 3颜色通道 24位</br>
         * CV_8uc4 4颜色通道 32位</br>
         */
        // CV_8UC4为支持透明PNG的RGBA格式
        this.des = Mat.zeros(height, width, CvType.CV_8UC4);
        // 计算总像素数量
        this.allPixel = (long) width * height;
    }

    public void mergeToMat(String pathAndName, long x, long y, boolean flag) {
        // 是否读取图像进行合并
        int tileWidth = StaticVar.TILE_WIDTH;
        int tileHeight = StaticVar.TILE_HEIGHT;
        if (flag) {
            // 读取图片
            var tileMat = Imgcodecs.imread(pathAndName, Imgcodecs.IMREAD_UNCHANGED);
            try {
                // 转换图片至RGBA格式
                Imgproc.cvtColor(tileMat, tileMat, Imgproc.COLOR_BGR2BGRA);
                // 确定坐标位置
                var rectForDes = this.des
                        .colRange(new Range((int) x, (int) x + tileWidth))
                        .rowRange(new Range((int) y, (int) y + tileHeight));
                // 填充至合并大图
                tileMat.copyTo(rectForDes);
            } catch (Exception ignored) {

            }
        }
        // 完成后计算已合并的像素数量
        this.runPixel += (long) tileWidth * tileHeight;
    }

    public void output(String path, String name, int type) throws IOException {
        String suffix = "png";
        if (type != 3) {
            if (this.width < 16383 && this.height < 16383) {
                if (type == 0 || type == 1) {
                    // 自动，WEBP
                    suffix = "webp";
                } else if (type == 2) {
                    // 自动，JPG
                    suffix = "jpg";
                }
            } else if (this.width < 65535 && this.height < 65535) {
                // 自动，WEBP，JPG
                if (type == 0 || type == 1 || type == 2) {
                    suffix = "jpg";
                }
            }
        }
        String out = path + name + "." + suffix;
        FileUtils.createParentDirectories(new File(out));
        Imgcodecs.imwrite(out, this.des);
    }

    public void destroy() {
        this.des.release();
        this.des = null;
    }

}
