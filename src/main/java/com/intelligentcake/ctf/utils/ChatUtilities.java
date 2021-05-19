package com.intelligentcake.ctf.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatUtilities {

    public static String starter = ChatColor.DARK_GRAY +
            "[" + ChatColor.RED + "CTF" + ChatColor.DARK_GRAY + "]" + ChatColor.WHITE;

    public static void broadcast(String msg) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(starter + msg);
        }
    }
}
