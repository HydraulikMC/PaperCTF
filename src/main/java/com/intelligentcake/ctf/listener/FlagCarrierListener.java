package com.intelligentcake.ctf.listener;

import com.intelligentcake.ctf.CTF;
import com.intelligentcake.ctf.Team;
import com.intelligentcake.ctf.TeamManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.ListIterator;

public class FlagCarrierListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (playerIsCarryingFlag(player)) {

            Location abovePlayer = new Location(player.getWorld(),player.getLocation().getX(), player.getLocation().getY() + 1, player.getLocation().getZ());
            player.setGlowing(true);
//            player.getLocation().getWorld().playEffect(player.getLocation(), Effect.ENDER_SIGNAL, 8);
//            abovePlayer.getWorld().playEffect(abovePlayer, Effect.ENDER_SIGNAL, 8);

            Team playerTeam = TeamManager.getPlayerTeam(player);
            if (playerTeam != null) {
                double distance = player.getLocation().distance(playerTeam.getBannerSpawn());
                if (distance < 1) {
                    Team victimTeam = TeamManager.flagCarriers.get(player);
                    victimTeam.restoreBanner();
                    Bukkit.broadcastMessage(ChatColor.AQUA + "=====================================================");
                    Bukkit.broadcastMessage(ChatColor.GREEN + "Player " + TeamManager.getPlayerColour(player) + player.getName() + ChatColor.GREEN + " captured " +
                            victimTeam.printTeamName() + ChatColor.GREEN + "'s flag and scored a point! Returning flag to base.");
                    Bukkit.broadcastMessage(ChatColor.AQUA + "=====================================================");
                    player.getInventory().remove(victimTeam.getBannerMaterial());
                    player.setGlowing(false);
                    playerTeam.addPoint();
                    // Check win condition
                    if(playerTeam.getScore() == CTF.maxScore) {
                        Bukkit.broadcastMessage(ChatColor.AQUA + "=====================================================");
                        Bukkit.broadcastMessage(playerTeam.getColour() + playerTeam.getName() + " team has won the game!!!");
                        Bukkit.broadcastMessage(ChatColor.AQUA + "=====================================================");
                        player.sendTitle(playerTeam.getColour() + playerTeam.getName() + " team has won the game!!!", playerTeam.getColour() + "GG", 1, 3, 1);
                        CTF.pvpEnabled = false;
                    }
                    TeamManager.flagCarriers.remove(player);
                    victimTeam.getBannerSpawn().getWorld().spawnParticle(Particle.FIREWORKS_SPARK, playerTeam.getBannerSpawn(), 150);
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (playerIsCarryingFlag(event.getEntity())) {	//If the dead player was carrying a flag
            Team victimTeam = TeamManager.flagCarriers.get(event.getEntity());	//Team of the stolen flag
            Material flagMaterial = victimTeam.getBannerMaterial();
            removeFlagFropDrops(event.getDrops(), flagMaterial);
            dropFlag(victimTeam, event.getEntity());
        }
    }

    public void dropFlag(Team victimTeam, @NotNull Player player) {
        Material flagMaterial = victimTeam.getBannerMaterial();
        Location tempFlagLocation = player.getLocation();
        placeTemporaryBanner(player.getLocation(), flagMaterial, victimTeam);
        Bukkit.broadcastMessage(ChatColor.GREEN + "Team " + victimTeam.printTeamName() + ChatColor.GREEN + "'s flag was dropped! Will return to base after 30 seconds if not picked up.");
        TeamManager.flagCarriers.remove(player);	//Remove user from list of carriers

        //Wait 30s, check if flag is still there, if so, restore it to base
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(JavaPlugin.getPlugin(CTF.class), new Runnable() {
            @Override
            public void run() {
                World w = Bukkit.getServer().getWorlds().get(0);
                if (w.getBlockAt(tempFlagLocation).getType().equals(victimTeam.getBannerMaterial())) {
                    victimTeam.restoreBanner();
                    Bukkit.broadcastMessage(ChatColor.AQUA + "=====================================================");
                    Bukkit.broadcastMessage(ChatColor.GREEN + "Team " + victimTeam.printTeamName() + ChatColor.GREEN +
                            "'s flag was restored to base!");
                    Bukkit.broadcastMessage(ChatColor.AQUA + "=====================================================");
                }
            }
        }, 600L);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (Tag.BANNERS.isTagged(event.getItemDrop().getItemStack().getType())) {
            Player player = event.getPlayer();
            Team victimTeam = TeamManager.flagCarriers.get(player);	//Team of the stolen flag
            player.getInventory().remove(player.getItemInHand());
            event.getItemDrop().remove();
            dropFlag(victimTeam, player);
        }
    }

    @EventHandler
    public void place(BlockPlaceEvent event) {
        if (Tag.BANNERS.isTagged(event.getBlock().getType())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot place banners manually!");
        }
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        if(TeamManager.flagCarriers.containsKey(event.getPlayer())) { 	//If disconnected player had the flag
            event.getPlayer().setHealth(0);
            TeamManager.flagCarriers.remove(event.getPlayer());
        }
    }

    @EventHandler
    public void onHeld(PlayerItemHeldEvent event) {
        if (TeamManager.flagCarriers.containsKey(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (TeamManager.flagCarriers.containsKey(event.getWhoClicked())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if (Tag.BANNERS.isTagged(event.getItem().getItemStack().getType())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHandSwap(PlayerSwapHandItemsEvent event) {
        if (Tag.BANNERS.isTagged(event.getMainHandItem().getType()) || Tag.BANNERS.isTagged(event.getOffHandItem().getType())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        for (ItemStack item : event.getPlayer().getInventory().getContents()) {
            if (item != null && Tag.BANNERS.isTagged(item.getType())) {
                event.getPlayer().getInventory().remove(item);
            }
        }
        if (event.isBedSpawn()) {
            event.setRespawnLocation(Bukkit.getServer().getWorlds().get(0).getSpawnLocation());
        }
    }

    public boolean playerIsCarryingFlag(Player player) {
        return TeamManager.flagCarriers.containsKey(player);
    }

    public void removeFlagFropDrops(@NotNull List<ItemStack> drops, Material flagMaterial) {
        drops.removeIf(stack -> stack.getType().equals(flagMaterial));
    }

    public void placeTemporaryBanner(Location location, Material flagMaterial, Team victimTeam) {
        World w = Bukkit.getServer().getWorlds().get(0);
        Block block = w.getBlockAt(location);
        block.setType(flagMaterial);
        victimTeam.addStolenBanner(block);
    }
}
