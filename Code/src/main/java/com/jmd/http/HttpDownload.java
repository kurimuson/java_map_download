package com.jmd.http;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jmd.entity.result.DownloadResult;
import com.jmd.inst.DownloadAmountInstance;
import com.jmd.util.CommonUtils;

@Component
public class HttpDownload {

    @Autowired
    private HttpClient http;
    @Autowired
    private DownloadAmountInstance downloadAmountInstance;

    // 通过URL下载文件
    public DownloadResult downloadTile(
            String url, HashMap<String, String> headers,
            int imgType, String pathAndName,
            int retry
    ) {
        pathAndName = CommonUtils.checkFilePathAndName(pathAndName);
        var result = new DownloadResult();
        var success = false;
        var bytes = http.getFileBytes(url, headers);
        if (null != bytes) {
            try {
                var fileLength = this.saveImage(imgType, bytes, pathAndName);
                if (fileLength >= 0) {
                    downloadAmountInstance.add(fileLength);
                    success = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (success) {
            result.setSuccess(true);
        } else if (Thread.currentThread().isInterrupted()) {
            result.setSuccess(false);
        } else {
            retry = retry - 1;
            if (retry >= 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return downloadTile(url, headers, imgType, pathAndName, retry);
            } else {
                result.setSuccess(false);
            }
        }
        return result;
    }

    // 转码并保存图片
    private long saveImage(int imgType, byte[] imgData, String pathAndName) throws IOException {
        switch (imgType) {
            case 1 -> {
                return CommonUtils.savePNG2WEBP(imgData, pathAndName);
            }
            case 2, 3, 4 -> {
                return switch (imgType) {
                    case 2 -> CommonUtils.savePNG2JPG(imgData, 0.2f, pathAndName);
                    case 4 -> CommonUtils.savePNG2JPG(imgData, 0.9f, pathAndName);
                    // 3
                    default -> CommonUtils.savePNG2JPG(imgData, 0.6f, pathAndName);
                };
            }
            default -> {
                // 0
                return CommonUtils.savePNG(imgData, pathAndName);
            }
        }
    }

}
