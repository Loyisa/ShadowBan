package cn.loyisa.shadowban.commands.subcommands;

import cn.loyisa.shadowban.ShadowBan;
import cn.loyisa.shadowban.commands.SubCommand;
import cn.loyisa.shadowban.enums.Messages;
import cn.loyisa.shadowban.enums.Permissions;
import cn.loyisa.shadowban.utils.TaskUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnbanCommand extends SubCommand {

    private final ShadowBan shadowBan;

    public UnbanCommand(ShadowBan shadowBan) {
        this.shadowBan = shadowBan;
    }

    @Override
    protected String getName() {
        return "unban";
    }

    @Override
    protected String getDescription() {
        return "从ShadowBan列表移除该名玩家";
    }

    @Override
    protected String getSyntax() {
        return "/shadowban unban <玩家名>";
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
            if (shadowBan.shadowBanMap.containsKey(player.getUniqueId())) {
                sender.sendMessage(Messages.REMOVEING_FROM_BAN_LIST.getMessage());
                shadowBan.shadowBanMap.remove(player.getUniqueId());
                TaskUtils.taskAsync(() -> shadowBan.getStorageManager().getStorageEngine().remove(player));
            } else {
                sender.sendMessage(Messages.REMOVED_FROM_BAN_LIST.getMessage());
            }

        }
    }
}
