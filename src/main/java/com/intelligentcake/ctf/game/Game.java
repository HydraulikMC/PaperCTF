package com.intelligentcake.ctf.game;

import com.intelligentcake.ctf.CTF;
import com.intelligentcake.ctf.threads.StartCountdown;
import com.intelligentcake.ctf.utils.ChatUtilities;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Game {

    private final TeamManager teamManager;
    private GameState gameState;

    private boolean canStart;

    public Game(CTF plugin) {
        this.teamManager = new TeamManager();
        this.teamManager.createTeam("Red");
        this.teamManager.createTeam("Blue");

        gameState.setState(GameState.IN_LOBBY);

    }

    public void start() {
        String teamName = "Red";
        for (Player player : Bukkit.getOnlinePlayers()) {
            teamManager.getTeams().get(teamName).addMember(player);
            teamName = teamName.equals("Red") ? "Blue" : "Red";
        }
    }

    public void stop() {

    }

    public boolean canStart() {
        return canStart;
    }

    public void setCanStart(boolean canStart) {
        this.canStart = canStart;
    }

    public GameState getGameState() {
        return this.gameState;
    }
}
