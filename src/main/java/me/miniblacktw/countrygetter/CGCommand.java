package me.miniblacktw.countrygetter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CGCommand implements CommandExecutor {

    private final CountryGetter plugin;

    public CGCommand(CountryGetter plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("cg.*")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission");
            return true;
        }

        if (args.length < 2 || !args[0].equalsIgnoreCase("info")) {
            sender.sendMessage(ChatColor.RED + "Usage: /cg info <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Could not find player " + ChatColor.AQUA + target.getDisplayName());
            return true;
        }

        CountryGetter.PlayerInfo info = plugin.getPlayerInfo(target);
        if (info == null) {
            sender.sendMessage(ChatColor.RED + "Please try again later");
            return true;
        }

        sender.sendMessage(ChatColor.DARK_GRAY + "======== " + ChatColor.AQUA + "CountryGetter" + ChatColor.DARK_GRAY + " ========");
        sender.sendMessage(ChatColor.GRAY + "Player : " + ChatColor.AQUA + target.getName());
        sender.sendMessage(ChatColor.GRAY + "Proxy/VPN : " + info.isProxy);
        sender.sendMessage(ChatColor.GRAY + "IP Address : " + ChatColor.AQUA + info.ip);
        sender.sendMessage(ChatColor.DARK_GRAY + "======== " + ChatColor.AQUA + "CountryGetter" + ChatColor.DARK_GRAY + " ========");
        return true;
    }
}