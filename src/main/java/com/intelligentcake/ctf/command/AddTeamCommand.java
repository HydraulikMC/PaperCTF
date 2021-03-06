package com.intelligentcake.ctf.command;

import com.intelligentcake.ctf.TeamManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class AddTeamCommand {

    public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //Check if missing teamName or teamColor
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Invalid arguments: /team create <teamName> <teamColor>");
            return true;
        }

        try {
            ChatColor.valueOf(args[2].toUpperCase());
            String teamName = args[1];
            String teamColor = args[2].toUpperCase();
            if (TeamManager.addTeam(teamName, teamColor)) {
                sender.sendMessage(ChatColor.GREEN + "Successfully added team " + ChatColor.valueOf(teamColor) + ChatColor.BOLD + teamName +
                        ChatColor.RESET + ChatColor.GREEN + " to list of teams.");
            } else {
                sender.sendMessage(ChatColor.RED + "Error: Team name or colour already in use.");
            }

        } catch (IllegalArgumentException e) {
            sender.sendMessage(ChatColor.RED + "Invalid colour. Please choose from: " + String.join(", ", TeamManager.availableColours));
        }
        return true;

    }
}
