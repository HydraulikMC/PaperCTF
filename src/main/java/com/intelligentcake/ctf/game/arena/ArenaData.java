package com.intelligentcake.ctf.game.arena;

import org.bukkit.entity.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArenaData {

    // <playerName, arenaName>
    public static HashMap<String, String> playerStatus = new HashMap<>();

    // <arenaName, <Team, PlayerList>>
    public static HashMap<String, HashMap<Integer, ArrayList<String>>> playerList = new HashMap<>();

    // SpawnPoint <arenaName, {x1, y1, z1, x2, y2, z2}>
    public static HashMap<String, List<Integer>> spawnPoint = new HashMap<>();

    // <arena, arenaStatusNumber>
    public static HashMap<String, Integer> arenaStatus = new HashMap<>();

    /* ArenaStatus
      0 or Null = none
      -1 = waiting cancellation
      6,5,4,3,2,1 = waiting for player
      *10  is the seconds on the timer
      130,129,128...101 = playing time left (130 => 5m0s(30*10s), 129 => 2m50s(29*10s))
      131 is ready time
     */

    // <arena, flagStatus>
    public static HashMap<String, String> flag1Status = new HashMap<>();
    public static HashMap<String, String> flag2Status = new HashMap<>();

    /*
      "camp" = flag on base camp
      "onGround", "count" = onGround (count=6,5,4,3,2,1,0. 0 means the flag is returned. count *5 seconds)
      playerName = havePlayer
     */

    //<arena, DropItem> when flag is onGround
    public static HashMap<String, Item> flag1Drop = new HashMap<String, Item>();
    public static HashMap<String, Item> flag2Drop = new HashMap<String, Item>();

    // <arena, armor>
    public static HashMap<String, FlyingItem> flagArmor1 = new HashMap<String, FlyingItem>();
    public static HashMap<String, FlyingItem> flagArmor2 = new HashMap<String, FlyingItem>();

    // <arena {team1Point, team2Point}
    public static HashMap<String, List<Integer>> arenaPoints = new HashMap<String, List<Integer>>();

    // <playerName arena>
    public static HashMap<String, String> spectator = new HashMap<String, String>();

    public int BelongTeam(String name) {
        if(playerStatus.containsKey(name)) {
            ArrayList<String> list = playerList.get(playerStatus.get(name)).get(1);
            for(String s : list) {
                if(s.equalsIgnoreCase(name)) return 1;
            }
            return 2;
        }else {
            return 0;
        }
    }
}
