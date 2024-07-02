package org.playerswars.playerswars.commands;

import net.md_5.bungee.api.ChatColor;
import org.playerswars.playerswars.database;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.bukkit.Bukkit.getServer;

public class loadwar implements CommandExecutor {

    private final database db;
    public loadwar(database db){
        this.db = db;
    }

    public static void loadPlayer(database db, Player ply, boolean force){
        try{
            String[] params = { ply.getUniqueId().toString() };
            ResultSet res = db.dbQuery( "SELECT * FROM xd_apocalypse WHERE uuid = ?", 1, params );
            if (!res.next() || force ){
                String[] sParams = { ply.getUniqueId().toString(), ply.getDisplayName(), "", "" };
                db.dbUpdate( "INSERT OR IGNORE INTO xd_apocalypse (uuid, username, wars, alliances) VALUES( ?, ?, ?, ? )", 4, sParams );
            }
        } catch( SQLException err ){
            getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "[XD] " + ChatColor.DARK_RED + "Database Error: " + ChatColor.WHITE + err.getErrorCode() + " - " + err.getMessage() );
        }
    };

    @Override
    public boolean onCommand(CommandSender ply, Command cmd, String str, String[] args ){
        if (args.length != 1){
            ply.sendMessage( ChatColor.LIGHT_PURPLE + "[XD] " + ChatColor.RED + "Not enough arguments!");
            ply.sendMessage( ChatColor.LIGHT_PURPLE + "[XD] " + ChatColor.RED + "Must be /loadwar <player>");
            return true;
        }

        String plyName = args[0];
        Player target = Bukkit.getPlayer(plyName);
        if (target == null){
            ply.sendMessage( ChatColor.LIGHT_PURPLE + "[XD] " + ChatColor.RED + "Player not found!");
            return true;
        }

        loadPlayer( this.db, target, true );
        ply.sendMessage( ChatColor.LIGHT_PURPLE + "[XD] " + ChatColor.WHITE + "Player was loaded!" );
        return true;
    }

}
