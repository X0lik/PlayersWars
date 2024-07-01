package org.apocalypse.apocalypse.commands;

import net.md_5.bungee.api.ChatColor;
import org.apocalypse.apocalypse.Apocalypse;
import org.apocalypse.apocalypse.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.bukkit.Bukkit.getServer;

public class setwar implements CommandExecutor {

    private final Database db;
    public setwar(Database db){
        this.db = db;
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

            String[] targetData = { target1.getUniqueId().toString() };
            ResultSet warsData = this.db.dbQuery( "SELECT wars FROM xd_apocalypse WHERE uuid = ?",1, targetData );
            if (warsData.next()){
                String warsStr = warsData.getString("wars") + target2.getDisplayName() + ",";
                String[] targetParams = { warsStr, target1.getUniqueId().toString() };
                this.db.dbUpdate( "UPDATE xd_apocalypse SET wars = ? WHERE uuid = ?", 2, targetParams );
            }

            targetData[0] = target2.getUniqueId().toString();
            warsData = this.db.dbQuery( "SELECT wars FROM xd_apocalypse WHERE uuid = ?",1, targetData );
            if (warsData.next()){
                String warsStr = warsData.getString("wars") + target1.getDisplayName() + ",";
                String[] targetParams = { warsStr, target2.getUniqueId().toString() };
                this.db.dbUpdate( "UPDATE xd_apocalypse SET wars = ? WHERE uuid = ?", 2, targetParams );
            }

        } catch(SQLException err) {
            getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "[XD] " + ChatColor.DARK_RED + "Database Error: " + ChatColor.WHITE + err.getErrorCode() + " - " + err.getMessage() );
        }


        target1.sendTitle( ChatColor.DARK_RED + "Вы в состоянии войны", ChatColor.WHITE + "Противник: " + ChatColor.RED + plyName2 + ChatColor.WHITE + ", будьте осторожны!", 10, 70, 10 );
        target2.sendTitle( ChatColor.DARK_RED + "Вы в состоянии войны", ChatColor.WHITE + "Противник: " + ChatColor.RED + plyName1 + ChatColor.WHITE + ", будьте осторожны!", 10, 70, 10 );

        ply.sendMessage( ChatColor.LIGHT_PURPLE + "[XD] " + ChatColor.WHITE + "War status has been set!" );
        return true;
    }
}
