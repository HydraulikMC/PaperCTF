package com.intelligentcake.ctf.player;

import com.intelligentcake.ctf.CTF;
import com.intelligentcake.ctf.game.ScoreboardControl;
import com.intelligentcake.ctf.game.WaitCTF;
import com.intelligentcake.ctf.game.arena.ArenaData;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.lang.reflect.Array;
import java.util.*;

public class ManagePlayer<EntityPlayer> {

    public void Join(Player p, String arena, CTF plugin) {
        int cn = ArenaData.arenaStatus.getOrDefault(arena, 0);

        if (!plugin.getConfig().getStringList("List").contains(arena)) {
            SendMessage(p, "This arena does not exist.", ChatColor.RED);
        } else if (ArenaData.playerStatus.containsKey(p.getName())) {
            SendMessage(p, "You have already join another arena.", ChatColor.RED);
        } else if (!plugin.getConfig().getBoolean(arena + ".enable")) {
            SendMessage(p, "This arena is disabled.", ChatColor.RED);
        } else {
            // Player
            SendMessage(p, "Joining a CTF game in arena " + arena, ChatColor.RED);
            SetupPlayer(p);
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
                p.setPlayerListName(ChatColor.BLUE + p.getName());
                team = 2;
            }
            if (cn > 99) {
                // Already playing
                new ScoreboardControl().PlayScoreBoardChange(arena);
                if (team == 1) {
                    // TODO: Implement these methods
                    TeleportPlayer(plugin.getConfig().getIntegerList(arena + ".spawn1"), Arrays.asList(p.getName()));
                    PaidItems(Arrays.asList(p.getName()), "Inv1.item", plugin, arena, false);
                    PaidItems(Arrays.asList(p.getName()), "Inv1.armour", plugin, arena, true);
                } else {
                    TeleportPlayer(plugin.getConfig().getIntegerList(arena + ".spawn2"), Arrays.asList(p.getName()));
                    PaidItems(Arrays.asList(p.getName()), "Inv2.item", plugin, arena, false);
                    PaidItems(Arrays.asList(p.getName()), "Inv2.armour", plugin, arena, true);
                }
            } else if (cn > -2) {
                new ScoreboardControl().WaitScoreboardChange(arena);

            } else {
                Broadcast("Fatal error when applying scoreboard in ManagePlayer.java", ChatColor.DARK_RED);
                Broadcast("Scoreboard Control Number is " + cn, ChatColor.DARK_RED);
            }

            // Arena
            hm = ArenaData.playerList.getOrDefault(arena, new HashMap<Integer,  ArrayList<String>>());
            team1List = hm.getOrDefault(1, null);
            team2List = hm.getOrDefault(2, null);
            team1Length = 0;
            team2Length = 0;
            if(team1List!=null) team1Length = team1List.size();
            if(team2List!=null) team2Length = team2List.size();
            if(team1Length>0 && team2Length>0) {
                if(cn==-1) {
                    Broadcast(arena + "'s countdown restarted because there aren't enough players!", ChatColor.GREEN);
                    ArenaData.arenaStatus.put(arena, 6);
                    // TODO: Implement method
                    new WaitCTF(arena, plugin, p.getWorld()).runTaskTimer(plugin, 100, 200);
                }else if(cn==0) {
                    //new
                    ArenaData.arenaStatus.put(arena, 6);
                    Broadcast("("+arena+")"+ChatColor.GREEN+" Starting countdown!", ChatColor.WHITE);
                    // TODO: Implement method
                    new WaitCTF(arena, plugin, p.getWorld()).runTaskTimer(plugin, 100, 200);//debug

                }else if(cn>0){
                    //none
                }else if(cn>99) {
                    //already playing

                }else {
                    Broadcast("Fatal error when setting arena in ManagePlayer.java", ChatColor.DARK_RED);
                }
            }
        }
    }

    public void leave(Player p, CTF plugin) {

        if(ArenaData.playerStatus.containsKey(p.getName())) {
            String arena = ArenaData.playerStatus.get(p.getName());
            int cn = ArenaData.arenaStatus.getOrDefault(arena, 0);

            //player
            SendMessage(p, "You leave from " + arena, ChatColor.GREEN);
            SetupPlayer(p);
            HashMap<Integer, ArrayList<String>> hm = ArenaData.playerList.getOrDefault(arena, new HashMap<Integer, ArrayList<String>>());
            int team = ArenaData.BelongTeam(p.getName());
            ArrayList<String> teamList = hm.getOrDefault(team, new ArrayList<String>());
            teamList.remove(p.getName());
            hm.put(team, teamList);
            ArenaData.playerList.put(arena, hm);
            if(cn>99) {
                new ScoreboardControl().PlayScoreBoardChange(arena);
                p.teleport(p.getWorld().getSpawnLocation());
            }else if(cn>-2) {
                new ScoreboardControl().WaitScoreboardChange(arena);
            }else {
                Broadcast("Fatal error when applying scoreboard in ManagePlayer.java", ChatColor.DARK_RED);
                Broadcast("Scoreboard Control Number is "+ cn, ChatColor.DARK_RED);
            }
            ArenaData.playerStatus.remove(p.getName());
            p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            p.setPlayerListName(ChatColor.WHITE+p.getName());

            //arena
            hm = ArenaData.playerList.getOrDefault(arena, new HashMap<>());
            ArrayList<String> team1List = hm.getOrDefault(1, new ArrayList<>());
            ArrayList<String> team2List = hm.getOrDefault(2, new ArrayList<>());
            if(ArenaData.arenaStatus.containsKey(arena)
                    &&( team1List.size()==0 || team2List.size()==0)) {
                if(cn>99) {
                    //already playing
                    Broadcast(arena + "'s game is cancelled because there aren't enough players.", ChatColor.RED);
                    // TODO: Implement finish game
                    new FinishCTF().Finished(arena, plugin, true);
                }else if(cn>-2) {
                    Broadcast(arena + "'s countdown is cancelled because there aren't enough players.", ChatColor.RED);
                    ArenaData.arenaStatus.put(arena, -1);
                }else {
                    Broadcast("Fatal error when setting arena in ManagePlayer.java", ChatColor.DARK_RED);
                }
            }

        }else {
            SendMessage(p, "You joined nothing.", ChatColor.RED);
        }
    }

    public void PaidItems(PlayerInventory pinv, String dir, CTF plugin, String arena, boolean armour) {
        int i = 0;
        for(List<String> lst : (List<ArrayList<String>>) Objects.requireNonNull(plugin.getConfig().getList(arena + "." + dir))) {
            setArmour(pinv, armour, i, lst);
            i++;
        }
    }

    private void setArmour(PlayerInventory pinv, boolean armour, int i, List<String> lst) {
        if(armour) {
            switch(i) {
                case 0:
                    pinv.setBoots(ListToItem(lst));
                case 1:
                    pinv.setLeggings(ListToItem(lst));
                case 2:
                    pinv.setChestplate(ListToItem(lst));
                case 3:
                    pinv.setHelmet(ListToItem(lst));
                    //i++ is not written but its ok for escape error
            }
        }else {
            pinv.addItem(ListToItem(lst));
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
                assert pet != null;
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
            Location l = Objects.requireNonNull(Bukkit.getServer().getPlayer(s)).getLocation().clone();
            l.setX(coordinate.get(0)+0.5);
            l.setY(coordinate.get(1)+1.0);
            l.setZ(coordinate.get(2)+0.5);
            Objects.requireNonNull(Bukkit.getServer().getPlayer(s)).teleport(l);
            Objects.requireNonNull(Bukkit.getServer().getPlayer(s)).setGameMode(GameMode.SURVIVAL);
        }
    }

    private void PaidItems(List<String> teamList, String dir, CTF plugin, String arena, boolean armour) {
        int i = 0;
        for(List<String> lst : (List<ArrayList<String>>) Objects.requireNonNull(plugin.getConfig().getList(arena + "." + dir))) {
            for(String s: teamList) {
                PlayerInventory pinv = Objects.requireNonNull(Bukkit.getServer().getPlayer(s)).getInventory();
                setArmour(pinv, armour, i, lst);
            }
            i++;
        }
    }

    private void SetupPlayer(Player p) {
        p.getInventory().clear();
        p.getInventory().setHelmet(new ItemStack(Material.AIR));
        p.getInventory().setChestplate(new ItemStack(Material.AIR));
        p.getInventory().setLeggings(new ItemStack(Material.AIR));
        p.getInventory().setBoots(new ItemStack(Material.AIR));
    }

    private void SendMessage(Player p, String message, ChatColor cc) {
        p.sendMessage(ChatColor.AQUA + "[CTF] " + cc + message);
    }

    private void Broadcast(String message, ChatColor cc) {
        Bukkit.getServer().broadcastMessage(ChatColor.AQUA + "[CTF] " + cc + message);
    }
}
