package com.jmd.ui.frame.download.preview;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.swing.*;

import com.jmd.ui.common.CommonSubFrame;
import com.jmd.util.MyFileUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jmd.common.StaticVar;
import com.jmd.model.geo.Polygon;
import com.jmd.model.task.TaskInstEntity;
import com.jmd.task.TaskStepFunc;

import lombok.extern.slf4j.Slf4j;

import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

@Slf4j
@Component
public class DownloadPreviewFrame extends CommonSubFrame {

    @Serial
    private static final long serialVersionUID = 5621937977510720723L;

    @Autowired
    private TaskStepFunc taskStepFunc;

    private boolean taskStop = false;

    private final DecimalFormat df2 = new DecimalFormat("#.##");
    private final String[] dw = {"B", "KB", "MB", "GB", "TB"};
    private SwingWorker<Void, Void> calculateWorker;

    private final HashMap<Integer, Long> countMap = new HashMap<>();
    private List<Integer> zoomList = null;
    private List<Polygon> polygons = null;

    private final JTextArea textArea;
    private final JComboBox<String> imgTypeComboBox;
    private final JLabel tileCountContentLabel;
    private final JLabel downloadAmountContentLabel;
    private final JLabel diskUsageContentLabel;
    private final JLabel loadingTitleLabel;
    private final JLabel loadingGifIconLabel;

    public DownloadPreviewFrame() throws IOException {

        var scrollPane = new JScrollPane();

        this.textArea = new JTextArea();
        this.textArea.setEditable(false);
        this.textArea.setFocusable(false);
        this.textArea.setLineWrap(true);
        scrollPane.setViewportView(this.textArea);

        JPanel panel = new JPanel();

        JLabel imgTypeTitleLabel = new JLabel("图片格式：");
        imgTypeTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_14);

        this.imgTypeComboBox = new JComboBox<>();
        this.imgTypeComboBox.setFocusable(false);
        this.imgTypeComboBox.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.imgTypeComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"PNG", "WEBP", "JPG-低", "JPG-中", "JPG-高"}));
        this.imgTypeComboBox.setSelectedIndex(0);
        this.imgTypeComboBox.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                changeImageType((String) e.getItem());
            }
        });

        var diskBlockSizeLabel = new JLabel("硬盘簇大小（以NTFS为例）：" + StaticVar.DISK_BLOCK + "字节");
        diskBlockSizeLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_14);

        var tileCountTitleLabel = new JLabel("瓦片图下载总数：");
        tileCountTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_14);

        this.tileCountContentLabel = new JLabel("正在计算...");
        this.tileCountContentLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_14);

        var downloadAmountTitleLabel = new JLabel("预计下载总量：");
        downloadAmountTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_14);

        this.downloadAmountContentLabel = new JLabel("正在计算...");
        this.downloadAmountContentLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_14);

        var diskUsageTitleLabel = new JLabel("预计硬盘占用：");
        diskUsageTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_14);

        this.diskUsageContentLabel = new JLabel("正在计算...");
        this.diskUsageContentLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_14);

        this.loadingTitleLabel = new JLabel("正在计算...");
        this.loadingTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_14);
        this.loadingTitleLabel.setVisible(false);

        this.loadingGifIconLabel = new JLabel();
        this.loadingGifIconLabel.setIcon(new ImageIcon(MyFileUtils.getResourceFileBytes("assets/icon/loading.gif")));
        this.loadingGifIconLabel.setVisible(false);

        // Right panel layout
        var gl_panel = new GroupLayout(panel);
        gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
                .createSequentialGroup().addContainerGap()
                .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_panel.createSequentialGroup()
                                .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                                        .addGroup(gl_panel.createSequentialGroup().addComponent(imgTypeTitleLabel)
                                                .addPreferredGap(ComponentPlacement.RELATED).addComponent(
                                                        this.imgTypeComboBox, GroupLayout.PREFERRED_SIZE,
                                                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(gl_panel.createSequentialGroup()
                                                .addComponent(this.loadingGifIconLabel, GroupLayout.PREFERRED_SIZE, 32,
                                                        GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(this.loadingTitleLabel, GroupLayout.DEFAULT_SIZE,
                                                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(6))
                                        .addComponent(diskBlockSizeLabel))
                                .addContainerGap(28, Short.MAX_VALUE))
                        .addGroup(
                                gl_panel.createSequentialGroup().addComponent(tileCountTitleLabel)
                                        .addPreferredGap(ComponentPlacement.RELATED)
                                        .addComponent(this.tileCountContentLabel, GroupLayout.DEFAULT_SIZE, 160,
                                                Short.MAX_VALUE)
                                        .addContainerGap())
                        .addGroup(gl_panel.createSequentialGroup().addComponent(downloadAmountTitleLabel)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(this.downloadAmountContentLabel, GroupLayout.DEFAULT_SIZE, 174,
                                        Short.MAX_VALUE)
                                .addContainerGap())
                        .addGroup(gl_panel.createSequentialGroup().addComponent(diskUsageTitleLabel)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(this.diskUsageContentLabel, GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                                .addContainerGap()))));
        gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
                .createSequentialGroup().addContainerGap()
                .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(imgTypeTitleLabel).addComponent(
                        this.imgTypeComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED).addComponent(diskBlockSizeLabel)
                .addPreferredGap(ComponentPlacement.UNRELATED)
                .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(tileCountTitleLabel)
                        .addComponent(this.tileCountContentLabel))
                .addPreferredGap(ComponentPlacement.UNRELATED)
                .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(downloadAmountTitleLabel)
                        .addComponent(this.downloadAmountContentLabel))
                .addPreferredGap(ComponentPlacement.UNRELATED)
                .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(diskUsageTitleLabel)
                        .addComponent(this.diskUsageContentLabel))
                .addPreferredGap(ComponentPlacement.RELATED, 218, Short.MAX_VALUE)
                .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                        .addComponent(this.loadingTitleLabel, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                        .addComponent(this.loadingGifIconLabel, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
                .addContainerGap()));
        panel.setLayout(gl_panel);

        // This frame layout
        var groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                        .addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 330, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE).addContainerGap()));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                .addComponent(panel, GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE)
                .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE));

        getContentPane().setLayout(groupLayout);

        this.setTitle("预估下载量");
        this.setSize(new Dimension(650, 383));
        this.setVisible(false);
        this.setResizable(false);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (calculateWorker != null) {
                    taskStop = true;
                    calculateWorker.cancel(true);
                }
            }
        });

    }

    @PostConstruct
    private void init() {

    }

    @PreDestroy
    protected void destroy() {
        super.destroy();
    }

    public void showPreview(List<Integer> zoomList, List<Polygon> polygon) {
        this.clear();
        this.taskStop = false;
        this.setVisible(true);
        this.calculate(zoomList, polygon);
    }

    private void clear() {
        this.countMap.clear();
        this.zoomList = null;
        this.polygons = null;
        this.textArea.setText("");
        this.tileCountContentLabel.setText("");
        this.downloadAmountContentLabel.setText("");
        this.diskUsageContentLabel.setText("");
        this.loadingTitleLabel.setVisible(false);
        this.loadingGifIconLabel.setVisible(false);
    }

    private void calculate(List<Integer> _zoomList, List<Polygon> _polygons) {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws InterruptedException, InvocationTargetException {
                SwingUtilities.invokeAndWait(() -> {
                    loadingTitleLabel.setVisible(true);
                    loadingGifIconLabel.setVisible(true);
                });
                zoomList = _zoomList;
                polygons = _polygons;
                countMap.clear();
                consoleLog("开始计算...");
                long count = 0L;
                long start = System.currentTimeMillis();
                for (int z : zoomList) {
                    if (taskStop) {
                        break;
                    } else {
                        try {
                            TaskInstEntity inst = taskStepFunc.tileTaskInstCalculation(z, polygons, (e) -> consoleLog(e));
                            countMap.put(z, inst.getRealCount());
                            count = count + inst.getRealCount();
                        } catch (InterruptedException | ExecutionException e) {
                            log.error("Tile Calculation Error", e);
                        }
                    }
                }
                long end = System.currentTimeMillis();
                double time = (double) (end - start) / 1000.0;
                consoleLog("计算完成");
                consoleLog("需要下载的总数：" + count);
                consoleLog("瓦片图计算所用时间：" + df2.format(time) + "秒");
                consoleLog("结果仅供参考");
                long finalCount = count;
                SwingUtilities.invokeAndWait(() -> {
                    tileCountContentLabel.setText(String.valueOf(finalCount));
                    loadingTitleLabel.setVisible(false);
                    loadingGifIconLabel.setVisible(false);
                });
                changeImageType((String) imgTypeComboBox.getSelectedItem());
                calculateWorker = null;
                return null;
            }
        };
        worker.execute();
        calculateWorker = worker;
    }

    private void changeImageType(String type) {
        if (zoomList == null || countMap.size() == 0) {
            return;
        }
        SwingUtilities.invokeLater(() -> {
            switch (type) {
                case "PNG" -> {
                    downloadAmountContentLabel.setText(getTileSizeAmount(zoomList, StaticVar.PNG_PER_SIZE_MAP, countMap));
                    diskUsageContentLabel.setText(getTileDiskUsageAmount(zoomList, StaticVar.PNG_PER_SIZE_MAP, countMap, StaticVar.DISK_BLOCK));
                }
                case "WEBP" -> {
                    downloadAmountContentLabel.setText(getTileSizeAmount(zoomList, StaticVar.WEBP_PER_SIZE_MAP, countMap));
                    diskUsageContentLabel.setText(getTileDiskUsageAmount(zoomList, StaticVar.WEBP_PER_SIZE_MAP, countMap, StaticVar.DISK_BLOCK));
                }
                case "JPG-低" -> {
                    downloadAmountContentLabel.setText(getTileSizeAmount(zoomList, StaticVar.JPG_LOW_PER_SIZE_MAP, countMap));
                    diskUsageContentLabel.setText(getTileDiskUsageAmount(zoomList, StaticVar.JPG_LOW_PER_SIZE_MAP, countMap, StaticVar.DISK_BLOCK));
                }
                case "JPG-中" -> {
                    downloadAmountContentLabel.setText(getTileSizeAmount(zoomList, StaticVar.JPG_MIDDLE_PER_SIZE_MAP, countMap));
                    diskUsageContentLabel.setText(getTileDiskUsageAmount(zoomList, StaticVar.JPG_MIDDLE_PER_SIZE_MAP, countMap, StaticVar.DISK_BLOCK));
                }
                case "JPG-高" -> {
                    downloadAmountContentLabel.setText(getTileSizeAmount(zoomList, StaticVar.JPG_HIGH_PER_SIZE_MAP, countMap));
                    diskUsageContentLabel.setText(getTileDiskUsageAmount(zoomList, StaticVar.JPG_HIGH_PER_SIZE_MAP, countMap, StaticVar.DISK_BLOCK));
                }
                default -> {
                }
            }
        });
    }

    private String getTileSizeAmount(List<Integer> zoomList, Map<Integer, Double> perSizeMap,
                                     Map<Integer, Long> countMap) {
        String dx = null;
        double allSize = 0.0;
        for (int z : zoomList) {
            allSize = allSize + perSizeMap.get(z) * countMap.get(z);
        }
        for (int i = 0; i <= 4; i++) {
            dx = dw[i];
            if (allSize < 1024) {
                break;
            } else {
                allSize = allSize / 1024;
            }
        }
        return df2.format(allSize) + dx;
    }

    private String getTileDiskUsageAmount(List<Integer> zoomList, Map<Integer, Double> perSizeMap,
                                          Map<Integer, Long> countMap, double diskBlock) {
        diskBlock = diskBlock * 0.8; // 更接近真实数据
        String dx = null;
        double allSize = 0.0;
        for (int z : zoomList) {
            double perAvg = perSizeMap.get(z);
            double eachUsage = 0.0;
            while (true) {
                eachUsage = eachUsage + diskBlock;
                if (perAvg % diskBlock < perAvg) {
                    perAvg = perAvg - diskBlock;
                } else {
                    break;
                }
            }
            allSize = allSize + eachUsage * countMap.get(z);
        }
        for (int i = 0; i <= 4; i++) {
            dx = dw[i];
            if (allSize < 1024) {
                break;
            } else {
                allSize = allSize / 1024;
            }
        }
        return df2.format(allSize) + dx;
    }

    /* 控制台打印 */
    private void consoleLog(String log) {
        SwingUtilities.invokeLater(() -> {
            textArea.append(log + "\n");
            textArea.setCaretPosition(textArea.getText().length());
        });
    }

}
