package com.jmd.browser.handler

import com.jmd.browser.view.BrowserViewContainer
import org.cef.browser.CefBrowser
import org.cef.browser.CefFrame
import org.cef.callback.CefContextMenuParams
import org.cef.callback.CefMenuModel
import org.cef.callback.CefMenuModel.MenuId
import org.cef.handler.CefContextMenuHandlerAdapter

class MenuHandler(private val container: BrowserViewContainer) : CefContextMenuHandlerAdapter() {

    companion object {
        private const val MENU_ID_DEV_TOOL = 1000001
    }

    override fun onBeforeContextMenu(
        browser: CefBrowser,
        frame: CefFrame,
        params: CefContextMenuParams,
        model: CefMenuModel
    ) {
        // 清除菜单项
        model.clear()
        // 剪切、复制、粘贴
        model.addItem(MenuId.MENU_ID_COPY, "复制")
        model.addItem(MenuId.MENU_ID_CUT, "剪切")
        model.addItem(MenuId.MENU_ID_PASTE, "粘贴")
        model.addSeparator() // 分割线
        model.addItem(MenuId.MENU_ID_BACK, "返回")
        model.setEnabled(MenuId.MENU_ID_BACK, browser.canGoBack())
        model.addItem(MenuId.MENU_ID_FORWARD, "前进")
        model.setEnabled(MenuId.MENU_ID_FORWARD, browser.canGoForward())
        model.addItem(MenuId.MENU_ID_RELOAD, "刷新")
        model.addSeparator() // 分割线
        model.addItem(MenuId.MENU_ID_VIEW_SOURCE, "查看源码")
        model.addItem(MENU_ID_DEV_TOOL, "开发者工具")
        //创建子菜单
//        CefMenuModel cmodel = model.addSubMenu(MENU_ID_INJECTION, "脚本注入");
//        cmodel.addItem(MENU_ID_ADDTEXT, "添加一段文本");
    }

    override fun onContextMenuCommand(
        browser: CefBrowser,
        frame: CefFrame,
        params: CefContextMenuParams,
        commandId: Int,
        eventFlags: Int
    ): Boolean {
        when (commandId) {
            MenuId.MENU_ID_RELOAD -> {
                browser.reload()
                return true
            }

            MENU_ID_DEV_TOOL -> {
                container.toggleDevTools()
                return true
            }
        }
        return false
    }

}