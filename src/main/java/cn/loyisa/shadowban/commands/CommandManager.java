package cn.loyisa.shadowban.commands;

import cn.loyisa.shadowban.ShadowBan;
import cn.loyisa.shadowban.commands.subcommands.BanCommand;
import cn.loyisa.shadowban.commands.subcommands.BanListCommand;
import cn.loyisa.shadowban.commands.subcommands.ReloadCommand;
import cn.loyisa.shadowban.commands.subcommands.UnbanCommand;
import cn.loyisa.shadowban.enums.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * A command manager class that we'll be using in order to make creating and using commands
 * Much easier and cleaner.
 */
public class CommandManager implements TabExecutor {

    private final List<SubCommand> subCommands = new ArrayList<>();

    public CommandManager(ShadowBan shadowBan) {
        this.subCommands.add(new BanCommand(shadowBan));
        this.subCommands.add(new BanListCommand(shadowBan));
        this.subCommands.add(new UnbanCommand(shadowBan));
        this.subCommands.add(new ReloadCommand(shadowBan));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            for (SubCommand subCommand : this.subCommands) {
                if (args[0].equalsIgnoreCase(subCommand.getName())) {
                    if (!subCommand.canConsoleExecute() && sender instanceof ConsoleCommandSender) {
                        sender.sendMessage(Messages.CONSOLE_ONLY.getMessage());
                        return true;
                    }

                    if (!sender.hasPermission(subCommand.getPermission())) {
                        sender.sendMessage(Messages.NO_PERMISSION.getMessage());
                        return true;
                    }

                    subCommand.perform(sender, args);
                    return true;
                }

                if (args[0].equalsIgnoreCase("help")) {
                    helpMessage(sender);
                    return true;
                }
            }
        }

        helpMessage(sender);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> list = new ArrayList<>();
            for (SubCommand subCommand : this.subCommands) {
                // Check permission before send tab list
                if (!sender.hasPermission(subCommand.getPermission())) {
                    continue;
                }
                String name = subCommand.getName();
                list.add(name);
            }
            return list;
        }

        return null;
    }

    private void helpMessage(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(Messages.PREFIX.getMessage() + Messages.AVAILABLE_COMMANDS.getMessage());
        sender.sendMessage("");

        for (SubCommand subCommand : this.subCommands) {
            if (sender.hasPermission(subCommand.getPermission())) {
                sender.sendMessage(ChatColor.GOLD + subCommand.getSyntax() + ChatColor.DARK_GRAY + " - "
                        + ChatColor.GRAY + subCommand.getDescription());
            }
        }

        sender.sendMessage("");
    }
}