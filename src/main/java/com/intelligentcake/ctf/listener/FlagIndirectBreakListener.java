package com.intelligentcake.ctf.listener;

import org.bukkit.Bukkit;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;


public class FlagIndirectBreakListener implements Listener {

    @EventHandler
    public void onPhysics(BlockPhysicsEvent event) {

        Block eventBlock = event.getBlock();
        if (Tag.BANNERS.isTagged(eventBlock.getType()) || Bukkit.getWorld(eventBlock.getWorld().getName()).getBlockAt(eventBlock.getLocation().add(0, 1, 0)).getType().toString().contains("BANNER")) {	//If banner is destroyed
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void entityChange(EntityChangeBlockEvent event) {

        Block eventBlock = event.getBlock();

        if (Tag.BANNERS.isTagged(eventBlock.getType()) || Bukkit.getWorld(eventBlock.getWorld().getName()).getBlockAt(eventBlock.getLocation().add(0, 1, 0)).getType().toString().contains("BANNER")) {	//If banner is destroyed
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void blockBurn(BlockBurnEvent event) {
        Block eventBlock = event.getBlock();

        if (Tag.BANNERS.isTagged(eventBlock.getType()) || Bukkit.getWorld(eventBlock.getWorld().getName()).getBlockAt(eventBlock.getLocation().add(0, 1, 0)).getType().toString().contains("BANNER")) {	//If banner is destroyed
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void blockExplode(BlockExplodeEvent event) {
        event.blockList().removeIf(b -> Tag.BANNERS.isTagged(b.getType()) || Bukkit.getWorld(b.getWorld().getName()).getBlockAt(b.getLocation().add(0, 1, 0)).getType().toString().contains("BANNER"));
    }

    @EventHandler
    public void entityExplode(EntityExplodeEvent e) {
        e.blockList().removeIf(b -> Tag.BANNERS.isTagged(b.getType()) || Bukkit.getWorld(b.getWorld().getName()).getBlockAt(b.getLocation().add(0, 1, 0)).getType().toString().contains("BANNER"));
    }

    @EventHandler
    public void blockUnderFlagBreaks(BlockBreakEvent e) {
        if (Bukkit.getWorld(e.getBlock().getWorld().getName()).getBlockAt(e.getBlock().getLocation().add(0, 1, 0)).getType().toString().contains("BANNER")) e.setCancelled(true);
    }
}
