package com.jmd

import com.jmd.common.Setting
import com.jmd.util.MyFileUtils
import java.io.File

object ApplicationSetting {

    private val path = File(System.getProperty("user.dir") + "/setting")
    private val file = File(System.getProperty("user.dir") + "/setting/ApplicationSetting")

    private var setting: Setting

    init {
        if (!path.exists() && !path.isFile) {
            path.mkdir()
        }
        if (!file.exists() && !file.isFile) {
            setting = createDefault()
        } else {
            setting = loadSettingFile()
        }
    }

    @JvmStatic
    fun getSetting(): Setting {
        return setting
    }

    @JvmStatic
    fun save() {
        try {
            MyFileUtils.saveObj2File(setting, file.absolutePath)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun save(s: Setting) {
        setting = s
        try {
            MyFileUtils.saveObj2File(setting, file.absolutePath)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createDefault(): Setting {
        val s = Setting("default")
        try {
            MyFileUtils.saveObj2File(s, file.absolutePath)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return s
    }

    private fun loadSettingFile(): Setting {
        var s: Setting
        try {
            s = MyFileUtils.readFile2Obj(file) as Setting
        } catch (e: Exception) {
            s = createDefault()
            e.printStackTrace()
        }
        return s
    }

}