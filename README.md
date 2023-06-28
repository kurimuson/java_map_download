# 地图瓦片图下载器

#### 本处为备份仓库，gitee为主仓库（本处只在新版本发布时同步最新代码）
#### gitee仓库地址：https://gitee.com/CrimsonHu/java_map_download

#### 使用JetBrains Runtime 17，SpringBoot 3.0，JCEF版本为Chromium 104

#### 介绍
使用Java开发的地图瓦片图下载工具，支持以下XYZ瓦片图下载与合并。多线程瓦片图下载，最大限度地使用本机网络资源。
- OpenStreetMap
- 谷歌地图（需要代理）
- 天地图（务必更换自己的key，并注意配额）
- 高德地图
- 腾讯地图
- 必应地图

#### 声明
- 本项目使用GPL 2.0协议开源，任何基于本项目的二次开发需遵守相关开源协议。
- 本项目仅为个人兴趣开发，不收费，作者也不提供任何付费服务。
- 本项目仅供个人学习研究使用。
- 本项目禁止商用，禁止在企业项目开发中使用此下载器下载地图，禁止使用此项目以及基于此项目二次开发的软件从事盈利活动。
- Build程序仅供方便预览本项目的各项功能，不作为最终运行本体。

#### 注意
该程序会挂在系统托盘，退出程序请在托盘中右键程序图标退出

#### 集思广益
各位若有瓦片图纠偏的思路欢迎留言讨论

#### Build下载地址（已打包的可执行程序，解压即可运行）
- 更新日期：2023-06-28（下载最新版注意该更新时间）
- 重要提示：下载谷歌地图需正确使用代理，不能下载就是代理没设置好
- 若无法打开，请将文件夹改为英文，并注意文件夹所在详细路径是否为全英文
- 使用多网盘发布，防止链接失效（阿里云盘不允许分享压缩包，故不使用）
- 百度网盘：https://pan.baidu.com/s/1CA7sdH6zL4OjJxVydKwrWQ  密码：mdve 
- 天翼云盘：https://cloud.189.cn/t/IBFrIzIFZz6j 密码: 5bgb
- 联通云盘：https://pan.wo.cn/s/2N1u0E1184 密码: uqVN

#### 软件说明
1. 使用SpringBoot+Swing+Angular开发的桌面程序
2. 内置若干Swing主题皮肤
3. Webview使用JetBrains Runtime自带的Chromium Embedded Framework
4. 支持Windows与macOS（macOS上需要自行运行代码编译）
5. 支持png、jpg、webp格式存储瓦片图，并支持瓦片图合并
6. 多线程+okhttp3瓦片图下载，最大限度地使用网络资源，拒绝付费限速
7. 使用OpenCV进行瓦片图合并，支持大尺寸png合成图

#### 新版内容
- 可以添加自定义图层，你甚至可以去找相关资源，下载游戏地图
- 本地瓦片预览，以及本地瓦片图web服务

![输入图片说明](Other/image/frame/add-tile-setting.png)
![输入图片说明](Other/image/frame/add-tile-frame.png)
![输入图片说明](Other/image/tile/AQGA19U6SF0O7TQJGZGR8Q.png)
![输入图片说明](Other/image/frame/tile-view-setting.png)
![输入图片说明](Other/image/frame/tile-view-frame-1.png)
![输入图片说明](Other/image/frame/tile-view-browser.png)

#### 下版本功能预览
1. 瓦片合并将做成一个独立功能（开发中，未来两个版本内更新）[关联issue](https://gitee.com/CrimsonHu/java_map_download/issues/I7CA35)
2. 使用滤镜功能实现自定义风格瓦片图下载（该功能已推上计划日程）[关联issue](https://gitee.com/CrimsonHu/java_map_download/issues/I7B3XK)

#### 主要功能
XYZ瓦片图下载与拼接
![输入图片说明](Other/image/tile/163712_032f9f19_1403243.webp)
![输入图片说明](Other/image/tile/192008_a3e72cda_1403243.webp)
![输入图片说明](Other/image/tile/194201_51cbcc76_1403243.webp)
![输入图片说明](Other/image/tile/ZWGLCVCLS2V57.png)
![输入图片说明](Other/image/tile/YL2S6HW.png)
![输入图片说明](Other/image/tile/235757_070c3fc7_1403243.webp)
![输入图片说明](Other/image/tile/191831_0fe37c36_1403243.webp)
![输入图片说明](Other/image/tile/191841_58a9107e_1403243.webp)
![输入图片说明](Other/image/tile/184433_266b9408_1403243.webp)
![输入图片说明](Other/image/tile/AQGA19U6SF0O7TQJGZGR8Q.png)

#### 主要界面
![输入图片说明](Other/image/frame/main-frame-1.png)
![输入图片说明](Other/image/frame/main-frame-2.png)
![输入图片说明](Other/image/frame/main-frame-3.png)
![输入图片说明](Other/image/frame/download-frame-1.png)
![输入图片说明](Other/image/frame/download-frame-2.png)
![输入图片说明](Other/image/frame/tile-view-setting.png)
![输入图片说明](Other/image/frame/tile-view-frame-1.png)
![输入图片说明](Other/image/frame/tile-view-browser.png)
![输入图片说明](Other/image/frame/add-tile-setting.png)
![输入图片说明](Other/image/frame/add-tile-frame.png)

#### 悬浮窗
![输入图片说明](Other/image/frame/float-window-1.png)
![输入图片说明](Other/image/frame/float-window-2.png)

#### 代理设置

下载谷歌地图需设置正确的代理

![输入图片说明](Other/image/frame/proxy-1.png)
![输入图片说明](Other/image/frame/proxy-2.png)

#### 各主题
![输入图片说明](Other/image/frame/theme-1.png)
![输入图片说明](Other/image/frame/theme-2.png)
![输入图片说明](Other/image/frame/theme-3.png)
![输入图片说明](Other/image/frame/theme-4.png)
![输入图片说明](Other/image/frame/theme-5.png)
![输入图片说明](Other/image/frame/theme-6.png)
![输入图片说明](Other/image/frame/theme-7.png)

#### 更新历史
- 2023-06-28：添加本地瓦片预览功能，添加本地瓦片图web服务功能；优化代码，更换为gradle构建项目，部分代码使用kotlin重构
- 2023-05-30：添加桌面悬浮窗，优化错误瓦片处理，优化自定义图层
- 2023-04-09：优化大量代码，添加[自定义图层功能](https://gitee.com/CrimsonHu/java_map_download/issues/I6KPWN)，添加webp支持，优化拼接大图导出格式
- 2022-11-26：JRE换为JetBrains Runtime 17，更新至SpringBoot 3.0.0，删除JxBrowser与JavaFX WebView，优化大量代码，更新谷歌地图域名
- 2022-03-25：更新至Java17 LTS，SpringBoot 2.6.4，JCEF更新至Chromium95内核；优化代码
- 2021-04-09：优化依赖结构，减少打包体积（注意：不要进行无意义的超巨大尺寸合并，那样OpenCV会内存溢出）
- 2021-03-24：添加腾讯地图地图，添加坐标类型显示，修复部分问题
- 2021-03-22：添加天地图key更换功能、添加必应地图，添加并更换默认WebView为Chromium Embedded Framework（JCEF）
- 2021-03-01：优化界面显示，修复部分问题
- 2021-02-18：默认地图设为高德地图；添加http代理支持，用于下载谷歌地图
- 2020-11-28：优化错误瓦片图自动重新下载功能
- 2020-11-27：初步添加错误瓦片图自动重新下载功能，解决无法下载天地图的问题

#### 代码运行说明
1. 开发环境：JetBrains Runtime 17，Angular 15
2. IDE需要安装lombok插件
3. 在lib目录下opencv(原版备份).jar文件中，提取出其中的native文件，在Code目录下创建名为native的文件夹，将其放入
![输入图片说明](Other/image/other/opencv.png)
4. 在SpringBoot项目中，解压db.7z，将db.sqlite3放入至resource目录下
![输入图片说明](Other/image/other/sqlite.png)
5. 编译Angular项目，将dist目录下的web文件夹放入SpringBoot项目的resource目录下
![输入图片说明](Other/image/other/web.png)

#### JetBrains Runtime下载地址
- https://github.com/JetBrains/JetBrainsRuntime/tree/jbr17
- 在Binaries for developers这一栏中下载“JBR with JCEF”，文件名以“jbrsdk_jcef”开头的


