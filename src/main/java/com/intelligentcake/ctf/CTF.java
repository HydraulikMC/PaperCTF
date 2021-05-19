package com.intelligentcake.ctf;

import com.intelligentcake.ctf.game.Game;
import com.intelligentcake.ctf.listener.player.PlayerJoinListener;
import com.intelligentcake.ctf.threads.StartCountdown;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CTF extends JavaPlugin {

    // Temporary until I can sort out having multiple games simultaneously
    public static Game game;

    @Override
    public void onEnable() {
        getServer().getLogger().info("CTF plugin enabled!");
        game = new Game(this);
        new Thread(new StartCountdown()).start();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        getServer().getLogger().info("CTF plugin disabled!");
        super.onDisable();
    }

    public void registerListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerJoinListener(this), this);
    }
}
