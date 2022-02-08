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
            OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
            sender.sendMessage(Messages.REMOVEING_FROM_BAN_LIST.getMessage());
            shadowBan.getBanManager().unban(player);
        }
    }
}
