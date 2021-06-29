package com.intelligentcake.ctf;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class TeamManager {

    // Key: TeamName, Value: TeamObject
    private static HashMap<String, Team> teams = new HashMap<>();
    // Key: flagCarrier(Player), Value: Victim Team(Team)
    public static HashMap<Player, Team> flagCarriers = new HashMap<>();
    public static List<String> availableColours = new ArrayList<>();

    public static boolean addTeam(String teamName, String teamColour) {
        teamColour = teamColour.toUpperCase();
        if (containsTeam(teamName)) {
            return false;
        } else if (!availableColours.contains(teamColour)) {
            return false;
        } else {
            teams.put(teamName.toUpperCase(), new Team(teamName, teamColour));
            availableColours.remove(teamColour);
            return true;
        }
    }

    public static boolean removeTeam(Team deleteTeam) {
        if (teams.containsValue(deleteTeam)) {
            deleteTeam.purge();
            teams.remove(deleteTeam.getName().toUpperCase());
            return true;
        }
        return false;
    }

    public static Team getTeam(String teamName) {
        teamName = teamName.toUpperCase();
        if (teams.containsKey(teamName)) {
            return teams.get(teamName);
        }
        return null;
    }

    public static Set<String> getTeamNames() {
        return teams.keySet();
    }

    public static Collection<Team> getTeams() {
        return teams.values();
    }

    public static boolean containsTeam(String teamName) {
        return teams.containsKey(teamName.toUpperCase());
    }

    public static Team getPlayerTeam(Player player) {
        for (Team team : teams.values()) {
            if (team.containsPlayer(player)) {
                return team;
            }
        }
        return null;
    }

    public static ChatColor getPlayerColour(Player player) {
        return getPlayerTeam(player).getColour();
    }

    public static void purgePlayer(Player player) {
        for (Team team : TeamManager.teams.values()) {
            if (team.containsPlayer(player)) {
                team.removePlayer(player);
            }
        }
    }
}
