package com.intelligentcake.ctf.listener.player;

import com.intelligentcake.ctf.CTF;
import com.intelligentcake.ctf.game.GameState;
import com.intelligentcake.ctf.listener.CTFListener;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener extends CTFListener {

    public PlayerQuitListener(CTF plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if(CTF.game.getGameState().isState(GameState.IN_LOBBY)) {
            CTF.game.setCanStart(Bukkit.getOnlinePlayers().size() - 1>= 4);
        }
    }
}
