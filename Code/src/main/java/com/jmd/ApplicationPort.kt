package com.jmd

import com.jmd.util.ConnectorUtils
import org.apache.catalina.connector.Connector
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory
import org.springframework.context.annotation.Configuration

@Configuration
class ApplicationPort : WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

    companion object {
        @JvmField
        var startPort = 0
    }

    override fun customize(factory: ConfigurableServletWebServerFactory) {
        (factory as TomcatServletWebServerFactory).addConnectorCustomizers(TomcatConnectorCustomizer { connector: Connector ->
            // 获取可用端口，指定端口范围，如果返回-1则范围内没有可用的，此时会使用80端口
            val port = ConnectorUtils.findAvailablePort(26737, 26787)
            try {
                if (port < 0) {
                    throw Exception("no available port !")
                } else {
                    startPort = port
                    connector.port = port
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
    }

}