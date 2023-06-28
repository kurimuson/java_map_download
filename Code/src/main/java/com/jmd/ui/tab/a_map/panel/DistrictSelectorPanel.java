package com.jmd.ui.tab.a_map.panel;

import com.alibaba.fastjson2.JSON;
import com.jmd.common.StaticVar;
import com.jmd.common.WsSendTopic;
import com.jmd.db.service.AllDistrictService;
import com.jmd.model.geo.LngLatPoint;
import com.jmd.util.GeoUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serial;
import java.util.ArrayList;
import java.util.HashMap;

@Component
public class DistrictSelectorPanel extends JPanel {

    @Serial
    private static final long serialVersionUID = 5749571452900454848L;

    @Autowired
    private MapControlBrowserPanel browserPanel;
    @Autowired
    private AllDistrictService allDistrictService;

    private final JComboBox<String> provinceComboBox;
    private final JComboBox<String> cityComboBox;
    private final JComboBox<String> districtComboBox;
    private final JButton okButton;

    private final HashMap<String, String> provinceAdcodeMap = new HashMap<>();
    private final HashMap<String, String> cityAdcodeMap = new HashMap<>();
    private final HashMap<String, String> districtAdcodeMap = new HashMap<>();

    public DistrictSelectorPanel() {

        JPopupMenu.setDefaultLightWeightPopupEnabled(false);

        this.provinceComboBox = new JComboBox<>();
        this.provinceComboBox.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.provinceComboBox.setFocusable(false);

        this.cityComboBox = new JComboBox<>();
        this.cityComboBox.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.cityComboBox.setFocusable(false);

        this.districtComboBox = new JComboBox<>();
        this.districtComboBox.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.districtComboBox.setFocusable(false);


        this.okButton = new JButton("确定");
        this.okButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.okButton.setFocusable(false);

        var groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(this.provinceComboBox, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(this.cityComboBox, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(this.districtComboBox, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(this.okButton))
                                .addContainerGap())
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(this.provinceComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(this.cityComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(this.districtComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(this.okButton)
                                .addContainerGap())
        );
        this.setLayout(groupLayout);

    }

    @PostConstruct
    private void init() {
        this.getAllProvinces();
        this.provinceComboBox.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                this.getCitysByProvinceAdcode(this.provinceAdcodeMap.get(e.getItem()));
            }
        });
        this.cityComboBox.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                this.getDistrictsByCityAdcode(this.cityAdcodeMap.get(e.getItem()));
            }
        });
        this.okButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == 1) {
                    String adcode = null;
                    if (districtComboBox.getSelectedItem() != null) {
                        String name = (String) districtComboBox.getSelectedItem();
                        adcode = districtAdcodeMap.get(name);
                    }
                    if (adcode == null && cityComboBox.getSelectedItem() != null) {
                        String name = (String) cityComboBox.getSelectedItem();
                        adcode = cityAdcodeMap.get(name);
                    }
                    if (adcode == null && provinceComboBox.getSelectedItem() != null) {
                        String name = (String) provinceComboBox.getSelectedItem();
                        adcode = provinceAdcodeMap.get(name);
                    }
                    getPolygonByAdcode(adcode);
                }
            }
        });
    }

    private void getAllProvinces() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                provinceAdcodeMap.clear();
                provinceComboBox.removeAllItems();
                var provinces = allDistrictService.getAllProvinces();
                var provinceItems = new String[provinces.size()];
                for (var i = 0; i < provinces.size(); i++) {
                    provinceItems[i] = provinces.get(i).getProvince();
                    provinceAdcodeMap.put(provinces.get(i).getProvince(), provinces.get(i).getAdcode());
                }
                provinceComboBox.setModel(new DefaultComboBoxModel<String>(provinceItems));
                return null;
            }
        };
        worker.execute();
    }

    private void getCitysByProvinceAdcode(String adcode) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                cityAdcodeMap.clear();
                cityComboBox.removeAllItems();
                districtAdcodeMap.clear();
                districtComboBox.removeAllItems();
                var cities = allDistrictService.getCitysByProvinceAdcode(adcode);
                if (cities == null || cities.size() == 0) {
                    return null;
                }
                var cityItems = new String[cities.size() + 1];
                cityItems[0] = "全部";
                for (var i = 0; i < cities.size(); i++) {
                    cityItems[i + 1] = cities.get(i).getName();
                    cityAdcodeMap.put(cities.get(i).getName(), cities.get(i).getAdcode());
                }
                cityComboBox.setModel(new DefaultComboBoxModel<String>(cityItems));
                getDistrictsByCityAdcode(cities.get(0).getAdcode());
                return null;
            }
        };
        worker.execute();
    }

    private void getDistrictsByCityAdcode(String adcode) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                districtAdcodeMap.clear();
                districtComboBox.removeAllItems();
                var districts = allDistrictService.getDistrictsByCityAdcode(adcode);
                if (districts == null || districts.size() == 0) {
                    return null;
                }
                var districtItems = new String[districts.size() + 1];
                districtItems[0] = "全部";
                for (var i = 0; i < districts.size(); i++) {
                    districtItems[i + 1] = districts.get(i).getName();
                    districtAdcodeMap.put(districts.get(i).getName(), districts.get(i).getAdcode());
                }
                districtComboBox.setModel(new DefaultComboBoxModel<String>(districtItems));
                return null;
            }
        };
        worker.execute();
    }

    private void getPolygonByAdcode(String adcode) {
        if (adcode == null) {
            return;
        }
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                var area = allDistrictService.getAreaByAdcode(adcode);
                var blocks = new ArrayList<ArrayList<LngLatPoint>>();
                var block = area.getPolyline().split("\\|");
                for (var s : block) {
                    var polyline = s.split(";");
                    var points = new ArrayList<LngLatPoint>();
                    for (var value : polyline) {
                        var a = value.split(",");
                        var lngLatPoint = new LngLatPoint(Double.parseDouble(a[0]), Double.parseDouble(a[1]));
                        if (adcode.indexOf("710") == 0 || adcode.indexOf("810") == 0
                                || adcode.indexOf("820") == 0) {
                            lngLatPoint = GeoUtils.gcj02_To_wgs84(lngLatPoint);
                        }
                        points.add(lngLatPoint);
                    }
                    blocks.add(points);
                }
                var json = JSON.toJSONString(blocks);
                browserPanel.sendMessageByWebsocket(WsSendTopic.DRAW_POLYGON_AND_POSITING, json);
                return null;
            }
        };
        worker.execute();
    }

}
