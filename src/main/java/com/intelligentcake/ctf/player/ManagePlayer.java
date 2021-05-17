package com.intelligentcake.ctf.player;

import com.intelligentcake.ctf.CTF;
import com.intelligentcake.ctf.game.arena.ArenaData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class ManagePlayer<EntityPlayer> {

    public void Join(Player p, String arena, CTF plugin) {
        int cn = ArenaData.arenaStatus.getOrDefault(arena, 0);

        if(!plugin.getConfig().getStringList("List").contains(arena)) {
            SendMessage(p, "This arena does not exist.", ChatColor.RED);
        }else if(ArenaData.playerStatus.containsKey(p.getName())){
            SendMessage(p, "You have already join another arena.", ChatColor.RED);
        }else if(!plugin.getConfig().getBoolean(arena+".enable")) {
            SendMessage(p, "This arena is disabled.", ChatColor.RED);
        }else {
            // Player
            SendMessage(p, "Joining a CTF game in arena " + arena, ChatColor.RED);
            p.getInventory().clear();
            p.getInventory().setHelmet(new ItemStack(Material.AIR));
            p.getInventory().setChestplate(new ItemStack(Material.AIR));
            p.getInventory().setLeggings(new ItemStack(Material.AIR));
            p.getInventory().setBoots(new ItemStack(Material.AIR));
            ArenaData.playerStatus.put(p.getName(), arena);
            HashMap<Integer, ArrayList<String>> hm = ArenaData.playerList.getOrDefault(arena, new HashMap<>());
            ArrayList<String> team1List = hm.getOrDefault(1, new ArrayList<>());
            ArrayList<String> team2List = hm.getOrDefault(2, new ArrayList<>());
            int team1Length = team1List.size();
            int team2Length = team2List.size();
            int team;
            if (team2Length >= team1Length) {
                // Team 1
                team1List.add(p.getName());
                hm.put(1, team1List);
                ArenaData.playerList.put(arena, hm);
                SendMessage(p, "You are" + ChatColor.RED + " Red Team", ChatColor.GREEN);
                p.setPlayerListName(ChatColor.RED + p.getName());
                team = 1;
            } else {
                // Team 2
                team2List.add(p.getName());
                hm.put(2, team2List);
                ArenaData.playerList.put(arena, hm); //necessary
                SendMessage(p, "You are" + ChatColor.BLUE + " BLUE Team", ChatColor.GREEN);
                p.setPlayerListName(ChatColor.BLUE+p.getName());
                team = 2;
            }
            if (cn > 99) {
                // Already playing
                // TODO: Add scoreboard ctrl here
            }
        }
    }

    private void SendMessage(Player p, String message, ChatColor cc) {
        p.sendMessage(ChatColor.AQUA + "[CTF] " + cc + message);
    }
}
