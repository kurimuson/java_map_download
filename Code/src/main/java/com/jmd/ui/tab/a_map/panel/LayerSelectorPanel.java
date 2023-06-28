package com.jmd.ui.tab.a_map.panel;

import com.alibaba.fastjson2.JSON;
import com.jmd.ApplicationSetting;
import com.jmd.common.StaticVar;
import com.jmd.common.WsSendTopic;
import com.jmd.model.setting.AddedLayerSetting;
import com.jmd.rx.Topic;
import com.jmd.rx.client.InnerMqClient;
import com.jmd.rx.service.InnerMqService;
import com.jmd.ui.common.CommonDialog;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.Serial;

@Component
public class LayerSelectorPanel extends JPanel {

    @Serial
    private static final long serialVersionUID = 3161455562222434180L;

    private final InnerMqService innerMqService = InnerMqService.getInstance();
    private InnerMqClient client;

    @Autowired
    private MapControlBrowserPanel mapViewBrowserPanel;

    private static final String OSM_NAME = "OSM(wgs84)(可能需要代理)";
    private static final String TIAN_NAME = "天地图(wgs84)";
    private static final String GOOGLE_NAME = "谷歌地图(gcj02)(可能需要代理)";
    private static final String AMAP_NAME = "高德地图(gcj02)";
    private static final String TENCENT_NAME = "腾讯地图(gcj02)";
    private static final String BING_NAME = "必应地图(gcj02)";
    private static final String BING_WGS84_NAME = "必应地图(wgs84)";
    private static final String BAIDU_NAME = "百度地图(仅预览)";
    private static final String ADDED_NAME = "自定义图层";

    private final DefaultMutableTreeNode addedLayersNode = new DefaultMutableTreeNode(ADDED_NAME, true);
    private final JTree layerTypeTree = new JTree();
    private final DefaultTreeModel treeModel = this.getTree();

    public LayerSelectorPanel() {

        this.setLayout(new BorderLayout(0, 0));

        this.layerTypeTree.setModel(this.treeModel);
        this.layerTypeTree.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);
        this.layerTypeTree.setFocusable(false);
        this.layerTypeTree.addTreeSelectionListener((e) -> {
            var node = (DefaultMutableTreeNode) this.layerTypeTree.getLastSelectedPathComponent();
            if (node != null && node.isLeaf()) {
                switchLayer(node.getParent().toString(), node.toString());
            }
        });

        var layerTypeScrollPane = new JScrollPane();
        layerTypeScrollPane.setViewportView(this.layerTypeTree);

        this.add(layerTypeScrollPane, BorderLayout.CENTER);

    }

    @PostConstruct
    private void init() {
        try {
            this.subInnerMqMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @PreDestroy
    protected void destroy() {
        this.innerMqService.destroyClient(this.client);
    }

    private void subInnerMqMessage() throws Exception {
        this.client = this.innerMqService.createClient();
        this.client.sub(Topic.ADD_NEW_LAYER, this::addNewLayer);
        this.client.sub(Topic.REMOVE_ADDED_LAYER, (res) -> {
            var node = (DefaultMutableTreeNode) this.layerTypeTree.getLastSelectedPathComponent();
            if (node != null && node.isLeaf()) {
                var parent = node.getParent().toString();
                var self = node.toString();
                if (ADDED_NAME.equals(parent)) {
                    this.removeAddedLayer(self);
                    return;
                }
            }
            CommonDialog.alert(null, "未选择自定义图层");
        });
    }

    private DefaultTreeModel getTree() {

        var tree = new DefaultMutableTreeNode("全部图层", true);

        var node_1 = new DefaultMutableTreeNode(OSM_NAME, true);
        node_1.add(new DefaultMutableTreeNode("OpenStreetMap", false));
        tree.add(node_1);

        var node_2 = new DefaultMutableTreeNode(TIAN_NAME, true);
        node_2.add(new DefaultMutableTreeNode("普通图-无标注", false));
        node_2.add(new DefaultMutableTreeNode("地形图-无标注", false));
        node_2.add(new DefaultMutableTreeNode("边界线-无标注", false));
        node_2.add(new DefaultMutableTreeNode("标注层", false));
        tree.add(node_2);

        var node_3 = new DefaultMutableTreeNode(GOOGLE_NAME, true);
        node_3.add(new DefaultMutableTreeNode("普通图-带标注", false));
        node_3.add(new DefaultMutableTreeNode("地形图-带标注", false));
        node_3.add(new DefaultMutableTreeNode("影像图-带标注", false));
        node_3.add(new DefaultMutableTreeNode("影像图-无标注", false));
        node_3.add(new DefaultMutableTreeNode("路网图-带标注", false));
        tree.add(node_3);

        var node_4 = new DefaultMutableTreeNode(AMAP_NAME, true);
        node_4.add(new DefaultMutableTreeNode("普通图-带标注", false));
        node_4.add(new DefaultMutableTreeNode("普通图-无标注", false));
        node_4.add(new DefaultMutableTreeNode("影像图-无标注", false));
        node_4.add(new DefaultMutableTreeNode("路网图-带标注", false));
        node_4.add(new DefaultMutableTreeNode("路网图-无标注", false));
        tree.add(node_4);

        var node_5 = new DefaultMutableTreeNode(TENCENT_NAME, true);
        node_5.add(new DefaultMutableTreeNode("普通图-带标注", false));
        tree.add(node_5);

        var node_6 = new DefaultMutableTreeNode(BING_NAME, true);
        node_6.add(new DefaultMutableTreeNode("普通图1-带标注-全球", false));
        node_6.add(new DefaultMutableTreeNode("普通图1-带标注-国内", false));
        node_6.add(new DefaultMutableTreeNode("普通图1-无标注", false));
        node_6.add(new DefaultMutableTreeNode("普通图2-带标注-全球", false));
        node_6.add(new DefaultMutableTreeNode("普通图2-带标注-国内", false));
        node_6.add(new DefaultMutableTreeNode("普通图2-无标注", false));
        tree.add(node_6);

        var node_7 = new DefaultMutableTreeNode(BING_WGS84_NAME, true);
        node_7.add(new DefaultMutableTreeNode("影像图-无标注", false));
        tree.add(node_7);

        var node_8 = new DefaultMutableTreeNode(BAIDU_NAME, true);
        node_8.add(new DefaultMutableTreeNode("百度瓦片图旧版", false));
        tree.add(node_8);

        var addedLayers = ApplicationSetting.getSetting().getAddedLayers();
        for (var layer : addedLayers) {
            this.addedLayersNode.add(new DefaultMutableTreeNode(layer.getName(), false));
        }
        tree.add(this.addedLayersNode);

        return new DefaultTreeModel(tree);

    }

    private void switchLayer(String parent, String self) {
        switch (parent) {
            case OSM_NAME:
                switch (self) {
                    case "OpenStreetMap" ->
                            mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.SWITCH_RESOURCE, "OpenStreet");
                }
                break;
            case TIAN_NAME:
                switch (self) {
                    case "普通图-无标注" ->
                            mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.SWITCH_RESOURCE, "Tianditu-Normal-None");
                    case "地形图-无标注" ->
                            mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.SWITCH_RESOURCE, "Tianditu-Terrain-None");
                    case "边界线-无标注" ->
                            mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.SWITCH_RESOURCE, "Tianditu-Line");
                    case "标注层" ->
                            mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.SWITCH_RESOURCE, "Tianditu-Tip");
                }
                break;
            case GOOGLE_NAME:
                switch (self) {
                    case "普通图-带标注" ->
                            mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.SWITCH_RESOURCE, "Google-Normal");
                    case "地形图-带标注" ->
                            mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.SWITCH_RESOURCE, "Google-Terrain");
                    case "影像图-带标注" ->
                            mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.SWITCH_RESOURCE, "Google-Satellite");
                    case "影像图-无标注" ->
                            mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.SWITCH_RESOURCE, "Google-Satellite-None");
                    case "路网图-带标注" ->
                            mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.SWITCH_RESOURCE, "Google-Street");
                }
                break;
            case AMAP_NAME:
                switch (self) {
                    case "普通图-带标注" ->
                            mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.SWITCH_RESOURCE, "AMap-Normal");
                    case "普通图-无标注" ->
                            mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.SWITCH_RESOURCE, "AMap-Normal-None");
                    case "影像图-无标注" ->
                            mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.SWITCH_RESOURCE, "AMap-Satellite-None");
                    case "路网图-带标注" ->
                            mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.SWITCH_RESOURCE, "AMap-Street");
                    case "路网图-无标注" ->
                            mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.SWITCH_RESOURCE, "AMap-Street-None");
                }
                break;
            case TENCENT_NAME:
                switch (self) {
                    case "普通图-带标注" ->
                            mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.SWITCH_RESOURCE, "Tencent-Normal");
                }
                break;
            case BING_NAME:
                switch (self) {
                    case "普通图1-带标注-全球" ->
                            mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.SWITCH_RESOURCE, "Bing-Normal-1");
                    case "普通图1-带标注-国内" ->
                            mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.SWITCH_RESOURCE, "Bing-Normal-1-CN");
                    case "普通图1-无标注" ->
                            mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.SWITCH_RESOURCE, "Bing-Normal-1-None");
                    case "普通图2-带标注-全球" ->
                            mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.SWITCH_RESOURCE, "Bing-Normal-2");
                    case "普通图2-带标注-国内" ->
                            mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.SWITCH_RESOURCE, "Bing-Normal-2-CN");
                    case "普通图2-无标注" ->
                            mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.SWITCH_RESOURCE, "Bing-Normal-2-None");
                }
                break;
            case BING_WGS84_NAME:
                switch (self) {
                    case "影像图-无标注" ->
                            mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.SWITCH_RESOURCE, "Bing-Satellite-None");
                }
                break;
            case BAIDU_NAME:
                switch (self) {
                    case "百度瓦片图旧版" ->
                            mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.SWITCH_RESOURCE, "Baidu-Normal");
                }
                break;
            case ADDED_NAME:
                var addedLayers = ApplicationSetting.getSetting().getAddedLayers();
                for (var layer : addedLayers) {
                    if (self.equals(layer.getName())) {
                        var data = JSON.toJSONString(layer);
                        mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.SWITCH_ADDED_RESOURCE, data);
                        break;
                    }
                }
                break;
        }
    }

    private void addNewLayer(AddedLayerSetting layer) {
        SwingUtilities.invokeLater(() -> {
            this.addedLayersNode.add(new DefaultMutableTreeNode(layer.getName(), false));
            this.treeModel.reload();
            this.layerTypeTree.expandRow(this.layerTypeTree.getRowCount() - 1);
        });
    }

    private void removeAddedLayer(String name) {
        var f = CommonDialog.confirm("确认", "是否删除自定义图层：" + name);
        if (!f) {
            return;
        }
        for (var i = 0; i < this.addedLayersNode.getChildCount(); i++) {
            var node = this.addedLayersNode.getChildAt(i);
            if (node.toString().equals(name)) {
                // 删除数组中的值
                var index = this.getLayerIndexByName(name);
                if (index != null) {
                    ApplicationSetting.getSetting().getAddedLayers().remove((int) index);
                }
                // 保存配置文件
                ApplicationSetting.save();
                // 推送至前端删除图层
                this.mapViewBrowserPanel.sendMessageByWebsocket(WsSendTopic.REMOVE_ADDED_RESOURCE, name);
                // 删除树节点
                int finalI = i;
                SwingUtilities.invokeLater(() -> {
                    this.addedLayersNode.remove(finalI);
                    this.treeModel.reload();
                });
                break;
            }
        }
    }

    private Integer getLayerIndexByName(String name) {
        var addedLayers = ApplicationSetting.getSetting().getAddedLayers();
        for (var i = 0; i < addedLayers.size(); i++) {
            if (addedLayers.get(i).getName().equals(name)) {
                return i;
            }
        }
        return null;
    }

}
