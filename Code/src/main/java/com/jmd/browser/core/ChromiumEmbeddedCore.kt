package com.jmd.browser.core

import com.jetbrains.cef.JCefAppConfig
import com.jmd.ApplicationPort
import com.jmd.browser.handler.MenuHandler
import com.jmd.browser.view.BrowserViewContainer
import com.jmd.util.MyFileUtils
import org.cef.CefApp
import org.cef.CefClient
import org.cef.browser.CefBrowser
import org.cef.browser.CefRendering

class ChromiumEmbeddedCore {

    // 双重校验锁式（Double Check)
    companion object {
        val instance: ChromiumEmbeddedCore by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ChromiumEmbeddedCore()
        }
    }
// 等效方法
//    public class SingletonDemo {
//        private volatile static SingletonDemo instance;
//        private SingletonDemo() {}
//        public static SingletonDemo getInstance() {
//            if (instance == null) {
//                synchronized(SingletonDemo.class) {
//                    if(instance==null) {
//                        instance = new SingletonDemo();
//                    }
//                }
//            }
//            return instance;
//        }
//    }

    private val cefApp: CefApp

    val version: String get() = "Chromium Embedded Framework (CEF), ChromeVersion: ${cefApp.version.chromeVersion}"

    init {
        val args = JCefAppConfig.getInstance().appArgs
        val settings = JCefAppConfig.getInstance().cefSettings
        settings.cache_path = MyFileUtils.checkFilePath(
            "${System.getProperty("user.dir")}/context/jcef/data_${ApplicationPort.startPort}"
        )
        // 获取CefApp实例
        CefApp.startup(args)
        cefApp = CefApp.getInstance(args, settings)
    }

    fun createClient(container: BrowserViewContainer?): CefClient {
        val cefClient = cefApp.createClient()
        cefClient.addContextMenuHandler(MenuHandler(container!!))
        return cefClient
    }

    fun createBrowser(client: CefClient, url: String?): CefBrowser {
        return client.createBrowser(url, CefRendering.DEFAULT, true)
    }

    fun dispose() {
        cefApp.dispose()
    }

}