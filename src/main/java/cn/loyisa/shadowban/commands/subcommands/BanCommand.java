package cn.loyisa.shadowban.commands.subcommands;

import cn.loyisa.shadowban.ShadowBan;
import cn.loyisa.shadowban.commands.SubCommand;
import cn.loyisa.shadowban.enums.Messages;
import cn.loyisa.shadowban.enums.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
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
            OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
            if (!shadowBan.getBanManager().isBanned(player.getUniqueId())) {
                shadowBan.getBanManager().ban(player);
                sender.sendMessage(Messages.ADDING_TO_BAN_LIST.getMessage());
            } else {
                if (sender instanceof Player)
                    sender.sendMessage(Messages.ADDED_TO_BAN_LIST.getMessage());
            }
        }
    }
}
