package com.jmd;

import javax.swing.SwingUtilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.jmd.ui.MainFrame;
import com.jmd.ui.StartupWindow;
import com.jmd.z0test.TestFunc;

import java.io.IOException;

@Component
@Order(1)
public class MainInit implements ApplicationRunner {

    @Autowired
    private MainFrame mainFrame;
    @Autowired
    private TestFunc test;

    @Override
    public void run(ApplicationArguments args) throws IOException {
        SwingUtilities.invokeLater(() -> {
            mainFrame.setVisible(true);
            StartupWindow.getInstance().close();
        });
        // new Thread(() -> test.run()).start();
    }

}
