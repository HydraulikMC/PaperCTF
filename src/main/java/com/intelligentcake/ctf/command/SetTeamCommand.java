package com.intelligentcake.ctf.command;

import com.intelligentcake.ctf.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetTeamCommand {

    public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Invalid arguments: /team add <player> <team>");

        } else {

            String playerName = args[1];
            String team = args[2].toUpperCase();
            Player player = Bukkit.getServer().getPlayerExact(playerName);

            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Invalid user.");
            } else if (!TeamManager.containsTeam(team)) {
                sender.sendMessage(ChatColor.RED + "Invalid team name. Please choose from: " + String.join(", ", TeamManager.getTeamNames()));
            } else {
                TeamManager.purgePlayer(player); // Remove player from all other teams
                TeamManager.getTeam(team.toUpperCase()).addPlayer(player);
                sender.sendMessage(ChatColor.GREEN + "Successfully added " + player.getDisplayName() + ChatColor.GREEN
                        + " to team " + TeamManager.getTeam(team.toUpperCase()).printTeamName() + ChatColor.GREEN + ".");
                player.setDisplayName(TeamManager.getPlayerTeam(player).getColour().toString() + player.getDisplayName());
            }
        }

        return true;
    }
}
