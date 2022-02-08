package cn.loyisa.shadowban.commands.subcommands;

import cn.loyisa.shadowban.ShadowBan;
import cn.loyisa.shadowban.commands.SubCommand;
import cn.loyisa.shadowban.enums.Messages;
import cn.loyisa.shadowban.enums.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.UUID;
import java.util.function.BiConsumer;

public class BanListCommand extends SubCommand {


    private final ShadowBan shadowBan;

    public BanListCommand(ShadowBan plugin) {
        this.shadowBan = plugin;
    }

    @Override
    protected String getName() {
        return "banlist";
    }

    @Override
    protected String getDescription() {
        return "查看当前房间下的 BanList";
    }

    @Override
    protected String getSyntax() {
        return "/shadowban banlist";
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
        if (args.length == 1) {
            sender.sendMessage("在当前房间中，以下玩家正等待处决: ");
            shadowBan.getBanManager().getShadowBanMap().forEach(
                    (uuid, aLong) -> sender.sendMessage(Bukkit.getOfflinePlayer(uuid).getName() + " - 剩余时间: " + ((aLong - System.currentTimeMillis()) / 1000) + " 秒")
            );
        }
    }
}
