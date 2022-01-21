package cn.loyisa.shadowban.commands.subcommands;

import cn.loyisa.shadowban.ShadowBan;
import cn.loyisa.shadowban.commands.SubCommand;
import cn.loyisa.shadowban.enums.Messages;
import cn.loyisa.shadowban.enums.Permissions;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends SubCommand {

    private final ShadowBan shadowBan;

    public ReloadCommand(ShadowBan plugin) {
        this.shadowBan = plugin;
    }

    @Override
    protected String getName() {
        return "reload";
    }

    @Override
    protected String getDescription() {
        return "重载插件配置";
    }

    @Override
    protected String getSyntax() {
        return "/shadowban reload";
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
            shadowBan.getConfigManager().reload();
            sender.sendMessage(Messages.RELOAD_PLUGIN.getMessage());
        }
    }
}
