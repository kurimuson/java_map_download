package com.jmd.ui.common

import com.jmd.ApplicationPort
import com.jmd.browser.core.ChromiumEmbeddedCore
import com.jmd.browser.view.BrowserViewContainer
import com.jmd.common.StaticVar
import org.cef.CefClient
import org.cef.browser.CefBrowser
import java.awt.BorderLayout
import java.io.Serial
import javax.swing.*

abstract class BrowserViewPanel(
    private val path: String,
    private val waitingText: String
) : JPanel(), BrowserViewContainer {

    companion object {
        @Serial
        private val serialVersionUID = 296955159808720054L
    }

    private var loaded: Boolean = false
    private var url: String? = null
    private lateinit var client: CefClient
    private lateinit var browser: CefBrowser
    private val splitPane: JSplitPane
    private val framePanel: JPanel
    private val devToolPanel: JPanel
    private var devToolOpen = false

    fun isLoaded(): Boolean {
        return this.loaded;
    }

    init {

        this.layout = BorderLayout()
        this.isFocusable = true

        this.splitPane = JSplitPane()
        this.splitPane.border = null
        this.splitPane.dividerSize = 0
        this.splitPane.isOneTouchExpandable = false
        this.splitPane.isContinuousLayout = false
        this.splitPane.orientation = JSplitPane.HORIZONTAL_SPLIT
        this.add(splitPane, BorderLayout.CENTER)

        // 浏览器界面
        this.framePanel = JPanel()
        this.framePanel.layout = BorderLayout()
        this.splitPane.leftComponent = this.framePanel

        val label = JLabel(this.waitingText)
        label.horizontalAlignment = SwingConstants.CENTER
        label.font = StaticVar.FONT_SourceHanSansCNNormal_13
        this.framePanel.add(label, BorderLayout.CENTER)

        // 开发者工具
        this.devToolPanel = JPanel()
        this.devToolPanel.layout = BorderLayout()
        this.splitPane.rightComponent = null

    }

    fun load(prod: Boolean) {
        this.url = if (prod) {
            "http://localhost:${ApplicationPort.startPort}/web/index.html/#${this.path}"
        } else {
            "http://localhost:4500/#${this.path}"
        }
        if (!this.loaded) {
            this.showBrowser();
            this.loaded = true
        }
    }

    fun reload() {
        this.browser.reload()
    }

    private fun showBrowser() {
        this.client = ChromiumEmbeddedCore.instance.createClient(this)
        this.browser = ChromiumEmbeddedCore.instance.createBrowser(this.client, this.url)
        SwingUtilities.invokeLater {
            this.framePanel.removeAll()
            this.browser.uiComponent!!.let {
                this.framePanel.add(it, BorderLayout.CENTER);
            }
            this.framePanel.revalidate()
        }
    }

    override fun toggleDevTools() {
        this.devToolOpen = if (this.devToolOpen) {
            SwingUtilities.invokeLater { closeDevTools() }
            false
        } else {
            SwingUtilities.invokeLater { openDevTools() }
            true
        }
    }

    private fun openDevTools() {
        this.splitPane.dividerSize = 5
        this.splitPane.rightComponent = this.devToolPanel
        this.splitPane.isContinuousLayout = true
        this.splitPane.dividerLocation = this.size.width - 500
        this.devToolPanel.add(this.browser.devTools.uiComponent, BorderLayout.CENTER)
        this.devToolPanel.revalidate()
        this.splitPane.revalidate()
    }

    private fun closeDevTools() {
        this.devToolPanel.removeAll()
        this.devToolPanel.revalidate()
        this.splitPane.remove(this.devToolPanel)
        this.splitPane.dividerSize = 0
        this.splitPane.rightComponent = null
        this.splitPane.isContinuousLayout = false
        this.splitPane.dividerLocation = this.size.width
        this.splitPane.revalidate()
    }

    fun execJS(javaScript: String?) {
        this.browser.executeJavaScript(javaScript, null, 0)
    }

}