package com.intelligentcake.ctf.command;

import com.intelligentcake.ctf.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemovePlayerCommand {

    public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Invalid arguments: /team remove <player> <team>");
        } else {
            String playerName = args[1];
            String teamName = args[2].toUpperCase();
            Player player = Bukkit.getServer().getPlayerExact(playerName);
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Invalid user");
            } else if (!TeamManager.containsTeam(teamName)) {
                sender.sendMessage(ChatColor.RED + "Invalid team name. Please choose from: "
                        + String.join(", ", TeamManager.getTeamNames()));
            } else {
                TeamManager.getTeam(teamName).removePlayer(player);
                sender.sendMessage(ChatColor.GREEN + "Removed player from the team.");
            }
        }

        return true;
    }
}
