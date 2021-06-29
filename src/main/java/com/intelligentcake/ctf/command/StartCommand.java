package com.intelligentcake.ctf.command;

import com.intelligentcake.ctf.CTF;
import com.intelligentcake.ctf.Team;
import com.intelligentcake.ctf.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

public class StartCommand {

    public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            int timeInSeconds = Integer.parseInt(args[1]);
            addTimer(timeInSeconds);
            sender.sendMessage(ChatColor.GREEN + "Countdown started for " + ChatColor.AQUA + timeInSeconds + ChatColor.GREEN + " seconds from now. PvP will be automatically enabled.");
            CTF.maxScore = Integer.parseInt(args[2]);
        } catch (IndexOutOfBoundsException e) {
            sender.sendMessage(ChatColor.RED + "Invalid use of command. /team start <countdownTime> <maxScore>");
        }
        return true;
    }

    public static void addTimer(int timeInSeconds) {
        Scoreboard board = CTF.board;
        board.getObjective("Scoreboard").getScore(" ").setScore(1);
        new BukkitRunnable() {
            int count = timeInSeconds;
            public void run() {
                count--;
                if (count > 60) {
                    board.getObjective("Scoreboard").getScore(ChatColor.BOLD + "Timer (minutes)").setScore((int) Math.ceil(count / 60f));
                } else if (count == 60) {
                    board.resetScores(ChatColor.BOLD + "Timer (minutes)");
                } else if (count <= 10) {
                    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 3, 3);
                    }
                    board.getObjective("Scoreboard").getScore(ChatColor.BOLD + "Timer (seconds)").setScore(count);
                } else {
                    board.getObjective("Scoreboard").getScore(ChatColor.BOLD + "Timer (seconds)").setScore(count);
                }
                // Countdown ends
                if (count == 0) {
                    board.resetScores(ChatColor.BOLD + "Timer (seconds)");
                    board.resetScores(" ");
                    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                        player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 10f, 0.5f);
                    }
                    CTF.pvpEnabled = true;
                    Bukkit.broadcastMessage(ChatColor.DARK_RED + "=====================================================");
                    Bukkit.broadcastMessage(ChatColor.GRAY + "LET THE GAMES BEGIN! PVP IS NOW ENABLED. GOOD LUCK!");
                    Bukkit.broadcastMessage(ChatColor.DARK_RED + "=====================================================");

                    // Teleport players to respective spawns
                    for (Team team : TeamManager.getTeams()) {
                        for (Player player : team.getPlayers()) {
                            player.teleport(team.getBannerSpawn());
                            player.setGameMode(GameMode.ADVENTURE);
                        }
                    }
                    this.cancel();
                }
            }
        }.runTaskTimer(CTF.getPlugin(CTF.class), 0L, 20L);
    }
}
