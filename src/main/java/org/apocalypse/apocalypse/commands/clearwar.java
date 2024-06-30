package org.apocalypse.apocalypse.commands;

import net.md_5.bungee.api.ChatColor;
import org.apocalypse.apocalypse.Apocalypse;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class clearwar implements CommandExecutor {

    private final Apocalypse apocalypse;

    public clearwar(Apocalypse apocalypse ){
        this.apocalypse = apocalypse;
    }

    @Override
    public boolean onCommand(CommandSender ply, Command cmd, String str, String[] args ){
        if (args.length != 1){
            ply.sendMessage( ChatColor.LIGHT_PURPLE + "[XD] " + ChatColor.RED + "Not enough arguments!");
            ply.sendMessage( ChatColor.LIGHT_PURPLE + "[XD] " + ChatColor.RED + "Must be /clearwar <player>");
            return true;
        }

        String plyName = args[0];
        Player target = Bukkit.getPlayer(plyName);
        if (target == null){
            ply.sendMessage( ChatColor.LIGHT_PURPLE + "[XD] " + ChatColor.RED + "Player not found!");
            return true;
        }


        try{
            this.apocalypse.getDatabase().clearPlayerWar( ply, target );
        } catch( SQLException err ){
            err.printStackTrace();
            ply.sendMessage( ChatColor.LIGHT_PURPLE + "[XD] " + ChatColor.RED + "Something went wrong with database" );
            return true;
        }

        ply.sendMessage( ChatColor.LIGHT_PURPLE + "[XD] " + ChatColor.WHITE + "War status has been cleared!" );
        return true;
    }

}
