package com.intelligentcake.ctf.command;

import com.intelligentcake.ctf.Team;
import com.intelligentcake.ctf.TeamManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetFlagCommand {

    public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // ========================== ERROR CHECKING ===============================
        // Command format: /team getFlag
        if (args.length == 1) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                // Check if player has a team
                if (TeamManager.getPlayerTeam(player) == null) {
                    sender.sendMessage("You do not belong to a team!");
                    return true;
                }
            } else {
                // Check if sender is console, console cannot belong to a team therefore they should enter a team name
                sender.sendMessage("You must enter a team name from console!");
                return true;
            }
        } else if (args.length == 2 && !TeamManager.containsTeam(args[1])) {
            sender.sendMessage(ChatColor.RED + "Invalid team name! Choose from: "
                    + String.join(", ", TeamManager.getTeamNames()));
            return true;
        }
        //=========================================================================


        if (sender instanceof Player) {
            Player player = (Player) sender;
            Team team;
            if (args.length <= 1) {
                team = TeamManager.getPlayerTeam(player);
            } else {
                String teamName = args[1];
                team = TeamManager.getTeam(teamName);
                if (team.hasBanner()) {
                    sender.sendMessage(ChatColor.RED + "Team does not have a banner placed.");
                    return true;
                }
            }
            player.sendMessage(team.printTeamName() + ChatColor.GREEN +
                    " team's flag is located at: " + team.getBannerCoordinates());
        }
        return true;
    }
}
