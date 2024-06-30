package org.apocalypse.apocalypse;

import net.md_5.bungee.api.ChatColor;
import org.apocalypse.apocalypse.commands.setwar;
import org.apocalypse.apocalypse.commands.clearwar;
import org.apocalypse.apocalypse.commands.debugwar;
import org.apocalypse.apocalypse.commands.loadwar;
import org.apocalypse.apocalypse.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;
import org.bukkit.GameMode;

import java.sql.SQLException;

public final class Apocalypse extends JavaPlugin implements Listener {

    private Database db;
    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "[XD] " + ChatColor.WHITE + "Apocalypse enabled");
        getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "[XD] " + ChatColor.WHITE + "Our Discord: " + ChatColor.DARK_AQUA + "https://discord.gg/UuSJ8jN59w");
        getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "[XD] " + ChatColor.WHITE + "Github: " + ChatColor.DARK_BLUE + "https://github.com/X0lik/players_wars");
        try {
            // Ensure the plugin's data folder exists
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }

            db = new Database(getDataFolder().getAbsolutePath() + "/apocalypse.db");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to database! " + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
        }

        getCommand( "setwar").setExecutor(new setwar(this));
        getCommand( "clearwar").setExecutor(new clearwar(this));
        getCommand( "debugwar").setExecutor(new debugwar(this));
        getCommand( "loadwar").setExecutor(new loadwar(this));
        getServer().getPluginManager().registerEvents(this, this);

    }

    public Database getDatabase(){
        return this.db;
    }

    @Override
    public void onDisable() {
        try {
            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onBlockDamage( BlockBreakEvent e ) {
        System.out.println( "Block has been broken" );
        Player ply = e.getPlayer();
        ply.setGameMode( GameMode.SURVIVAL );
    }

    @EventHandler
    public void onPlayerJoin( PlayerJoinEvent e ) {
        Player ply = e.getPlayer();
        try{
            db.loadPlayer( ply, false );
        } catch (SQLException err){
            System.out.println( "[XD] Failed to load a player: " + ply.getDisplayName() );
            err.printStackTrace();
        }
    }
}
