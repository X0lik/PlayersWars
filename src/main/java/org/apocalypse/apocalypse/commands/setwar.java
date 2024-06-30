package org.apocalypse.apocalypse.commands;

import net.md_5.bungee.api.ChatColor;
import org.apocalypse.apocalypse.Apocalypse;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class setwar implements CommandExecutor {

    private final Apocalypse apocalypse;

    public setwar(Apocalypse apocalypse ){
        this.apocalypse = apocalypse;
    }

    @Override
    public boolean onCommand( CommandSender ply, Command cmd, String str, String[] args ){
        if (args.length != 2){
            ply.sendMessage( ChatColor.LIGHT_PURPLE + "[XD] " + ChatColor.RED + "Not enough arguments!");
            ply.sendMessage( ChatColor.LIGHT_PURPLE + "[XD] " + ChatColor.RED + "Must be /setwar <player> <another_player>");
            return true;
        }

        String plyName1 = args[0];
        Player target1 = Bukkit.getPlayer(plyName1);
        if (target1 == null){
            ply.sendMessage( ChatColor.LIGHT_PURPLE + "[XD] " + ChatColor.RED + "First Player not found!");
            return true;
        }

        String plyName2 = args[0];
        Player target2 = Bukkit.getPlayer(plyName2);
        if (target2 == null){
            ply.sendMessage( ChatColor.LIGHT_PURPLE + "[XD] " + ChatColor.RED + "Second Player not found!");
            return true;
        }

        try{
            this.apocalypse.getDatabase().setPlayerWar( ply, target1, target2 );
            target1.sendTitle( ChatColor.DARK_RED + "Вы в состоянии войны", ChatColor.WHITE + "Будьте осторожны!", 10, 70, 10 );
        } catch( SQLException err ){
            err.printStackTrace();
            ply.sendMessage( ChatColor.LIGHT_PURPLE + "[XD] " + ChatColor.RED + "Something went wrong with database" );
            return true;
        }

        ply.sendMessage( ChatColor.LIGHT_PURPLE + "[XD] " + ChatColor.WHITE + "War status has been set!" );
        return true;
    }
}
