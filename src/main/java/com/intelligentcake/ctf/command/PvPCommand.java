package com.intelligentcake.ctf.command;

import com.intelligentcake.ctf.CTF;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class PvPCommand {

    public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Invalid arguments: /team pvp true/false");
            return true;
        }

        if (args[1].equalsIgnoreCase("true")) {
            CTF.pvpEnabled = true;
            sender.sendMessage(ChatColor.GREEN + "Enabled PvP.");
        } else if (args[1].equalsIgnoreCase("false")) {
            CTF.pvpEnabled = false;
            sender.sendMessage(ChatColor.GREEN + "Disabled PvP.");
        } else {
            sender.sendMessage(ChatColor.RED + "Invalid arguments: /team pvp true/false");
        }

        return true;
    }
}
