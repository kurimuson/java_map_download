package com.jmd.entity.config;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;

import lombok.Data;

@Data
public class HttpClientConfigEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = -4889925151834448697L;

	private int connectTimeout;
	private int readTimeout;
	private int writeTimeout;
	private int maxIdleConnections;
	private int keepAliveDuration;
	private HashMap<String, String> headers;

}
