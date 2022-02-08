package cn.loyisa.shadowban.enums;

import org.bukkit.ChatColor;

public enum Messages {

    LOAD_UP("ShadowBan插件加载中... 作者: Loyisa 版本: "),
    DISABLE("ShadowBan插件关闭中..."),
    PREFIX(format("&6[ShadowBan] &r> &6")),
    CONSOLE_ONLY(format(PREFIX.getMessage() + "此指令只能在控制台使用!")),
    PLAYER_ONLY(format(PREFIX.getMessage() + "此指令不能在控制台使用!")),
    NO_PERMISSION(format(PREFIX.getMessage() + "你没有权限使用这个指令!")),
    NO_PLAYER(format(PREFIX.getMessage() + "未找到此玩家!")),
    AVAILABLE_COMMANDS(format("可用指令")),
    ADDING_TO_BAN_LIST(format(PREFIX.getMessage() + "加入ShadowBan列表中...")),
    ADDED_TO_BAN_LIST(format(PREFIX.getMessage() + "此玩家已经在ShadowBan列表中了")),
    REMOVING_FROM_BAN_LIST(format(PREFIX.getMessage() + "从ShadowBan列表移除此玩家中...")),
    REMOVED_FROM_BAN_LIST(format(PREFIX.getMessage() + "此玩家不在ShadowBan列表中!")),
    RELOAD_PLUGIN(format(PREFIX.getMessage() + "插件配置重载成功"));

    private final String message;

    Messages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public static String format(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
