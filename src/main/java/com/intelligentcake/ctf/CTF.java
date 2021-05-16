package com.intelligentcake.ctf;

import org.bukkit.plugin.java.JavaPlugin;

public final class CTF extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.saveDefaultConfig();

        // Commands

        // Listeners
        new PlayerListeners(this);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
