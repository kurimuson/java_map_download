package com.jmd.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.jmd.model.task.TaskAllInfoEntity;
import com.jmd.model.task.TaskBlockDivide;

public class TaskUtils {

    public static TaskAllInfoEntity getExistTaskByPath(String path) {
        TaskAllInfoEntity taskAllInfo = null;
        try {
            taskAllInfo = (TaskAllInfoEntity) MyFileUtils.readFile2Obj(path + "/task_info.jmd");
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
        return taskAllInfo;
    }

    public static TaskAllInfoEntity getExistTaskByFile(File file) {
        TaskAllInfoEntity taskAllInfo = null;
        try {
            taskAllInfo = (TaskAllInfoEntity) MyFileUtils.readFile2Obj(file);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
        return taskAllInfo;
    }

    public static String getSuffix(int imgType) {
        String suffix = "";
        switch (imgType) {
            case 0 -> suffix = "png";
            case 1 -> suffix = "webp";
            case 2, 3, 4 -> suffix = "jpg";
            default -> {
            }
        }
        return suffix;
    }

    public static String getFilePathName(String pathStyle, int imgType, int z, long x, long y) {
        pathStyle = pathStyle.replaceAll("\\{x\\}", String.valueOf(x));
        pathStyle = pathStyle.replaceAll("\\{y\\}", String.valueOf(y));
        pathStyle = pathStyle.replaceAll("\\{z\\}", String.valueOf(z));
        pathStyle = pathStyle.replaceAll("\\[image\\]", getSuffix(imgType));
        return pathStyle;
    }

    public static TaskBlockDivide blockDivide(long xStart, long xEnd, long yStart, long yEnd, double d) {
        TaskBlockDivide divide = new TaskBlockDivide();
        long countX = xEnd - xStart + 1;
        long countY = yEnd - yStart + 1;
        int eachX = (int) Math.ceil(countX / d);
        int eachY = (int) Math.ceil(countY / d);
        if (countX <= d) {
            eachX = (int) Math.ceil(countX / 2);
            eachX = eachX == 0 ? 1 : eachX;
        }
        if (countY <= d) {
            eachY = (int) Math.ceil(countY / 2);
            eachY = eachY == 0 ? 1 : eachY;
        }
        ArrayList<Long[]> divideX = new ArrayList<>();
        ArrayList<Long[]> divideY = new ArrayList<>();
        if (countX / eachX <= 1) {
            Long arr[] = {0L, countX};
            divideX.add(arr);
        } else {
            long cnt = (int) Math.floor(countX / eachX);
            long e = countX % eachX;
            for (var i = 0L; i < cnt; i++) {
                Long arr[] = {i * eachX, (i + 1) * eachX - 1};
                divideX.add(arr);
            }
            if (cnt * eachX < cnt * eachX + e) {
                Long arrEnd[] = {cnt * eachX, cnt * eachX + e - 1};
                divideX.add(arrEnd);
            }
        }
        if (countY / eachY <= 1) {
            Long arr[] = {0L, countY};
            divideY.add(arr);
        } else {
            long cnt = (int) Math.floor(countY / eachY);
            long e = countY % eachY;
            for (var i = 0L; i < cnt; i++) {
                Long arr[] = {i * eachY, (i + 1) * eachY - 1};
                divideY.add(arr);
            }
            if (cnt * eachY < cnt * eachY + e) {
                Long arrEnd[] = {cnt * eachY, cnt * eachY + e - 1};
                divideY.add(arrEnd);
            }
        }
        divide.setCountX(countX);
        divide.setCountY(countY);
        divide.setDivideX(divideX);
        divide.setDivideY(divideY);
        return divide;
    }

}
