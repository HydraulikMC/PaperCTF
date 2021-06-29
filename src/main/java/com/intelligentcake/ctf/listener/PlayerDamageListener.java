package com.intelligentcake.ctf.listener;

import com.intelligentcake.ctf.CTF;
import com.intelligentcake.ctf.Team;
import com.intelligentcake.ctf.TeamManager;
import com.intelligentcake.ctf.command.HomeCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

public class PlayerDamageListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        //Check for team-hitting
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {    //If the attacker and victim are players

            Player attacker = (Player) event.getDamager();
            Player victim = (Player) event.getEntity();

            if (TeamManager.getPlayerTeam(attacker).equals(TeamManager.getPlayerTeam(victim))) {
                attacker.sendMessage(ChatColor.RED + "You cannot attack your own team members!");
                event.setCancelled(true);
                return;
            }

        }

        //Check if PVP is enabled
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player && !CTF.pvpEnabled) {
            event.setCancelled(true);
            event.getDamager().sendMessage(ChatColor.RED + "PvP is currently disabled!");
            return;
        }

        //Check if PvP is inside spawn
        if (event.getEntity() instanceof Player && event.getEntity().getLocation().distance(event.getEntity().getWorld().getSpawnLocation()) < 5) {
            event.getDamager().sendMessage(ChatColor.RED + "PvP is disabled in spawn!");
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        HomeCommand.cooldown.add(player);
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(JavaPlugin.getPlugin(CTF.class), () -> HomeCommand.cooldown.remove(player), 2400);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        player.setGameMode(GameMode.SPECTATOR);
        BukkitScheduler scheduler = CTF.getPlugin(CTF.class).getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(CTF.getPlugin(CTF.class), new Runnable() {
            @Override
            public void run() {
                if (TeamManager.getPlayerTeam(player) == null) {
                    return;
                }
                player.teleport(TeamManager.getPlayerTeam(player).getBannerSpawn());
                player.setGameMode(GameMode.ADVENTURE);
            }
        }, 200L);
    }
}
