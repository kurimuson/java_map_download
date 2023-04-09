package com.jmd.ui.common;

import com.jmd.ApplicationStore;

import javax.swing.*;
import java.util.Arrays;

public class CommonDialog {

    public static void alert(String title, String message) {
        var pane = new JOptionPane();
        pane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
        pane.setMessage(message);
        if (title == null) {
            title = UIManager.getString("OptionPane.messageDialogTitle", ApplicationStore.commonParentFrame.getLocale());
        }
        var dialog = pane.createDialog(ApplicationStore.commonParentFrame, title);
        var x = ApplicationStore.MAIN_FRAME_LOCATION_X - (int) dialog.getSize().getWidth() / 2 + ApplicationStore.MAIN_FRAME_WIDTH / 2;
        var y = ApplicationStore.MAIN_FRAME_LOCATION_Y - (int) dialog.getSize().getHeight() / 2 + ApplicationStore.MAIN_FRAME_HEIGHT / 2;
        dialog.setLocation(x, y);
        dialog.setVisible(true);
        dialog.dispose();
    }

    public static boolean confirm(String title, String message) {
        var pane = new JOptionPane();
        pane.setMessageType(JOptionPane.QUESTION_MESSAGE);
        pane.setMessage(message);
        pane.setOptionType(JOptionPane.YES_NO_OPTION);
        if (title == null) {
            title = UIManager.getString("OptionPane.messageDialogTitle", ApplicationStore.commonParentFrame.getLocale());
        }
        var dialog = pane.createDialog(ApplicationStore.commonParentFrame, title);
        pane.selectInitialValue();
        var x = ApplicationStore.MAIN_FRAME_LOCATION_X - (int) dialog.getSize().getWidth() / 2 + ApplicationStore.MAIN_FRAME_WIDTH / 2;
        var y = ApplicationStore.MAIN_FRAME_LOCATION_Y - (int) dialog.getSize().getHeight() / 2 + ApplicationStore.MAIN_FRAME_HEIGHT / 2;
        dialog.setLocation(x, y);
        dialog.setVisible(true);
        dialog.dispose();
        var selectedValue = pane.getValue();
        if (selectedValue == null) {
            return false;
        }
        if (selectedValue instanceof Integer) {
            return selectedValue.equals(JOptionPane.YES_OPTION);
        }
        return false;
    }

    public static Integer option(String title, String message, String[] options) {
        var pane = new JOptionPane();
        pane.setMessageType(JOptionPane.QUESTION_MESSAGE);
        pane.setMessage(message);
        pane.setOptions(Arrays.copyOf(options, options.length));
        if (title == null) {
            title = UIManager.getString("OptionPane.messageDialogTitle", ApplicationStore.commonParentFrame.getLocale());
        }
        var dialog = pane.createDialog(ApplicationStore.commonParentFrame, title);
        pane.selectInitialValue();
        var x = ApplicationStore.MAIN_FRAME_LOCATION_X - (int) dialog.getSize().getWidth() / 2 + ApplicationStore.MAIN_FRAME_WIDTH / 2;
        var y = ApplicationStore.MAIN_FRAME_LOCATION_Y - (int) dialog.getSize().getHeight() / 2 + ApplicationStore.MAIN_FRAME_HEIGHT / 2;
        dialog.setLocation(x, y);
        dialog.setVisible(true);
        dialog.dispose();
        var selectedValue = pane.getValue();
        if (selectedValue == null) {
            return null;
        }
        for (var i = 0; i < options.length; i++) {
            if (options[i].equals(selectedValue)) {
                return i;
            }
        }
        return null;
    }

}
