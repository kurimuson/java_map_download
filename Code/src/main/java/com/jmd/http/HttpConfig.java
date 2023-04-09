package com.jmd.http;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jmd.entity.config.HttpClientConfigEntity;

@Component
public class HttpConfig {

	@SuppressWarnings("unused")
	private static final String USER_AGENT_MS_EDGE = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.150 Safari/537.36 Edg/88.0.705.68";
	@SuppressWarnings("unused")
	private static final String USER_AGENT_CHROME = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.150 Safari/537.36";
	@SuppressWarnings("unused")
	private static final String USER_AGENT_QQ_BROWSER = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.25 Safari/537.36 Core/1.70.3861.400 QQBrowser/10.7.4313.400";

	@Value("${okhttp.connect-timeout}")
	private int default_connectTimeout;
	@Value("${okhttp.read-timeout}")
	private int default_readTimeout;
	@Value("${okhttp.write-timeout}")
	private int default_writeTimeout;
	@Value("${okhttp.max-idle-connections}")
	private int default_maxIdleConnections;
	@Value("${okhttp.keep-alive-duration}")
	private int default_keepAliveDuration;

	/** 获取默认设置 */
	public HttpClientConfigEntity getDefaultConfig() {
		HttpClientConfigEntity config = new HttpClientConfigEntity();
		config.setConnectTimeout(default_connectTimeout);
		config.setReadTimeout(default_readTimeout);
		config.setWriteTimeout(default_writeTimeout);
		config.setMaxIdleConnections(default_maxIdleConnections);
		config.setKeepAliveDuration(default_keepAliveDuration);
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("User-Agent", USER_AGENT_MS_EDGE);
		config.setHeaders(headers);
		return config;
	}

	@Value("${okhttp.osm.connect-timeout}")
	private int osm_connectTimeout;
	@Value("${okhttp.osm.read-timeout}")
	private int osm_readTimeout;
	@Value("${okhttp.osm.write-timeout}")
	private int osm_writeTimeout;
	@Value("${okhttp.osm.max-idle-connections}")
	private int osm_maxIdleConnections;
	@Value("${okhttp.osm.keep-alive-duration}")
	private int osm_keepAliveDuration;

	/** 获取OSM下载设置 */
	public HttpClientConfigEntity getOsmConfig() {
		HttpClientConfigEntity config = new HttpClientConfigEntity();
		config.setConnectTimeout(osm_connectTimeout);
		config.setReadTimeout(osm_readTimeout);
		config.setWriteTimeout(osm_writeTimeout);
		config.setMaxIdleConnections(osm_maxIdleConnections);
		config.setKeepAliveDuration(osm_keepAliveDuration);
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("User-Agent", USER_AGENT_MS_EDGE);
		config.setHeaders(headers);
		return config;
	}

	@Value("${okhttp.tian.connect-timeout}")
	private int tian_connectTimeout;
	@Value("${okhttp.tian.read-timeout}")
	private int tian_readTimeout;
	@Value("${okhttp.tian.write-timeout}")
	private int tian_writeTimeout;
	@Value("${okhttp.tian.max-idle-connections}")
	private int tian_maxIdleConnections;
	@Value("${okhttp.tian.keep-alive-duration}")
	private int tian_keepAliveDuration;

	/** 获取天地图下载设置 */
	public HttpClientConfigEntity getTianConfig() {
		HttpClientConfigEntity config = new HttpClientConfigEntity();
		config.setConnectTimeout(tian_connectTimeout);
		config.setReadTimeout(tian_readTimeout);
		config.setWriteTimeout(tian_writeTimeout);
		config.setMaxIdleConnections(tian_maxIdleConnections);
		config.setKeepAliveDuration(tian_keepAliveDuration);
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("User-Agent", USER_AGENT_MS_EDGE);
		headers.put("Accept", "mage/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8");
		headers.put("Accept-Encoding", "gzip, deflate, br");
		headers.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
		headers.put("Sec-Fetch-Dest", "image");
		headers.put("Sec-Fetch-Mode", "no-cors");
		headers.put("Sec-Fetch-Site", "cross-site");
		config.setHeaders(headers);
		return config;
	}

	@Value("${okhttp.google.connect-timeout}")
	private int google_connectTimeout;
	@Value("${okhttp.google.read-timeout}")
	private int google_readTimeout;
	@Value("${okhttp.google.write-timeout}")
	private int google_writeTimeout;
	@Value("${okhttp.google.max-idle-connections}")
	private int google_maxIdleConnections;
	@Value("${okhttp.google.keep-alive-duration}")
	private int google_keepAliveDuration;

	/** 获取谷歌地图下载设置 */
	public HttpClientConfigEntity getGoogleConfig() {
		HttpClientConfigEntity config = new HttpClientConfigEntity();
		config.setConnectTimeout(google_connectTimeout);
		config.setReadTimeout(google_readTimeout);
		config.setWriteTimeout(google_writeTimeout);
		config.setMaxIdleConnections(google_maxIdleConnections);
		config.setKeepAliveDuration(google_keepAliveDuration);
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("User-Agent", USER_AGENT_MS_EDGE);
		config.setHeaders(headers);
		return config;
	}

	@Value("${okhttp.amap.connect-timeout}")
	private int amap_connectTimeout;
	@Value("${okhttp.amap.read-timeout}")
	private int amap_readTimeout;
	@Value("${okhttp.amap.write-timeout}")
	private int amap_writeTimeout;
	@Value("${okhttp.amap.max-idle-connections}")
	private int amap_maxIdleConnections;
	@Value("${okhttp.amap.keep-alive-duration}")
	private int amap_keepAliveDuration;

	/** 获取高德地图下载设置 */
	public HttpClientConfigEntity getAmapConfig() {
		HttpClientConfigEntity config = new HttpClientConfigEntity();
		config.setConnectTimeout(amap_connectTimeout);
		config.setReadTimeout(amap_readTimeout);
		config.setWriteTimeout(amap_writeTimeout);
		config.setMaxIdleConnections(amap_maxIdleConnections);
		config.setKeepAliveDuration(amap_keepAliveDuration);
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("User-Agent", USER_AGENT_MS_EDGE);
		config.setHeaders(headers);
		return config;
	}

	@Value("${okhttp.tencent.connect-timeout}")
	private int tencent_connectTimeout;
	@Value("${okhttp.tencent.read-timeout}")
	private int tencent_readTimeout;
	@Value("${okhttp.tencent.write-timeout}")
	private int tencent_writeTimeout;
	@Value("${okhttp.tencent.max-idle-connections}")
	private int tencent_maxIdleConnections;
	@Value("${okhttp.tencent.keep-alive-duration}")
	private int tencent_keepAliveDuration;

	/** 获取腾讯地图下载设置 */
	public HttpClientConfigEntity getTencentConfig() {
		HttpClientConfigEntity config = new HttpClientConfigEntity();
		config.setConnectTimeout(tencent_connectTimeout);
		config.setReadTimeout(tencent_readTimeout);
		config.setWriteTimeout(tencent_writeTimeout);
		config.setMaxIdleConnections(tencent_maxIdleConnections);
		config.setKeepAliveDuration(tencent_keepAliveDuration);
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("User-Agent", USER_AGENT_MS_EDGE);
		config.setHeaders(headers);
		return config;
	}

	@Value("${okhttp.bing.connect-timeout}")
	private int bing_connectTimeout;
	@Value("${okhttp.bing.read-timeout}")
	private int bing_readTimeout;
	@Value("${okhttp.bing.write-timeout}")
	private int bing_writeTimeout;
	@Value("${okhttp.bing.max-idle-connections}")
	private int bing_maxIdleConnections;
	@Value("${okhttp.bing.keep-alive-duration}")
	private int bing_keepAliveDuration;

	/** 获取必应地图下载设置 */
	public HttpClientConfigEntity getBingConfig() {
		HttpClientConfigEntity config = new HttpClientConfigEntity();
		config.setConnectTimeout(bing_connectTimeout);
		config.setReadTimeout(bing_readTimeout);
		config.setWriteTimeout(bing_writeTimeout);
		config.setMaxIdleConnections(bing_maxIdleConnections);
		config.setKeepAliveDuration(bing_keepAliveDuration);
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("User-Agent", USER_AGENT_MS_EDGE);
		config.setHeaders(headers);
		return config;
	}

}
