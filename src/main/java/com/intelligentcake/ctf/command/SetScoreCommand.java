package com.intelligentcake.ctf.command;

import com.intelligentcake.ctf.TeamManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SetScoreCommand {


    public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Invalid command usage: /team setScore <team> <score>");
            return true;
        }

        String teamName = args[1];

        if (!TeamManager.containsTeam(teamName)) {
            sender.sendMessage(ChatColor.RED + "Invalid team name! Choose from: "
                    + String.join(", ", TeamManager.getTeamNames()));
            return true;
        }

        //Checking if the new score entered is a numeric score
        try {
            Integer.parseInt(args[2]);
        } catch (NumberFormatException nfe) {
            sender.sendMessage(ChatColor.RED + "New score must be a numeric value.");
            return true;
        }

        TeamManager.getTeam(teamName).setScore(Integer.parseInt(args[2]));



        return true;
    }
}
