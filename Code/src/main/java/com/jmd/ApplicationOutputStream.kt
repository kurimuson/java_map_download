package com.jmd

import java.io.IOException
import java.io.OutputStream
import javax.swing.JTextArea
import javax.swing.SwingUtilities

class ApplicationOutputStream(destination: JTextArea?) : OutputStream() {

    private val destination: JTextArea

    init {
        requireNotNull(destination) { "Destination is null" }
        this.destination = destination
    }

    @Throws(IOException::class)
    override fun write(buffer: ByteArray, offset: Int, length: Int) {
        val text = String(buffer, offset, length)
        SwingUtilities.invokeLater {
            destination.append(text)
            destination.caretPosition = destination.text.length
        }
    }

    @Throws(IOException::class)
    override fun write(b: Int) {
        write(byteArrayOf(b.toByte()), 0, 1)
    }

}