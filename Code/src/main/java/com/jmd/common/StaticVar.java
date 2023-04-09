package com.jmd.common;

import java.awt.Font;
import java.io.Serial;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jmd.entity.theme.ThemeEntity;
import com.jmd.util.CommonUtils;
import com.jmd.util.FontUtils;

import javax.swing.*;

public class StaticVar {

    public static final boolean IS_Mac = CommonUtils.isMac();
    public static final boolean IS_Windows = CommonUtils.isWindows();
    public static final boolean IS_Windows_10 = CommonUtils.isWindows10();
    public static final boolean IS_Windows_11 = CommonUtils.isWindows11();

    public static final Font FONT_SourceHanSansCNNormal_12 = FontUtils.SourceHanSansCNNormal(12);
    public static final Font FONT_SourceHanSansCNNormal_13 = FontUtils.SourceHanSansCNNormal(13);
    public static final Font FONT_SourceHanSansCNNormal_14 = FontUtils.SourceHanSansCNNormal(14);
    public static final Font FONT_YaHeiConsolas_13 = FontUtils.YaHeiConsolas(13);

    public static final Font ICON_FONT_ICOMOON_18 = FontUtils.getIconFont("IcoMoon.ttf", 18);
    public static final Font ICON_FONT_ZONDICONS_18 = FontUtils.getIconFont("Zondicons.ttf", 18);
    public static final Font ICON_FONT_BRANDS_28 = FontUtils.getIconFont("Brands.ttf", 28);

    public static final int TILE_WIDTH = 256;
    public static final int TILE_HEIGHT = 256;

    public static final double DISK_BLOCK = 4096.0;
    public static final HashMap<Integer, Double> PNG_PER_SIZE_MAP = new HashMap<>() {
        @Serial
        private static final long serialVersionUID = 7197404512099224555L;

        {
            put(3, 15956.17);
            put(4, 12584.93);
            put(5, 20054.56);
            put(6, 18043.19);
            put(7, 15228.87);
            put(8, 12510.82);
            put(9, 8557.34);
            put(10, 6822.98);
            put(11, 5928.59);
            put(12, 8816.01);
            put(13, 5991.90);
            put(14, 3824.78);
            put(15, 3429.01);
            put(16, 1863.61);
            put(17, 1835.58);
            put(18, 1986.90);
            put(19, 2425.66);
            put(20, 1401.93);
            put(21, 1037.25);
        }
    };
    public static final HashMap<Integer, Double> WEBP_PER_SIZE_MAP = new HashMap<>() {
        @Serial
        private static final long serialVersionUID = 7197404512099224555L;

        {
            put(3, 15956.17);
            put(4, 12584.93);
            put(5, 20054.56);
            put(6, 18043.19);
            put(7, 15228.87);
            put(8, 12510.82);
            put(9, 8557.34);
            put(10, 6822.98);
            put(11, 5928.59);
            put(12, 8816.01);
            put(13, 5991.90);
            put(14, 3824.78);
            put(15, 3429.01);
            put(16, 1863.61);
            put(17, 1835.58);
            put(18, 1986.90);
            put(19, 2425.66);
            put(20, 1401.93);
            put(21, 1037.25);
        }
    };
    public static final HashMap<Integer, Double> JPG_LOW_PER_SIZE_MAP = new HashMap<>() {
        @Serial
        private static final long serialVersionUID = -982542885984884007L;

        {
            put(3, 15956.17 / 4.34);
            put(4, 12584.93 / 4.30);
            put(5, 20054.56 / 4.65);
            put(6, 18043.19 / 4.23);
            put(7, 15228.87 / 3.98);
            put(8, 12510.82 / 3.69);
            put(9, 8557.34 / 3.10);
            put(10, 6822.98 / 2.67);
            put(11, 5928.59 / 2.60);
            put(12, 8816.01 / 3.42);
            put(13, 5991.90 / 2.52);
            put(14, 3824.78 / 1.50);
            put(15, 3429.01 / 1.45);
            put(16, 1863.61 / 1.15);
            put(17, 1835.58 / 1.15);
            put(18, 1986.90 / 1.15);
            put(19, 2425.66 / 1.25);
            put(20, 1401.93 / 1.10);
            put(21, 1037.25 / 1.10);
        }
    };
    public static final HashMap<Integer, Double> JPG_MIDDLE_PER_SIZE_MAP = new HashMap<>() {
        @Serial
        private static final long serialVersionUID = -4810604191215005476L;

        {
            put(3, 15956.17 / 2.20);
            put(4, 12584.93 / 2.20);
            put(5, 20054.56 / 2.20);
            put(6, 18043.19 / 2.17);
            put(7, 15228.87 / 2.04);
            put(8, 12510.82 / 1.97);
            put(9, 8557.34 / 1.50);
            put(10, 6822.98 / 1.35);
            put(11, 5928.59 / 1.20);
            put(12, 8816.01 / 2.00);
            put(13, 5991.90 / 1.30);
            put(14, 3824.78 / 1.20);
            put(15, 3429.01 / 1.20);
            put(16, 1863.61 / 1.10);
            put(17, 1835.58 / 1.10);
            put(18, 1986.90 / 1.10);
            put(19, 2425.66 / 1.15);
            put(20, 1401.93 / 1.10);
            put(21, 1037.25 / 1.10);
        }
    };
    public static final HashMap<Integer, Double> JPG_HIGH_PER_SIZE_MAP = new HashMap<>() {
        @Serial
        private static final long serialVersionUID = 7197404512099224555L;

        {
            put(3, 15956.17);
            put(4, 12584.93);
            put(5, 20054.56);
            put(6, 18043.19);
            put(7, 15228.87);
            put(8, 12510.82);
            put(9, 8557.34);
            put(10, 6822.98);
            put(11, 5928.59);
            put(12, 8816.01);
            put(13, 5991.90);
            put(14, 3824.78);
            put(15, 3429.01);
            put(16, 1863.61);
            put(17, 1835.58);
            put(18, 1986.90);
            put(19, 2425.66);
            put(20, 1401.93);
            put(21, 1037.25);
        }
    };

    public static final List<ThemeEntity> THEME_LIST = new ArrayList<>() {
        @Serial
        private static final long serialVersionUID = 2023328714357147685L;

        {
            if (StaticVar.IS_Windows) {
                add(new ThemeEntity("Windows", new ArrayList<>() {
                    @Serial
                    private static final long serialVersionUID = -8432603123832743410L;

                    {
                        add(new ThemeEntity(1, "Default", UIManager.getSystemLookAndFeelClassName()));
                        add(new ThemeEntity(1, "Classic", "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel"));
                    }
                }));
            } else if (StaticVar.IS_Mac) {
                add(new ThemeEntity("Mac OS", new ArrayList<>() {
                    @Serial
                    private static final long serialVersionUID = 7116227973269334283L;

                    {
                        add(new ThemeEntity(1, "Default", UIManager.getSystemLookAndFeelClassName()));
                    }
                }));
            }
            add(new ThemeEntity("Swing", new ArrayList<>() {
                @Serial
                private static final long serialVersionUID = 954378432271213559L;

                {
                    add(new ThemeEntity(2, "metal", "javax.swing.plaf.metal.MetalLookAndFeel"));
                    add(new ThemeEntity(2, "nimbus", "javax.swing.plaf.nimbus.NimbusLookAndFeel"));
                }
            }));
            add(new ThemeEntity("Flatlaf Default", new ArrayList<>() {
                @Serial
                private static final long serialVersionUID = -6842238699142942829L;

                {
                    add(new ThemeEntity(3, "Intellij", "com.formdev.flatlaf.FlatIntelliJLaf"));
                    add(new ThemeEntity(3, "Darcula", "com.formdev.flatlaf.FlatDarculaLaf"));
                }
            }));
            add(new ThemeEntity("Flatlaf Intellij Themes", new ArrayList<>() {
                @Serial
                private static final long serialVersionUID = 8879743471711607953L;

                {
                    add(new ThemeEntity(3, "Arc", "com.formdev.flatlaf.intellijthemes.FlatArcIJTheme"));
                    add(new ThemeEntity(3, "Arc - Orange", "com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme"));
                    add(new ThemeEntity(3, "Arc Dark", "com.formdev.flatlaf.intellijthemes.FlatArcDarkIJTheme"));
                    add(new ThemeEntity(3, "Arc Dark - Orange", "com.formdev.flatlaf.intellijthemes.FlatArcDarkOrangeIJTheme"));
                    add(new ThemeEntity(3, "Carbon", "com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme"));
                    add(new ThemeEntity(3, "Cobalt 2", "com.formdev.flatlaf.intellijthemes.FlatCobalt2IJTheme"));
                    add(new ThemeEntity(3, "Cyan light", "com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme"));
                    add(new ThemeEntity(3, "Dark Flat", "com.formdev.flatlaf.intellijthemes.FlatDarkFlatIJTheme"));
                    add(new ThemeEntity(3, "Dark purple", "com.formdev.flatlaf.intellijthemes.FlatDarkPurpleIJTheme"));
                    add(new ThemeEntity(3, "Dracula", "com.formdev.flatlaf.intellijthemes.FlatDraculaIJTheme"));
                    add(new ThemeEntity(3, "Gradianto Dark Fuchsia", "com.formdev.flatlaf.intellijthemes.FlatGradiantoDarkFuchsiaIJTheme"));
                    add(new ThemeEntity(3, "Gradianto Deep Ocean", "com.formdev.flatlaf.intellijthemes.FlatGradiantoDeepOceanIJTheme"));
                    add(new ThemeEntity(3, "Gradianto Midnight Blue", "com.formdev.flatlaf.intellijthemes.FlatGradiantoMidnightBlueIJTheme"));
                    add(new ThemeEntity(3, "Gradianto Nature Green", "com.formdev.flatlaf.intellijthemes.FlatGradiantoNatureGreenIJTheme"));
                    add(new ThemeEntity(3, "Gray", "com.formdev.flatlaf.intellijthemes.FlatGrayIJTheme"));
                    add(new ThemeEntity(3, "Gruvbox Dark Hard", "com.formdev.flatlaf.intellijthemes.FlatGruvboxDarkHardIJTheme"));
                    add(new ThemeEntity(3, "Gruvbox Dark Medium", "com.formdev.flatlaf.intellijthemes.FlatGruvboxDarkMediumIJTheme"));
                    add(new ThemeEntity(3, "Gruvbox Dark Soft", "com.formdev.flatlaf.intellijthemes.FlatGruvboxDarkSoftIJTheme"));
                    add(new ThemeEntity(3, "Hiberbee Dark", "com.formdev.flatlaf.intellijthemes.FlatHiberbeeDarkIJTheme"));
                    add(new ThemeEntity(3, "High contrast", "com.formdev.flatlaf.intellijthemes.FlatHighContrastIJTheme"));
                    add(new ThemeEntity(3, "Light Flat", "com.formdev.flatlaf.intellijthemes.FlatLightFlatIJTheme"));
                    add(new ThemeEntity(3, "Material Design Dark", "com.formdev.flatlaf.intellijthemes.FlatMaterialDesignDarkIJTheme"));
                    add(new ThemeEntity(3, "Monocai", "com.formdev.flatlaf.intellijthemes.FlatMonocaiIJTheme"));
                    add(new ThemeEntity(3, "Monokai Pro", "com.formdev.flatlaf.intellijthemes.FlatMonokaiProIJTheme"));
                    add(new ThemeEntity(3, "Nord", "com.formdev.flatlaf.intellijthemes.FlatNordIJTheme"));
                    add(new ThemeEntity(3, "One Dark", "com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme"));
                    add(new ThemeEntity(3, "Solarized Light", "com.formdev.flatlaf.intellijthemes.FlatSolarizedLightIJTheme"));
                    add(new ThemeEntity(3, "Solarized Dark", "com.formdev.flatlaf.intellijthemes.FlatSolarizedDarkIJTheme"));
                    add(new ThemeEntity(3, "Spacegray", "com.formdev.flatlaf.intellijthemes.FlatSpacegrayIJTheme"));
                    add(new ThemeEntity(3, "Vuesion", "com.formdev.flatlaf.intellijthemes.FlatVuesionIJTheme"));
                    add(new ThemeEntity(3, "Xcode-Dark", "com.formdev.flatlaf.intellijthemes.FlatXcodeDarkIJTheme"));
                }
            }));
            add(new ThemeEntity("Flatlaf Material Themes", new ArrayList<>() {
                @Serial
                private static final long serialVersionUID = -1334799371917483719L;

                {
                    add(new ThemeEntity(3, "Arc Dark", "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatArcDarkIJTheme"));
                    // add(new ThemeEntity(3, "Arc Dark Contrast", "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatArcDarkContrastIJTheme"));
                    add(new ThemeEntity(3, "Atom One Light", "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneLightIJTheme"));
                    // add(new ThemeEntity(3, "Atom One Light Contrast", "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneLightContrastIJTheme"));
                    add(new ThemeEntity(3, "Atom One Dark", "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkIJTheme"));
                    // add(new ThemeEntity(3, "Atom One Dark Contrast", "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkContrastIJTheme"));
                    add(new ThemeEntity(3, "Dracula", "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatDraculaIJTheme"));
                    // add(new ThemeEntity(3, "Dracula Contrast", "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatDraculaContrastIJTheme"));
                    add(new ThemeEntity(3, "GitHub", "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatGitHubIJTheme"));
                    // add(new ThemeEntity(3, "GitHub Contrast", "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatGitHubContrastIJTheme"));
                    add(new ThemeEntity(3, "GitHub Dark", "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatGitHubDarkIJTheme"));
                    // add(new ThemeEntity(3, "GitHub Dark Contrast", "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatGitHubDarkContrastIJTheme"));
                    add(new ThemeEntity(3, "Light Owl", "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatLightOwlIJTheme"));
                    // add(new ThemeEntity(3, "Light Owl Contrast", "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatLightOwlContrastIJTheme"));
                    add(new ThemeEntity(3, "Material Lighter", "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialLighterIJTheme"));
                    // add(new ThemeEntity(3, "Material Lighter Contrast", "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialLighterIJTheme"));
                    add(new ThemeEntity(3, "Material Darker", "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialDarkerIJTheme"));
                    // add(new ThemeEntity(3, "Material Darker Contrast", "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialDarkerContrastIJTheme"));
                    add(new ThemeEntity(3, "Material Oceanic", "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialOceanicIJTheme"));
                    // add(new ThemeEntity(3, "Material Oceanic Contrast", "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialOceanicContrastIJTheme"));
                    add(new ThemeEntity(3, "Material Deep Ocean", "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialDeepOceanIJTheme"));
                    // add(new ThemeEntity(3, "Material Deep Ocean Contrast", "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialDeepOceanContrastIJTheme"));
                    add(new ThemeEntity(3, "Material Palenight", "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialPalenightIJTheme"));
                    // add(new ThemeEntity(3, "Material Palenight Contrast", "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialPalenightContrastIJTheme"));
                    add(new ThemeEntity(3, "Monokai Pro", "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMonokaiProIJTheme"));
                    // add(new ThemeEntity(3, "Monokai Pro Contrast", "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMonokaiProContrastIJTheme"));
                    add(new ThemeEntity(3, "Moonlight", "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMoonlightIJTheme"));
                    // add(new ThemeEntity(3, "Moonlight Contrast", "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMoonlightContrastIJTheme"));
                    add(new ThemeEntity(3, "Night Owl", "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatNightOwlIJTheme"));
                    // add(new ThemeEntity(3, "Night Owl Contrast", "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatNightOwlContrastIJTheme"));
                    add(new ThemeEntity(3, "Solarized Dark", "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatSolarizedDarkIJTheme"));
                    // add(new ThemeEntity(3, "Solarized Dark Contrast", "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatSolarizedDarkContrastIJTheme"));
                    add(new ThemeEntity(3, "Solarized Light", "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatSolarizedLightIJTheme"));
                    // add(new ThemeEntity(3, "Solarized Light Contrast", "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatSolarizedLightContrastIJTheme"));
                }
            }));
            add(new ThemeEntity("Jtattoo", new ArrayList<>() {
                @Serial
                private static final long serialVersionUID = 7029664277402612130L;

                {
                    add(new ThemeEntity(4, "acryl", "com.jtattoo.plaf.acryl.AcrylLookAndFeel"));
                    add(new ThemeEntity(4, "aero", "com.jtattoo.plaf.aero.AeroLookAndFeel"));
                    add(new ThemeEntity(4, "aluminium", "com.jtattoo.plaf.aluminium.AluminiumLookAndFeel"));
                    add(new ThemeEntity(4, "bernstein", "com.jtattoo.plaf.bernstein.BernsteinLookAndFeel"));
                    add(new ThemeEntity(4, "fast", "com.jtattoo.plaf.fast.FastLookAndFeel"));
                    add(new ThemeEntity(4, "graphite", "com.jtattoo.plaf.graphite.GraphiteLookAndFeel"));
                    add(new ThemeEntity(4, "hifi", "com.jtattoo.plaf.hifi.HiFiLookAndFeel"));
                    add(new ThemeEntity(4, "luna", "com.jtattoo.plaf.luna.LunaLookAndFeel"));
                    add(new ThemeEntity(4, "mcwin", "com.jtattoo.plaf.mcwin.McWinLookAndFeel"));
                    add(new ThemeEntity(4, "mint", "com.jtattoo.plaf.mint.MintLookAndFeel"));
                    add(new ThemeEntity(4, "smart", "com.jtattoo.plaf.smart.SmartLookAndFeel"));
                    add(new ThemeEntity(4, "noire", "com.jtattoo.plaf.noire.NoireLookAndFeel"));
                    add(new ThemeEntity(4, "texture", "com.jtattoo.plaf.texture.TextureLookAndFeel"));
                }
            }));
        }
    };

}
