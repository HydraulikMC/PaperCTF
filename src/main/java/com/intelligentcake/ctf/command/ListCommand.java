package com.intelligentcake.ctf.command;

import com.intelligentcake.ctf.Team;
import com.intelligentcake.ctf.TeamManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ListCommand {

    public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        StringBuilder builder = new StringBuilder();
        builder.append(ChatColor.GREEN);
        builder.append("Current teams: ");
        for (Team team : TeamManager.getTeams()) {
            builder.append(team.printTeamName());
            builder.append(ChatColor.GREEN);
            builder.append(", ");
        }
        sender.sendMessage(builder.toString());
        return true;
    }
}
