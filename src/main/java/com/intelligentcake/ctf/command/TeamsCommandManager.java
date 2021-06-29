package com.intelligentcake.ctf.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TeamsCommandManager implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            // Help Page
            sender.sendMessage(helpMessage(sender));
            return true;
        }

        String subCommand = args[0];
        if (subCommand.equalsIgnoreCase("create")) {	//	/team create <teamName> <teamColor>
            if (sender.isOp()) {
                AddTeamCommand.onCommand(sender, command, label, args);
            } else {
                denyPermissions(sender);
            }
        } else if (subCommand.equalsIgnoreCase("list")) {	//	/team list
            //list teams
            ListCommand.onCommand(sender, command, label, args);
        } else if (subCommand.equalsIgnoreCase("add")) {	//	/team add <player> <toTeam>
            //add player to team
            if (sender.isOp()) {
                SetTeamCommand.onCommand(sender, command, label, args);
            } else {
                denyPermissions(sender);
            }
        } else if (subCommand.equalsIgnoreCase("remove")) {	//	/team remove <player> <fromTeam>
            //remove player from team
            if (sender.isOp()) {
                RemovePlayerCommand.onCommand(sender, command, label, args);
            } else {
                denyPermissions(sender);
            }
        } else if (subCommand.equalsIgnoreCase("get")) {	//	/team get <player>
            //get user's team
            GetTeamCommand.onCommand(sender, command, label, args);
        } else if (subCommand.equalsIgnoreCase("info")) {	//	/team info <team>
            //list team's users
        } else if (subCommand.equalsIgnoreCase("getflag")) {	//	/team getflag <team>
            //get flag location
            GetFlagCommand.onCommand(sender, command, label, args);
        } else if (subCommand.equalsIgnoreCase("setflag")) {	//	/team setflag <team>
            //sets flag location for team
            if (sender.isOp()) {
                SetFlagCommand.onCommand(sender, command, label, args);
            } else {
                denyPermissions(sender);
            }
        } else if (subCommand.equalsIgnoreCase("help")) {
            sender.sendMessage(helpMessage(sender));
        } else if (subCommand.equalsIgnoreCase("delete")) {
            if (sender.isOp()) {
                DeleteTeamCommand.onCommand(sender, command, label, args);
            } else {
                denyPermissions(sender);
            }
        } else if (subCommand.equalsIgnoreCase("home")) {
            HomeCommand.onCommand(sender, command, label, args);
        } else if (subCommand.equalsIgnoreCase("setscore")) {	//	/team getflag <team>
            if (sender.isOp()) {
                SetScoreCommand.onCommand(sender, command, label, args);
            } else {
                denyPermissions(sender);
            }
        } else if (subCommand.equalsIgnoreCase("pvp")) {
            if (sender.isOp()) {
                PvPCommand.onCommand(sender, command, label, args);
            } else {
                denyPermissions(sender);
            }
        } else if (subCommand.equalsIgnoreCase("start")) {
            if (sender.isOp()) {
                StartCommand.onCommand(sender, command, label, args);
            } else {
                denyPermissions(sender);
            }
        } else if (subCommand.equalsIgnoreCase("stop")) {
            if (sender.isOp()) {
                StopCommand.onCommand(sender, command, label, args);
            } else {
                denyPermissions(sender);
            }
        }
        return true;
    }

    public String helpMessage(CommandSender sender) {

        String message = ChatColor.GREEN + "=============CTF===============";

        message += "\nCommands:";
        // Admin Commands
        if (sender.isOp()) {
            message += "\n" + ChatColor.AQUA + "/ctfteam help" + ChatColor.GREEN + " - list help menu for plugin";
            message += "\n" + ChatColor.AQUA + "/ctfteam home" + ChatColor.GREEN + " - teleport to your flag";
            message += "\n" + ChatColor.AQUA + "/ctfteam create <teamName> <teamColour>" + ChatColor.GREEN + " - create new a team with the specified name/colour";
            message += "\n" + ChatColor.AQUA + "/ctfteam delete <teamName>" + ChatColor.GREEN + " - delete a team completely (removes members as well)";
            message += "\n" + ChatColor.AQUA + "/ctfteam list" + ChatColor.GREEN + " - lists all of the current teams";
            message += "\n" + ChatColor.AQUA + "/ctfteam add/remove <player> <team>" + ChatColor.GREEN + " - add/remove user to/from specified team";
            message += "\n" + ChatColor.AQUA + "/ctfteam setFlag <teamName>" + ChatColor.GREEN + " - set the location of the specified team's flag";
            message += "\n" + ChatColor.AQUA + "/ctfteam getFlag <teamName>" + ChatColor.GREEN + " - get the location of the specified team's flag";
            message += "\n" + ChatColor.AQUA + "/ctfteam setScore <teamName> <score>" + ChatColor.GREEN + " - set specified team's score";
            message += "\n" + ChatColor.AQUA + "/ctfteam get <player>" + ChatColor.GREEN + " - get the specified user's team name";
            message += "\n" + ChatColor.AQUA + "/ctfteam info <teamName>" + ChatColor.GREEN + " - list info about the specified team";
            message += "\n" + ChatColor.AQUA + "/ctfteam pvp true/false" + ChatColor.GREEN + " - enable or disable pvp manually";
            message += "\n" + ChatColor.AQUA + "/ctfteam start <timeInSeconds>" + ChatColor.GREEN + " - start the countdown to game";
            message += "\n" + ChatColor.AQUA + "/ctfteam stop" + ChatColor.GREEN + " - stop the game";
        } else {
            // Regular Commands
            message += "\n" + ChatColor.AQUA + "/ctfteam help" + ChatColor.GREEN + " - list help menu for plugin";
            message += "\n" + ChatColor.AQUA + "/ctfteam home" + ChatColor.GREEN + " - teleport to your flag";
            message += "\n" + ChatColor.AQUA + "/ctfteam list" + ChatColor.GREEN + " - lists all of the current teams";
            message += "\n" + ChatColor.AQUA + "/ctfteam getFlag" + ChatColor.GREEN + " - get the location of your team's flag";
            message += "\n" + ChatColor.AQUA + "/ctfteam get" + ChatColor.GREEN + " - get your team's name";
            message += "\n" + ChatColor.AQUA + "/ctfteam info <teamName>" + ChatColor.GREEN + " - list info about your team";
        }
        message += "\n=====================================";
        return message;
    }

    public void denyPermissions(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "You do not have permission to do that!");
    }

    public static boolean isConsole(CommandSender sender) {
        return (sender instanceof Player);
    }
}
