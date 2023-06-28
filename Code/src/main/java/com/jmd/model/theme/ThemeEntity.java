package com.jmd.model.theme;

import java.util.List;

import lombok.Data;

@Data
public class ThemeEntity {

    private Integer type;
    private String name;
    private String clazz;
    private List<ThemeEntity> sub;

    public ThemeEntity(String name, List<ThemeEntity> sub) {
        this.name = name;
        this.sub = sub;
    }

    public ThemeEntity(int type, String name, String clazz) {
        this.type = type;
        this.name = name;
        this.clazz = clazz;
    }

}
