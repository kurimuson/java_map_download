package com.jmd.web.common;

import lombok.Data;

@Data
public class WsSendData {

    private String title;
    private String content;

    public WsSendData(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
