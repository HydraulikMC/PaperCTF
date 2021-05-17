package com.intelligentcake.ctf.game;

import com.intelligentcake.ctf.game.ScoreboardControl;
import com.intelligentcake.ctf.game.arena.ArenaData;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.units.qual.A;

import java.util.*;

public class PlayCTF extends BukkitRunnable {

    private String arena;
    private Plugin plugin;
    private World world;

    public PlayCTF(String arena, Plugin plugin, World world) {
        this.arena = arena;
        this.plugin = plugin;
        this.world = world;
        HashMap<Integer, ArrayList<String>> hm = ArenaData.playerList.getOrDefault(arena, new HashMap<>());
        ArrayList<String> team1List = hm.getOrDefault(1, new ArrayList<>());
        ArrayList<String> team2List = hm.getOrDefault(2, new ArrayList<>());
        int team1Length = team1List.size();
        int team2Length = team2List.size();

        if (team1Length == 0 || team2Length == 0) {
            Broadcast(arena + "'s countdown is cancelled because there aren't enough players!", ChatColor.RED, arena);
            ArenaData.arenaStatus.put(arena, 0);
        } else {
            ArenaData.arenaStatus.put(arena, 130);
            FileConfiguration fc = plugin.getConfig();

            // TODO: Teleport player to spawn points

            List<Integer> spawnPoint = new ArrayList<>();
            spawnPoint.addAll(fc.getIntegerList(arena + ".spawn1"));
            spawnPoint.addAll(fc.getIntegerList(arena + "spawn2"));

            ArenaData.spawnPoint.put(arena, spawnPoint);

            Broadcast(ChatColor.BOLD + "Game Started!", ChatColor.GREEN, arena);
            Broadcast("5 minutes remaining", ChatColor.GREEN, arena);

            // TODO: Add paid items
            // TODO: Create flag particle
        }

        // Arena
        List<Integer> coordinate = plugin.getConfig().getIntegerList(arena + ".flag1");
        // TODO: Create FlyingItem
        // Set flying item
        ArenaData.flag1Status.put(arena, "camp");

        coordinate = plugin.getConfig().getIntegerList(arena+".flag2");
        // Set flying item
        ArenaData.flag2Status.put(arena, "camp");
        ArenaData.arenaPoints.put(arena, Arrays.asList(0, 0));
    }

    @Override
    public void run() {
        int cn = ArenaData.arenaStatus.getOrDefault(arena, 0)-1;
        if (cn == 100) {
            // End
            new FinishCTF().Finished(arena, plugin, false);
            this.cancel();
        } else if (cn < 100) {
            this.cancel();
        } else {
            if ((cn-100)%3 == 0) {
                Broadcast(((cn-100)*10)+" seconds remaining ", ChatColor.GREEN, arena);
                ArenaData.arenaStatus.put(arena, cn);
                ArenaData.arenaPoints.put(arena, ArenaData.arenaPoints.getOrDefault(arena, Arrays.asList(0, 0)));
                new ScoreboardControl().PlayScoreBoardChange(arena);
            }
        }
    }

    private void PaidItems(List<String> teamList, String dir, boolean armor) {
        int i = 0;
        for(List<String> lst : (List<ArrayList<String>>) Objects.requireNonNull(plugin.getConfig().getList(arena + "." + dir))) {
            for(String s: teamList) {
                PlayerInventory pinv = Objects.requireNonNull(Bukkit.getServer().getPlayer(s)).getInventory();
                if(armor) {
                    switch(i) {
                        case 0:
                            pinv.setBoots(ListToItem(lst));
                        case 1:
                            pinv.setLeggings(ListToItem(lst));
                        case 2:
                            pinv.setChestplate(ListToItem(lst));
                        case 3:
                            pinv.setHelmet(ListToItem(lst));
                            //i++ is not writeen but its ok for escape error
                    }
                }else {
                    pinv.addItem(ListToItem(lst));
                }
            }
            i++;
        }
    }

    private ItemStack ListToItem(List<String> lst) {
        //setType
        ItemStack i = new ItemStack(Material.valueOf(lst.get(0)));
        //setAmount
        i.setAmount(Integer.parseInt(lst.get(1)));
        //setEnchant
        ItemMeta im = i.getItemMeta();
        String[] ss = lst.get(2).split(", ");
        for(int j=0; j<ss.length; j+=2) {
            if(ss[j].equals("")) break;
            im.addEnchant(Objects.requireNonNull(Enchantment.getByName(ss[j])), Integer.parseInt(ss[j+1]), true);
        }
        //set Potion
        if(i.getType().equals(Material.POTION)) {
            ss = lst.get(3).split(", ");
            for(int j=0; j<ss.length; j+=4) {
                PotionType pet = PotionType.getByEffect(PotionEffectType.getByName(ss[0]));
                assert Objects.requireNonNull(pet).getEffectType() != null;
                assert pet.getEffectType() != null;
                pet.getEffectType().createEffect(Integer.parseInt(ss[2]), Integer.parseInt(ss[1]));

                Potion po = new Potion(Objects.requireNonNull(PotionType.getByEffect(PotionEffectType.getByName(ss[0]))));
                po.setType(pet);
                po.setSplash(Boolean.parseBoolean(ss[3]));
                po.apply(i);
            }
        }
        i.setItemMeta(im);
        return i;
    }

    private void TeleportPlayer(List<Integer> coordinate, List<String> PlayerList) {
        for(String s : PlayerList) {
            Location l = Bukkit.getServer().getPlayer(s).getLocation().clone();
            l.setX(coordinate.get(0)+0.5);
            l.setY(coordinate.get(1)+1.0);
            l.setZ(coordinate.get(2)+0.5);
            Bukkit.getServer().getPlayer(s).teleport(l);
            Bukkit.getServer().getPlayer(s).setGameMode(GameMode.SURVIVAL);
        }
    }

    private ItemStack getFlag(int team) {
        ItemStack is;
        if(team==1) {
            is = new ItemStack(Material.LEGACY_WOOL, 1, (byte)14);
            is.getItemMeta().setDisplayName(ChatColor.RED+"RED Flag");
        }else {
            is = new ItemStack(Material.LEGACY_WOOL, 1, (byte)11);
            is.getItemMeta().setDisplayName(ChatColor.BLUE+"BLUE Flag");
        }
        List<String> lore = new ArrayList<String>();
        lore.add("bring this flag back to your base flag");
        is.getItemMeta().setLore(lore);

        return is;
    }

    private void Broadcast(String message, ChatColor cc, String arena) {
        Bukkit.getServer().broadcastMessage(ChatColor.AQUA + "[CTF]" +
                ChatColor.WHITE + "(" + arena + ") " + cc + message);
    }
}
