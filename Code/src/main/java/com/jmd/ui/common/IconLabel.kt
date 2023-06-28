package com.jmd.ui.common

import java.io.Serial
import javax.swing.Icon
import javax.swing.JLabel

class IconLabel(path: String) : JLabel() {

    companion object {
        @Serial
        private val serialVersionUID = -2643890482143441343L
    }

    private val icon: AutoScalingIcon

    init {
        this.icon = AutoScalingIcon(path, AutoScalingIcon.XPosition.LEFT, AutoScalingIcon.YPosition.TOP, 0, 0);
        this.setIcon(this.icon)
    }

    override fun getIcon(): Icon {
        return this.icon
    }

}