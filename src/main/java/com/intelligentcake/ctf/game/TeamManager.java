package com.intelligentcake.ctf.game;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TeamManager {

    // <team name, Team>
    private HashMap<String, Team> teams = new HashMap<>();
    // <player UUID, team name>
    private HashMap<UUID, String> players = new HashMap<>();

    public void createTeam(String name) {
        Team team = new Team(name);
        teams.put(name, team);
    }

    public boolean addPlayer(Player player, String name) {
        if (teams.containsKey(name) && !players.containsKey(player.getUniqueId())) {
            Team team = teams.get(name);
            team.addMember(player);
            players.put(player.getUniqueId(), name);
            return true;
        }
        return false;
    }

    public void removePlayer(Player player) {
        if (players.containsKey(player.getUniqueId())) {
            String name = players.get(player.getUniqueId());
            players.remove(player.getUniqueId());
            Team team = teams.get(name);
            team.removeMember(player);
        }
    }

    public HashMap<String, Team> getTeams() {
        return teams;
    }

    public HashMap<UUID, String> getPlayers() {
        return players;
    }

    public boolean hasTeam(Player player) {
        for (Team team : teams.values()) {
            if (team.isInTeam(player)) {
                return  true;
            }
        }
        return false;
    }

    public Team getTeam(Player player) {
        if (hasTeam(player)) {
            for (Team team : teams.values()) {
                if (team.isInTeam(player)) {
                    return team;
                }
            }
        }
        return null;
    }
}
