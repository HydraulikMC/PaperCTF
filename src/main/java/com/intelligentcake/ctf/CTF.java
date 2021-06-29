package com.intelligentcake.ctf;

import com.intelligentcake.ctf.command.HomeCommand;
import com.intelligentcake.ctf.command.TeamsCommandManager;
import com.intelligentcake.ctf.listener.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class CTF extends JavaPlugin {

    public static Scoreboard board;
    public static Scoreboard timerBoard;
    public static HashMap<Score, Integer> scores;
    public static int maxScore;
    public static boolean pvpEnabled = false;

    // Listener references
    FlagBreakListener flagBreakListener = new FlagBreakListener();
    PlayerJoinListener playerJoinListener = new PlayerJoinListener();
    FlagIndirectBreakListener flagIndirectBreakListener = new FlagIndirectBreakListener();
    PlayerDamageListener playerDamageListener = new PlayerDamageListener();
    FlagCarrierListener flagCarrierListener = new FlagCarrierListener();
    HomeCommand homeCommand = new HomeCommand();

    // Fired when first enabled
    @Override
    public void onEnable() {

        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        //COMMAND AND LISTENERS
        this.getCommand("ctfteam").setExecutor(new TeamsCommandManager());
        getServer().getPluginManager().registerEvents(new FlagBreakListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new FlagIndirectBreakListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDamageListener(), this);
        getServer().getPluginManager().registerEvents(new FlagCarrierListener(), this);
        getServer().getPluginManager().registerEvents(new HomeCommand(), this);

        CTF.board = Bukkit.getScoreboardManager().getMainScoreboard();

        initialiseGame();
    }

    public void initialiseGame()
    {
        loadColors();

        wipeScoreboard();	//Scoreboards save through bukkit, need to wipe and re-add them on reload/start so they're in sync with plugin
        constructScoreboard();

        disableBannerCrafting();
        initializeCompasses();
    }

    // Fired when disabled
    @Override
    public void onDisable() {
    }

    public void wipeScoreboard() {

        //Remove teams from scoreboard
        for (org.bukkit.scoreboard.Team team : board.getTeams()) {
            team.unregister();
        }

        for (String entry : board.getEntries()) {
            Player player = Bukkit.getServer().getPlayerExact(entry);
            if (player != null) {
                player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            }
        }

        //Remove all objectives (objectives are simply each team and their score)
        for (Objective objective : board.getObjectives()) {
            objective.unregister();
        }
    }

    public void constructScoreboard() {
        //Constructing side scoreboard
        Objective objective = board.registerNewObjective("Scoreboard", "Scoreboard");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("Team Scores");

        //Assign scoreboard to every player in server
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            player.setScoreboard(board);
        }
    }

    public void disableBannerCrafting() {
        Iterator<Recipe> it = getServer().recipeIterator();
        Recipe recipe;
        while(it.hasNext())
        {
            recipe = it.next();
            if (recipe != null && Tag.BANNERS.isTagged(recipe.getResult().getType()))
            {
                it.remove();
            }
        }
    }

    public void loadColors() {
        TeamManager.availableColours.add("RED");
        TeamManager.availableColours.add("AQUA");
        TeamManager.availableColours.add("GOLD");
        TeamManager.availableColours.add("GRAY");
        TeamManager.availableColours.add("GREEN");
        TeamManager.availableColours.add("YELLOW");
        TeamManager.availableColours.add("BLUE");
        TeamManager.availableColours.add("LIGHT_PURPLE");
        TeamManager.availableColours.add("DARK_PURPLE");
        TeamManager.availableColours.add("WHITE");
    }

    public void initializeCompasses() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    double minDistance = 999999;
                    Location closestFlag = null;
                    for (Team team : TeamManager.getTeams()) {
                        if (TeamManager.getPlayerTeam(player) != null && !(TeamManager.getPlayerTeam(player).equals(team))) {
                            double distanceFromEnemyTeam = player.getLocation().distance(team.getBannerSpawn());
                            if (distanceFromEnemyTeam < minDistance) {
                                minDistance = distanceFromEnemyTeam;
                                closestFlag = team.getBannerSpawn();
                            }
                        }
                    }
                    if (closestFlag != null) {
                        player.setCompassTarget(closestFlag);
                    }
                }

            }
        }, 0L, 250L);
    }
}
