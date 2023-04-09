package com.jmd;

import com.jmd.util.ConnectorUtils;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

	public static int startPort = 0;

	@Override
	public void customize(ConfigurableServletWebServerFactory factory) {
		((TomcatServletWebServerFactory) factory).addConnectorCustomizers(connector -> {
			// 获取可用端口，指定端口范围，如果返回-1则范围内没有可用的，此时会使用80端口
			var port = ConnectorUtils.findAvailablePort(26737, 26787);
			try {
				if (port < 0) {
					throw new Exception("no available port !");
				} else {
					startPort = port;
					connector.setPort(port);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

	}

}
