package cn.loyisa.shadowban.commands.subcommands;

import cn.loyisa.shadowban.ShadowBan;
import cn.loyisa.shadowban.commands.SubCommand;
import cn.loyisa.shadowban.enums.Messages;
import cn.loyisa.shadowban.enums.Permissions;
import cn.loyisa.shadowban.utils.RandomUtils;
import cn.loyisa.shadowban.utils.TaskUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class BanCommand extends SubCommand {

    private final ShadowBan shadowBan;

    public BanCommand(ShadowBan shadowBan) {
        this.shadowBan = shadowBan;
    }

    @Override
    protected String getName() {
        return "ban";
    }

    @Override
    protected String getDescription() {
        return "把这名玩家加入ShadowBan列表中";
    }

    @Override
    protected String getSyntax() {
        return "/shadowban ban <玩家名>";
    }

    @Override
    protected String getPermission() {
        return Permissions.USE.getPermission();
    }

    @Override
    protected int maxArguments() {
        return 0;
    }

    @Override
    protected boolean canConsoleExecute() {
        return true;
    }

    @Override
    protected void perform(CommandSender sender, String[] args) {
        if (args.length == 2) {
            // 获取玩家名
            OfflinePlayer offp = Bukkit.getOfflinePlayer(args[1]);
            if (offp == null || !offp.isOnline()){
                sender.sendMessage(Messages.NO_PLAYER.getMessage());
                return;
            }
            Player player = offp.getPlayer();
            if (!shadowBan.shadowBanMap.containsKey(player.getUniqueId())) {
                FileConfiguration config = shadowBan.getConfigManager().getConfig();
                sender.sendMessage(Messages.ADDING_TO_BAN_LIST.getMessage());
                shadowBan.shadowBanMap.put(player.getUniqueId(), System.currentTimeMillis()
                        + RandomUtils.nextLong(config.getLong("banwave.minbantime"), config.getLong("banwave.maxbantime")) * 1000);
                TaskUtils.taskAsync(() -> shadowBan.getStorageManager().getStorageEngine().save(player));
            } else {
                sender.sendMessage(Messages.ADDED_TO_BAN_LIST.getMessage());
            }
        }
    }
}
