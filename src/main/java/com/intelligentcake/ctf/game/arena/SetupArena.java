package com.intelligentcake.ctf.game.arena;

import com.intelligentcake.ctf.CTF;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class SetupArena {

    public static HashMap<String, Integer> creating = new HashMap<>();
    public static HashMap<String, String> createArena = new HashMap<>();

    public static int getCreateControl(String playerName) {
        return creating.getOrDefault(playerName, 0);
    }

    public void CreateArena(Player p, String arenaName, CTF plugin) {
        String name = p.getName();
        int cn = creating.getOrDefault(name, 0);

        // Add item
        if (cn == 0) {
            p.getInventory().addItem(new ItemStack(Material.BLAZE_ROD));
            createArena.put(name, arenaName);
            creating.put(name, 1);
            SendMessage(p, "Left Click to set the first corner of the arena", ChatColor.GREEN);
        }

        // SetInv1
        if (cn == 8) {
            SetInv(p.getInventory(), "Inv1", p.getName(), plugin);
            creating.put(p.getName(), 9);
            SendMessage(p, "Type command for team2, /ctf admin setInv", ChatColor.GREEN);
        }

        // SetInv2
        if (cn == 9) {
            SetInv(p.getInventory(), "Inv2", p.getName(), plugin);
            plugin.getConfig().set(createArena.get(p.getName()) + ".enable", false);
            List<String> lst = plugin.getConfig().getStringList("List");
            lst.add(createArena.get(p.getName()));
            plugin.getConfig().set("List", lst);
            creating.put(p.getName(), 10);

            //finish
            SendMessage(p, "Successfully created Arena!", ChatColor.GREEN);
            plugin.saveConfig();
            plugin.reloadConfig();
            creating.remove(p.getName());
            createArena.remove(p.getName());
        }
    }

    //1
    public void SetFirstPoint(Location l, CTF plugin, String playerName) {
        List<Integer> lst = new ArrayList<>(Arrays.asList(l.getBlockX(), l.getBlockY(), l.getBlockZ()));
        plugin.getConfig().set(createArena.get(playerName) + ".firstpoint", lst);
        creating.put(playerName, 2);
        SendMessage(Objects.requireNonNull(plugin.getServer().getPlayer(playerName))
                , "Left Click to set the second corner of the arena", ChatColor.GREEN);
    }

    //2
    public void SetSecondPoint(Location l, CTF plugin, String playerName) {
        List<Integer> lst = new ArrayList<>(Arrays.asList(l.getBlockX(), l.getBlockY(), l.getBlockZ()));
        plugin.getConfig().set(createArena.get(playerName) + ".secondpoint", lst);
        creating.put(playerName, 3);
        SendMessage(Objects.requireNonNull(plugin.getServer().getPlayer(playerName))
                , "Left Click to set the location of team1's flag", ChatColor.GREEN);
    }

    //3
    public void SetFlag1(Location l, CTF plugin, String playerName) {
        List<Integer> lst = new ArrayList<>(Arrays.asList(l.getBlockX(), l.getBlockY(), l.getBlockZ()));
        plugin.getConfig().set(createArena.get(playerName) + ".flag1", lst);
        creating.put(playerName, 4);
        SendMessage(Objects.requireNonNull(plugin.getServer().getPlayer(playerName))
                , "Left Click to set the location of team2's flag", ChatColor.GREEN);
    }

    //4
    public void SetFlag2(Location l, CTF plugin, String playerName) {
        List<Integer> lst = new ArrayList<>(Arrays.asList(l.getBlockX(), l.getBlockY(), l.getBlockZ()));
        plugin.getConfig().set(createArena.get(playerName) + ".flag2", lst);
        creating.put(playerName, 5);
        SendMessage(Objects.requireNonNull(plugin.getServer().getPlayer(playerName))
                , "Left Click to set the location of team1's spawn", ChatColor.GREEN);
    }

    //5
    public void SetSpawn1(Location l, CTF plugin, String playerName) {
        List<Integer> lst = new ArrayList<>(Arrays.asList(l.getBlockX(), l.getBlockY(), l.getBlockZ()));
        plugin.getConfig().set(createArena.get(playerName) + ".spawn1", lst);
        creating.put(playerName, 6);
        SendMessage(Objects.requireNonNull(plugin.getServer().getPlayer(playerName))
                , "Left Click to set the location of team2's spawn", ChatColor.GREEN);
    }

    //6
    public void SetSpawn2(Location l, CTF plugin, String playerName) {
        List<Integer> lst = new ArrayList<>(Arrays.asList(l.getBlockX(), l.getBlockY(), l.getBlockZ()));
        plugin.getConfig().set(createArena.get(playerName) + ".spawn2", lst);
        creating.put(playerName, 7);
        SendMessage(Objects.requireNonNull(plugin.getServer().getPlayer(playerName))
                , "Left Click to set the return point", ChatColor.GREEN);
    }

    //7
    public void SetEnd(Location l, CTF plugin, String playerName) {
        List<Integer> lst = new ArrayList<>(Arrays.asList(l.getBlockX(), l.getBlockY(), l.getBlockZ()));
        plugin.getConfig().set(createArena.get(playerName) + ".endpoint", lst);
        creating.put(playerName, 8);
        SendMessage(Objects.requireNonNull(plugin.getServer().getPlayer(playerName))
                , "type cmd for team1 /ctf admin setInv", ChatColor.GREEN);
    }

    // Set Inventory
    private void SetInv(PlayerInventory playerInventory, String str, String playerName, CTF plugin) {
        List<ArrayList<String>> items = new ArrayList<>();
        List<ArrayList<String>> armours = new ArrayList<>();

        for (ItemStack i : playerInventory.getContents()) items.add(InvToList(i));
        for (ItemStack i : playerInventory.getContents()) armours.add(InvToList(i));

        plugin.getConfig().set(createArena.get(playerName) + "." + str + ".item", items);
        plugin.getConfig().set(createArena.get(playerName) + "." + str + ".armour", items);
    }

    private ArrayList<String> InvToList(ItemStack i) {
        ArrayList<String> itm = new ArrayList<>();
        if (i != null) {
            itm.add(i.getType().name());
            itm.add(String.valueOf(i.getAmount()));
            if (i.hasItemMeta()) {
                StringBuilder s = new StringBuilder();
                for (Enchantment en : i.getItemMeta().getEnchants().keySet()) {
                    s.append(en.getKey())
                            .append(", ")
                            .append(i.getItemMeta().getEnchants().getOrDefault(en, 0))
                            .append(", ");
                }
                itm.add(s.toString());
            } else {
                itm.add("");
            }
            // Potion
            if (i.getType().equals(Material.POTION)) {
                StringBuilder s = new StringBuilder();
                for (PotionEffect pe : Potion.fromItemStack(i).getEffects()) {
                    s.append(pe.getType().getName())
                            .append(", ")
                            .append(pe.getDuration())
                            .append(", ")
                            .append(pe.getAmplifier())
                            .append(", ")
                            .append(Potion.fromItemStack(i).isSplash())
                            .append(", ");
                }
                itm.add(s.toString());
            } else {
                itm.add("");
            }
        } else {
            itm.add(Material.AIR.name());
            itm.add("0");
            itm.add("");
            itm.add("");
        }
        return itm;
    }

    private void SendMessage(Player p, String message, ChatColor cc) {
        p.sendMessage(ChatColor.AQUA + "[S-CTF] " + cc + message);
    }
}
