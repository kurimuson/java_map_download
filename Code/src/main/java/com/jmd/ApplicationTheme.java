package com.jmd;

import javax.swing.*;

import com.jmd.rx.Topic;
import com.jmd.rx.service.InnerMqService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class ApplicationTheme {

    private final Integer currentThemeType = ApplicationSetting.getSetting().getThemeType();
    private final InnerMqService innerMqService = InnerMqService.getInstance();

    @PostConstruct
    private void init() {
        try {
            this.subInnerMqMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void subInnerMqMessage() throws Exception {
        var client = this.innerMqService.createClient();
        client.<HashMap<String, Object>>sub(Topic.CHANGE_THEME, (res) -> {
            change((String) res.get("name"), (Integer) res.get("type"), (String) res.get("clazz"));
        });
    }

    public void change(String name, Integer type, String clazz) {
        // 保存配置
        var setting = ApplicationSetting.getSetting();
        setting.setThemeName(name);
        setting.setThemeType(type);
        setting.setThemeClazz(clazz);
        ApplicationSetting.save(setting);
        // 更新窗口
        if (currentThemeType.equals(type)) {
            SwingUtilities.invokeLater(() -> {
                try {
                    UIManager.setLookAndFeel(clazz);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                this.innerMqService.pub(Topic.UPDATE_THEME_TEXT, name);
                this.innerMqService.pub(Topic.UPDATE_UI, true);
            });
        } else {
            JOptionPane.showMessageDialog(null, "切换不同组的主题，程序重启后生效");
        }
    }

}
