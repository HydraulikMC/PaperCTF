package com.intelligentcake.ctf.listener;

import com.intelligentcake.ctf.CTF;
import com.intelligentcake.ctf.TeamManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.teleport(player.getWorld().getSpawnLocation());
        player.setScoreboard(CTF.board);

        if (TeamManager.getPlayerTeam(player) != null) {
            // Re-assigning display/list name since bukkit does not save these
            player.setDisplayName(TeamManager.getPlayerTeam(player).getColour() + player.getName() + ChatColor.RESET);
            player.setPlayerListName(TeamManager.getPlayerTeam(player).getColour() + player.getName() + ChatColor.RESET);
        }
    }
}
