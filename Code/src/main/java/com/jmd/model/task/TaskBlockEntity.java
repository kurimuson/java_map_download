package com.jmd.model.task;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

@Data
public class TaskBlockEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -2871903479117630871L;

    private String name;
    private Integer z;

    private Long xStart;
    private Long xEnd;
    private Long yStart;
    private Long yEnd;

    private Long realCount;

    private Long xRun;
    private Long yRun;
    private Long runCount;

    public TaskBlockEntity() {

    }

    public TaskBlockEntity(
            String name,
            Integer z,
            Long xStart, Long xEnd,
            Long yStart, Long yEnd,
            Long realCount,
            Long xRun,
            Long yRun,
            Long runCount
    ) {
        this.name = name;
        this.z = z;
        this.xStart = xStart;
        this.xEnd = xEnd;
        this.yStart = yStart;
        this.yEnd = yEnd;
        this.realCount = realCount;
        this.xRun = xRun;
        this.yRun = yRun;
        this.runCount = runCount;
    }

}
