package com.jmd

import javax.swing.JFrame
import javax.swing.JTextArea

object ApplicationStore {

    @JvmField
    var commonParentFrame: JFrame? = null

    @JvmField
    var consoleTextArea = JTextArea()

    @JvmField
    var MAIN_FRAME_HEIGHT = 0

    @JvmField
    var MAIN_FRAME_WIDTH = 0

    @JvmField
    var MAIN_FRAME_LOCATION_X = 0

    @JvmField
    var MAIN_FRAME_LOCATION_Y = 0

}