package org.apocalypse.apocalypse;

import net.md_5.bungee.api.ChatColor;

import org.apocalypse.apocalypse.commands.clearwar;
import org.apocalypse.apocalypse.commands.debugwar;
import org.apocalypse.apocalypse.commands.loadwar;
import org.apocalypse.apocalypse.commands.setwar;
import org.apocalypse.apocalypse.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.entity.Player;

import java.sql.SQLException;

import static org.bukkit.Bukkit.getServer;

public final class Apocalypse extends JavaPlugin implements Listener {

    private Database db;
    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "[XD] " + ChatColor.WHITE + "Apocalypse enabled");
        getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "[XD] " + ChatColor.WHITE + "Our Discord: " + ChatColor.DARK_AQUA + "https://discord.gg/UuSJ8jN59w");
        getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "[XD] " + ChatColor.WHITE + "Github: " + ChatColor.DARK_BLUE + "https://github.com/X0lik/players_wars");
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            db = new Database(getDataFolder().getAbsolutePath() + "/playerswars.db");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to database! " + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
        }

        getCommand( "setwar").setExecutor(new setwar(this.db));
        getCommand( "clearwar").setExecutor(new clearwar(this.db));
        getCommand( "debugwar").setExecutor(new debugwar(this.db));
        getCommand( "loadwar").setExecutor(new loadwar(this.db));
        getServer().getPluginManager().registerEvents(this, this);

    }

    /*public Database getDatabase(){
        return this.db;
    }*/

    @Override
    public void onDisable() {
        try {
            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerJoin( PlayerJoinEvent e ) {
        Player ply = e.getPlayer();
        loadwar.loadPlayer( db, ply, false );
    }
}
