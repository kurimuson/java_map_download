package com.jmd

import com.jmd.ui.MainFrame
import com.jmd.ui.StartupWindow
import com.jmd.z0test.TestFunc
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.io.IOException
import javax.swing.SwingUtilities

@Component
@Order(1)
class ApplicationInit : ApplicationRunner {

    @Autowired
    private lateinit var mainFrame: MainFrame

    @Autowired
    private lateinit var test: TestFunc

    @Throws(IOException::class)
    override fun run(args: ApplicationArguments) {
        SwingUtilities.invokeLater {
            mainFrame.isVisible = true
            StartupWindow.getInstance().close()
        }
        // new Thread(() -> test.run()).start();
    }

}