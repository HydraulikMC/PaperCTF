package com.intelligentcake.ctf.game;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Team {

    private final String name;
    private final List<UUID> members = new ArrayList<>();

    public Team(String name) {
        this.name = name;
    }

    public boolean isInTeam(Player player) {
        return members.contains(player.getUniqueId());
    }

    public List<UUID> getMembers() {
        return members;
    }

    public String getName() {
        return name;
    }

    public void addMember(Player player) {
        members.add(player.getUniqueId());
    }

    public void removeMember(Player player) {
        members.remove(player.getUniqueId());
    }


}
