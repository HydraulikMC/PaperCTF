package com.intelligentcake.ctf.listener;

import com.intelligentcake.ctf.CTF;
import org.bukkit.event.Listener;

public abstract class CTFListener implements Listener {

    CTF plugin;

    public CTFListener(CTF plugin) {
        this.plugin = plugin;
    }
}
