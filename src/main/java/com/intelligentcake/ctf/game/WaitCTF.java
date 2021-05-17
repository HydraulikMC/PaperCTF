package com.intelligentcake.ctf.game;

import com.intelligentcake.ctf.game.arena.ArenaData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class WaitCTF extends BukkitRunnable {

    private String arena;
    private Plugin plugin;
    private World world;

    public WaitCTF(String arena, Plugin plugin, World world) {
        this.arena = arena;
        this.plugin = plugin;
        this.world = world;
        int c = ArenaData.arenaStatus.getOrDefault(arena, 0);
        if (!(c < 10 && c != 0)) {
            throw new IllegalArgumentException(ChatColor.AQUA + "[CTF] " +
                    ChatColor.DARK_RED + "Fatal error when when starting the arena wait counter in WaitCTF.java");
        }
    }

    @Override
    public void run() {
        int c = ArenaData.arenaStatus.getOrDefault(arena, 0)-1;
        if (c == -2) {
            ArenaData.arenaStatus.remove(arena);
            this.cancel();
        } else if (c == 0) {
            ArenaData.arenaStatus.put(arena, 130);
            new PlayCTF(arena, plugin, world).runTaskTimer(plugin, 100, 200); // debug
        } else if (c > 100) {
            this.cancel();
        } else {
            ArenaData.arenaStatus.put(arena, c);
            Broadcast(arena + " will start in " + ChatColor.GOLD + (c*10) + ChatColor.GREEN + " Seconds", ChatColor.GREEN, arena);
        }
    }

    private void Broadcast(String message, ChatColor cc, String arena) {
        Bukkit.getServer().broadcastMessage(ChatColor.AQUA + "[CTF]" +
                ChatColor.WHITE + "(" + arena + ") " + cc + message);
    }
}
