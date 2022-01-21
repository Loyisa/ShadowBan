package cn.loyisa.shadowban.commands.subcommands;

import cn.loyisa.shadowban.ShadowBan;
import cn.loyisa.shadowban.commands.SubCommand;
import cn.loyisa.shadowban.enums.Messages;
import cn.loyisa.shadowban.enums.Permissions;
import org.bukkit.Bukkit;
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
        return "对这名玩家整活";
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
            Player player = Bukkit.getPlayerExact(args[1]);
            if (player != null) {
                if (!shadowBan.shadowBanList.contains(player.getUniqueId())) {
                    sender.sendMessage(Messages.ADDING_TO_KICK_LIST.getMessage());
                    shadowBan.shadowBanList.add(player.getUniqueId());
                } else {
                    sender.sendMessage(Messages.ADDED_TO_KICK_LIST.getMessage());
                }
            } else {
                sender.sendMessage(Messages.NO_PLAYER.getMessage());
            }
        }
    }
}
