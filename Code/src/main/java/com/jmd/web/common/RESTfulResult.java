package com.jmd.web.common;

import lombok.Data;

@Data
public class RESTfulResult<T> {

    private Integer code;
    private String message;
    private Boolean success;
    private T data;

}
