package com.jmd.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jmd.entity.config.HttpClientConfigEntity;

import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;

@Slf4j
@Component
public class HttpClient {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType XML = MediaType.parse("application/xml; charset=utf-8");

    @Value("${okhttp.connect-timeout}")
    private int connectTimeout;
    @Value("${okhttp.read-timeout}")
    private int readTimeout;
    @Value("${okhttp.write-timeout}")
    private int writeTimeout;
    @Value("${okhttp.max-idle-connections}")
    private int maxIdleConnections;
    @Value("${okhttp.keep-alive-duration}")
    private int keepAliveDuration;

    private OkHttpClient okHttpClient;

    @PostConstruct
    private void init() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        // 连接超时
        clientBuilder.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
        // 读取超时
        clientBuilder.readTimeout(readTimeout, TimeUnit.MILLISECONDS);
        // 写入超时
        clientBuilder.writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);
        // 连接池
        clientBuilder.connectionPool(new ConnectionPool(maxIdleConnections, keepAliveDuration, TimeUnit.MILLISECONDS));
        // 创建
        okHttpClient = clientBuilder.build();
    }

    public String rebuild(HttpClientConfigEntity config) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(config.getConnectTimeout(), TimeUnit.MILLISECONDS);
        clientBuilder.readTimeout(config.getReadTimeout(), TimeUnit.MILLISECONDS);
        clientBuilder.writeTimeout(config.getWriteTimeout(), TimeUnit.MILLISECONDS);
        clientBuilder.connectionPool(new ConnectionPool(config.getMaxIdleConnections(), config.getKeepAliveDuration(),
                TimeUnit.MILLISECONDS));
        if (ProxySetting.enable) {
            try {
                clientBuilder.proxy(
                        new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxySetting.hostname, ProxySetting.port)));
            } catch (Exception e) {
                return "请输入正确的代理参数";
            }
        }
        okHttpClient = clientBuilder.build();
        return "success";
    }

    /**
     * get 请求
     */
    public String doGet(String url) {
        return doGet(url, null, null);
    }

    /**
     * get 请求
     */
    public String doGet(String url, Map<String, String> params) {
        return doGet(url, params, null);
    }

    /**
     * get 请求
     */
    public String doGet(String url, String[] headers) {
        return doGet(url, null, headers);
    }

    /**
     * get 请求
     */
    public String doGet(String url, Map<String, String> params, String[] headers) {
        StringBuilder sb = new StringBuilder(url);
        if (params != null && params.keySet().size() > 0) {
            boolean firstFlag = true;
            for (String key : params.keySet()) {
                if (firstFlag) {
                    sb.append("?").append(key).append("=").append(params.get(key));
                    firstFlag = false;
                } else {
                    sb.append("&").append(key).append("=").append(params.get(key));
                }
            }
        }
        Request.Builder builder = new Request.Builder();
        if (headers != null && headers.length > 0) {
            if (headers.length % 2 == 0) {
                for (int i = 0; i < headers.length; i = i + 2) {
                    builder.addHeader(headers[i], headers[i + 1]);
                }
            } else {
                log.warn("headers's length[{}] is error.", headers.length);
            }

        }
        Request request = builder.url(sb.toString()).build();
        log.info("do get request and url[{}]", sb.toString());
        return execute(request);
    }

    /**
     * post 请求
     */
    public String doPost(String url, Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
        Request request = new Request.Builder().url(url).post(builder.build()).build();
        log.info("do post request and url[{}]", url);

        return execute(request);
    }

    /**
     * post 请求
     */
    public String doPostJson(String url, String json) {
        log.info("do post request and url[{}]", url);
        return exectePost(url, json, JSON);
    }

    /**
     * post 请求
     */
    public String doPostXml(String url, String xml) {
        log.info("do post request and url[{}]", url);
        return exectePost(url, xml, XML);
    }

    private String exectePost(String url, String data, MediaType contentType) {
        RequestBody requestBody = RequestBody.create(contentType, data);
        Request request = new Request.Builder().url(url).post(requestBody).build();

        return execute(request);
    }

    private String execute(Request request) {
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                return response.body().string();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return "";
    }

    /**
     * 获取文件流
     */
    public byte[] getFileBytes(String url, HashMap<String, String> headers) {
        Builder builder = new Request.Builder().url(url);
        for (Entry<String, String> entry : headers.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }
        Request request = builder.build();
        Response response = null;
        byte[] buf = null;
        try {
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                assert response.body() != null;
                buf = response.body().bytes();
            }
        } catch (IOException ignored) {
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return buf;
    }

}
