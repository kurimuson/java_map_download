package com.jmd

import com.jmd.common.StaticVar
import com.jmd.rx.Topic
import com.jmd.rx.service.InnerMqService
import com.jmd.util.ImageUtils
import java.awt.MenuItem
import java.awt.PopupMenu
import java.awt.SystemTray
import java.awt.TrayIcon
import java.awt.event.ActionEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

object ApplicationTray {

    private val innerMqService = InnerMqService.getInstance()

    @JvmStatic
    fun addSystemTray() {
        try {
            val image = ImageUtils.getResourceImage("assets/icon/map.png")
            println(image)
            if (SystemTray.isSupported()) {
                val tray = SystemTray.getSystemTray()
                val popupMenu = PopupMenu()
                val openItem = MenuItem("show")
                openItem.font = StaticVar.FONT_SourceHanSansCNNormal_12
                openItem.addActionListener { e: ActionEvent? -> innerMqService.pub(Topic.MAIN_FRAME_SHOW, true) }
                val exitItem = MenuItem("exit")
                exitItem.font = StaticVar.FONT_SourceHanSansCNNormal_12
                exitItem.addActionListener { e: ActionEvent? -> Application.exit() }
                popupMenu.add(openItem)
                popupMenu.add(exitItem)
                val trayIcon = TrayIcon(image, "地图下载器", popupMenu)
                trayIcon.isImageAutoSize = true
                trayIcon.addMouseListener(object : MouseAdapter() {
                    override fun mouseClicked(e: MouseEvent) {
                        if (e.clickCount == 2) {
                            innerMqService.pub(Topic.MAIN_FRAME_SHOW, true)
                        }
                    }
                })
                tray.add(trayIcon)
            }
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

}