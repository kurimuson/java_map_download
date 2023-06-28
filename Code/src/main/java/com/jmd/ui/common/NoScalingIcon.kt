package com.jmd.ui.common

import com.jmd.util.MyFileUtils
import java.awt.Component
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Image
import java.awt.geom.AffineTransform
import javax.swing.Icon
import javax.swing.ImageIcon

class NoScalingIcon(width: Int, height: Int, path: String) : Icon {

    private val icon = ImageIcon()

    init {
        val originIcon = ImageIcon(MyFileUtils.getResourceFileBytes(path))
        this.icon.image = originIcon.image.getScaledInstance(width, height, Image.SCALE_SMOOTH)
    }

    override fun getIconWidth(): Int {
        return this.icon.iconWidth
    }

    override fun getIconHeight(): Int {
        return this.icon.iconHeight
    }

    override fun paintIcon(c: Component, g: Graphics, x: Int, y: Int) {

        val g2d = g.create() as Graphics2D
        val at = g2d.transform

        val scaleX = (x * at.scaleX).toInt()
        val scaleY = (y * at.scaleY).toInt()
        val offsetX = (this.icon.iconWidth * (at.scaleX - 1) / 2).toInt()
        val offsetY = (this.icon.iconHeight * (at.scaleY - 1) / 2).toInt()
        val locationX = scaleX + offsetX
        val locationY = scaleY + offsetY

        //  Reset scaling to 1.0 by concatenating an inverse scale transform
        val scaled = AffineTransform.getScaleInstance(1.0 / at.scaleX, 1.0 / at.scaleY)
        at.concatenate(scaled)
        g2d.transform = at

        this.icon.paintIcon(c, g2d, locationX, locationY)
        g2d.dispose()

    }

}