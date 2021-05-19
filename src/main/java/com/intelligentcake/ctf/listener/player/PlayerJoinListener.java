package com.intelligentcake.ctf.listener.player;

import com.intelligentcake.ctf.CTF;
import com.intelligentcake.ctf.listener.CTFListener;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener extends CTFListener {

    public PlayerJoinListener(CTF plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        CTF.game.setCanStart(Bukkit.getOnlinePlayers().size() >= 4);
    }
}
