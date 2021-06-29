package com.intelligentcake.ctf.command;

import com.intelligentcake.ctf.CTF;
import com.intelligentcake.ctf.Team;
import com.intelligentcake.ctf.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;

public class StopCommand {

    public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // team stop

        if (args.length == 1) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                TeamManager.purgePlayer(player);
                player.teleport(player.getWorld().getSpawnLocation());
                player.setGameMode(GameMode.SPECTATOR);
            }

            for (Team team : TeamManager.getTeams())
            {
                team.setScore(0);
            }
        }
        return true;
    }
}
