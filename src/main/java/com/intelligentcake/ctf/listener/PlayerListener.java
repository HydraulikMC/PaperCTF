package com.intelligentcake.ctf.listener;

import com.intelligentcake.ctf.CTF;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

public class PlayerListener implements Listener {

    private final CTF plugin;

    public PlayerListener(CTF plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void PlayerDeathEvent(PlayerDeathEvent event) {

    }

    @EventHandler
    public void PlayerPickupItemEvent(PlayerPickupItemEvent event) {

    }

    @EventHandler
    public void PlayerDropItemEvent(PlayerDropItemEvent event) {

    }

    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent event) {

    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent event) {

    }

    @EventHandler
    public void PlayerRespawnEvent(PlayerRespawnEvent event) {

    }

    @EventHandler
    public void BlockPlaceEvent(BlockPlaceEvent event) {

    }

    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent event) {

    }
}
