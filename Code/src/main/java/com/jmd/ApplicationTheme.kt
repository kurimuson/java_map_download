package com.jmd

import com.jmd.ApplicationSetting.save
import com.jmd.rx.Topic
import com.jmd.rx.service.InnerMqService
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component
import javax.swing.JOptionPane
import javax.swing.SwingUtilities
import javax.swing.UIManager

@Component
class ApplicationTheme {

    private val currentThemeType: Int = ApplicationSetting.getSetting().getThemeType()
    private val innerMqService = InnerMqService.getInstance()

    @PostConstruct
    private fun init() {
        try {
            subInnerMqMessage()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(Exception::class)
    private fun subInnerMqMessage() {
        val client = innerMqService.createClient()
        client.sub(Topic.CHANGE_THEME) { res: HashMap<String?, Any?> ->
            change(
                res["name"] as String?, res["type"] as Int?, res["clazz"] as String?
            )
        }
    }

    fun change(name: String?, type: Int?, clazz: String?) {
        // 保存配置
        ApplicationSetting.getSetting().themeName = name
        ApplicationSetting.getSetting().themeName = name
        ApplicationSetting.getSetting().themeType = type
        ApplicationSetting.getSetting().themeClazz = clazz
        save()
        // 更新窗口
        if (currentThemeType == type) {
            SwingUtilities.invokeLater {
                try {
                    UIManager.setLookAndFeel(clazz)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
                innerMqService.pub(Topic.UPDATE_THEME_TEXT, name)
                innerMqService.pub(Topic.UPDATE_UI, true)
            }
        } else {
            JOptionPane.showMessageDialog(null, "切换不同组的主题，程序重启后生效")
        }
    }
}