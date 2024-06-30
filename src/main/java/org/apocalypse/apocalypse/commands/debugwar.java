package org.apocalypse.apocalypse.commands;

import net.md_5.bungee.api.ChatColor;
import org.apocalypse.apocalypse.Apocalypse;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class debugwar implements CommandExecutor {

    private final Apocalypse apocalypse;

    public debugwar(Apocalypse apocalypse ){
        this.apocalypse = apocalypse;
    }

    @Override
    public boolean onCommand(CommandSender ply, Command cmd, String str, String[] args ){

        Player pl = Bukkit.getPlayer(ply.getName());

        try{
            this.apocalypse.getDatabase().getPlayerWar(ply, pl);
        } catch( SQLException err ){
            err.printStackTrace();
            ply.sendMessage( ChatColor.LIGHT_PURPLE + "[XD] " + ChatColor.RED + "Something went wrong with database" );
            return true;
        }

        return true;
    }

}
