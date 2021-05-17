package com.intelligentcake.ctf.game;

import com.intelligentcake.ctf.game.arena.ArenaData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FinishCTF {

    public void Finished(String arena, Plugin plugin, boolean abnormal) {
        HashMap<Integer, ArrayList<String>> playerList = ArenaData.playerList.getOrDefault(arena, new HashMap<Integer, ArrayList<String>>());

        //player
        for(String s : playerList.get(1)) {
            Player p = Bukkit.getServer().getPlayer(s);
            assert p != null;
            SetupPlayer(p);
        }
        for(String s : playerList.get(2)) {
            Player p = Bukkit.getServer().getPlayer(s);
            SetupPlayer(p);

        }
        ArenaData.playerList.remove(arena);

        //arena
        ArenaData.spawnPoint.remove(arena);
        ArenaData.arenaStatus.remove(arena);

        String[] fs = ArenaData.flag1Status.get(arena).split(", ");
        if(fs[0].equals("camp")) {
            // TODO: Set flying item
            ArenaData.flagArmor1.remove(arena);
        }else if(fs[0].equalsIgnoreCase("onGround")) {
            ArenaData.flag1Drop.get(arena).remove();
            ArenaData.flag1Drop.remove(arena);
        }
        ArenaData.flag1Status.remove(arena);

        fs = ArenaData.flag2Status.get(arena).split(", ");
        if(fs[0].equals("camp")) {
            // TODO: Set flying item
            ArenaData.flagArmor2.remove(arena);
        }else if(fs[0].equalsIgnoreCase("onGround")) {
            ArenaData.flag2Drop.get(arena).remove();
            ArenaData.flag2Drop.remove(arena);
        }
        ArenaData.flag2Status.remove(arena);

        List<Integer> points = ArenaData.arenaPoints.get(arena);
        if(abnormal) {
            Broadcast("Game over! This game ended "+ ChatColor.DARK_PURPLE+"Abnormally.", ChatColor.AQUA, arena);
        }else {
            if(points.get(0)>points.get(1)) {
                Broadcast("Game over! Winner team is "+ChatColor.RED+"RED Team!", ChatColor.GOLD, arena);
            }else if(points.get(0)<points.get(1)) {
                Broadcast("Game over! Winner team is "+ChatColor.BLUE+"BLUE Team!", ChatColor.GOLD, arena);
            }else {
                Broadcast("Game over! This game was "+ChatColor.LIGHT_PURPLE+"Draw!", ChatColor.GOLD, arena);
            }
        }
        ArenaData.arenaPoints.remove(arena);

        List<Integer> co = plugin.getConfig().getIntegerList(arena+".flag1");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender()
                , "kill @e[type=ArmorStand,x="+co.get(0)+",y="+co.get(1)+",z="+co.get(2)+",r=3]");
        co = plugin.getConfig().getIntegerList(arena+".flag2");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender()
                , "kill @e[type=ArmorStand,x="+co.get(0)+",y="+co.get(1)+",z="+co.get(2)+",r=3]");
    }

    private void SetupPlayer(Player p) {
        if(p.isOnline()) {
            p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            p.getInventory().clear();
            p.getInventory().setHelmet(new ItemStack(Material.AIR));
            p.getInventory().setChestplate(new ItemStack(Material.AIR));
            p.getInventory().setLeggings(new ItemStack(Material.AIR));
            p.getInventory().setBoots(new ItemStack(Material.AIR));
            ArenaData.playerStatus.remove(p.getName());
            p.teleport(p.getWorld().getSpawnLocation());
        }
    }

    private void Broadcast(String message, ChatColor cc, String arena) {
        Bukkit.getServer().broadcastMessage(ChatColor.AQUA + "[S-CTF]" +
                ChatColor.WHITE + "(" + arena + ") " + cc + message);
    }
}
