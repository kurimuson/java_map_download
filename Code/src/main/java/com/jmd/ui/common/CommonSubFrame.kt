package com.jmd.ui.common

import com.jmd.ApplicationStore
import com.jmd.rx.Topic
import com.jmd.rx.callback.OnMessageCallback
import com.jmd.rx.client.InnerMqClient
import com.jmd.rx.service.InnerMqService
import com.jmd.util.ImageUtils
import java.io.Serial
import javax.swing.JFrame
import javax.swing.SwingUtilities

abstract class CommonSubFrame : JFrame() {

    companion object {
        @Serial
        private val serialVersionUID = -3945359489263563093L
    }

    private val innerMqService = InnerMqService.getInstance()
    private var client: InnerMqClient? = null

    init {
        /* 任务栏图标 */
        this.iconImage = ImageUtils.getResourceImage("assets/icon/map.png")
        this.defaultCloseOperation = DISPOSE_ON_CLOSE
        try {
            subInnerMqMessage()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected open fun destroy() {
        this.innerMqService?.destroyClient(this.client)
    }

    @Throws(Exception::class)
    private fun subInnerMqMessage() {
        this.client = innerMqService.createClient()
        this.client?.sub(
            Topic.UPDATE_UI, OnMessageCallback { res: Any? ->
                SwingUtilities.invokeLater { SwingUtilities.updateComponentTreeUI(this) }
            }
        )
    }

    override fun setVisible(b: Boolean) {
        if (b) {
            val lx = ApplicationStore.MAIN_FRAME_LOCATION_X
            val ly = ApplicationStore.MAIN_FRAME_LOCATION_Y
            val mw = ApplicationStore.MAIN_FRAME_WIDTH
            val mh = ApplicationStore.MAIN_FRAME_HEIGHT
            val x = lx - this.size.getWidth().toInt() / 2 + mw / 2
            val y = ly - this.size.getHeight().toInt() / 2 + mh / 2
            this.setLocation(x, y)
        }
        super.setVisible(b)
    }

}