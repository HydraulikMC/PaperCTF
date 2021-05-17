package com.intelligentcake.ctf.game;

import com.intelligentcake.ctf.game.arena.ArenaData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

public class ScoreboardControl {

    // <arenaName, {team1, team2}>
    private static HashMap<String, Scoreboard> scoreboard = new HashMap<>();

    public void UpdateScoreboardData(String arena, Scoreboard sb) {
        scoreboard.put(arena, sb);
    }

    public void WaitScoreboardChange(String arena) {
        HashMap<Integer, ArrayList<String>> hm = ArenaData.playerList.getOrDefault(arena, new HashMap<>());
        Scoreboard sb = Initialise(arena, hm);
        Objective objective = sb.getObjective(arena);

        assert objective != null;
        objective.getScore(ChatColor.GREEN + "  == CTF in " + arena + " == ").setScore(200);
        objective.getScore(" ").setScore(199);
        objective.getScore(ChatColor.RED + " -- RED Team Member -- ").setScore(198);
        objective.getScore(" ").setScore(99);
        objective.getScore(ChatColor.BLUE + " -- BLUE Team Member -- ").setScore(98);

        // RED -> 101~197
        // BLUE -> 1~97
        int i = 101;
        if (hm.get(1) != null) {
            for (String s : hm.get(1)) {
                objective.getScore(" " + ChatColor.RED + s).setScore(i);
                i++;
            }
        }

        i = 0;
        if(hm.get(2)!=null) {
            for(String s : hm.get(2)) {
                objective.getScore(" "+ChatColor.BLUE+s).setScore(i);
                i++;
            }
        }

        ApplyScoreboard(arena, sb, hm);
    }

    public void PlayScoreBoardChange(String arena) {
        HashMap<Integer, ArrayList<String>> hm = ArenaData.playerList.getOrDefault(arena, new HashMap<Integer, ArrayList<String>>());
        Scoreboard sb = Initialise(arena, hm);
        Objective objective = sb.getObjective(arena);

        List<Integer> points = ArenaData.arenaPoints.getOrDefault(arena, Arrays.asList(0, 0));
        int[] i = {0, 0};
        if(hm.get(1)!=null) { i[0] = hm.get(1).size(); }
        if(hm.get(2)!=null) { i[1] = hm.get(2).size(); }

        // RED_101~197,  BLUE_1~97
        int cn = ArenaData.arenaStatus.getOrDefault(arena, 0);
        assert objective != null;
        objective.getScore(ChatColor.GREEN + "  == CTF in " + arena + " == ").setScore(300);
        objective.getScore(" ").setScore(299);
        objective.getScore(ChatColor.AQUA + "remaining time: "+(cn-100)*10).setScore(300);
        objective.getScore(" ").setScore(199);
        objective.getScore(ChatColor.RED + " -- RED Team Status -- ").setScore(198);
        objective.getScore(ChatColor.RED + " Score: " + ChatColor.WHITE + points.get(0)).setScore(197);
        objective.getScore(ChatColor.RED + " Join Players: " + ChatColor.WHITE + i[0]).setScore(196);
        objective.getScore(" ").setScore(99);
        objective.getScore(ChatColor.BLUE + " -- BLUE Team Status -- ").setScore(98);
        objective.getScore(ChatColor.BLUE + " Score: " + ChatColor.WHITE + points.get(1)).setScore(97);
        objective.getScore(ChatColor.BLUE + " Join Players: " + ChatColor.WHITE + i[1]).setScore(96);

        ApplyScoreboard(arena, sb, hm);
    }

    private Scoreboard Initialise(String arena, HashMap<Integer, ArrayList<String>> hm) {
        Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
        sb.registerNewTeam("team1").setAllowFriendlyFire(false);
        sb.registerNewTeam("team2").setAllowFriendlyFire(false);
        Objects.requireNonNull(sb.getTeam("team1")).setPrefix(ChatColor.RED + "");
        Objects.requireNonNull(sb.getTeam("team2")).setPrefix(ChatColor.BLUE + "");

        Objective objective = sb.registerNewObjective(arena, "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        if(hm.get(1)!=null) {
            for(String s : hm.get(1)) {
                Player player = Bukkit.getServer().getPlayer(s);
                assert player != null;
                player.getScoreboard().resetScores(ChatColor.RED+player.getName());
                player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                Objects.requireNonNull(sb.getTeam("team1")).addEntry(player.getName());
            }
        }
        if(hm.get(2)!=null) {
            for(String s : hm.get(2)) {
                Player player = Bukkit.getServer().getPlayer(s);
                assert player != null;
                player.getScoreboard().resetScores(ChatColor.BLUE+player.getName());
                player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                Objects.requireNonNull(sb.getTeam("team2")).addEntry(player.getName());
            }
        }

        return sb;
    }

    private void ApplyScoreboard(String arena, Scoreboard sb, HashMap<Integer, ArrayList<String>> hm) {
        if(hm.get(1)!=null) {
            for(String s : hm.get(1)) {
                Player player = Bukkit.getServer().getPlayer(s);
                assert player != null;
                player.setScoreboard(sb);
            }
        }
        if(hm.get(2)!=null) {
            for(String s : hm.get(2)) {
                Player player = Bukkit.getServer().getPlayer(s);
                assert player != null;
                player.setScoreboard(sb);
            }
        }
        if(ArenaData.spectator.containsValue(arena)) {
            for(Player p : Bukkit.getServer().getOnlinePlayers()) {
                if(ArenaData.spectator.getOrDefault(p.getName(), "none").equalsIgnoreCase(arena)) {
                    p.setScoreboard(sb);
                }
            }
        }
        scoreboard.put(arena, sb);
    }
}
