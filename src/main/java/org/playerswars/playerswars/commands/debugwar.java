package org.playerswars.playerswars.commands;

import org.bukkit.ChatColor;
import org.playerswars.playerswars.Database;
import org.playerswars.playerswars.GlowManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.bukkit.Bukkit.getServer;

public class debugwar implements CommandExecutor {

    private final Database db;

    public debugwar(Database db){
        this.db = db;
    }

    @Override
    public boolean onCommand(CommandSender ply, Command cmd, String str, String[] args ){

        Player target = Bukkit.getPlayer(ply.getName());

        try {
            String[] params = {target.getUniqueId().toString()};
            ResultSet res = this.db.dbQuery("SELECT wars FROM xd_playerswars WHERE uuid = ?", 1, params);
            ply.sendMessage(ChatColor.LIGHT_PURPLE + "[XD] " + ChatColor.WHITE + res.getString("wars"));
            GlowManager.setGlowColor( target, ChatColor.AQUA );
            GlowManager.setGlow( target, true );
        } catch (SQLException err){
            getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "[XD] " + ChatColor.DARK_RED + "Database Error: " + ChatColor.WHITE + err.getErrorCode() + " - " + err.getMessage() );
        }

        return true;
    }

}
