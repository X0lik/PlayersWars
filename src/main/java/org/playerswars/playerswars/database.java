package org.playerswars.playerswars;
import java.sql.*;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getServer;


public class database {

    private final Connection connection;

    public database(String path) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS xd_apocalypse (" +
                    "uuid TEXT PRIMARY KEY, " +
                    "username TEXT NOT NULL, " +
                    "wars TEXT NOT NULL, " +
                    "alliances TEXT NOT NULL)");
        }
    }

    public void dbUpdate( String query, int count, String[] arr ){
        try{
            PreparedStatement dbquery = connection.prepareStatement( query );
            for ( int i = 0; i < count; i++ ){
                dbquery.setString(i+1, arr[i]);
            }
            dbquery.executeUpdate();
        } catch (SQLException err){
            getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "[XD] " + ChatColor.DARK_RED + "Database Error: " + err.getErrorCode() + "-" + err.getMessage() );
        }
    }

    public ResultSet dbQuery( String query, int count, String[] arr ){
        try{
            PreparedStatement dbquery = connection.prepareStatement( query );
            for ( int i = 0; i < count; i++ ){
                dbquery.setString(i+1, arr[i]);
            }
            ResultSet result = dbquery.executeQuery();
            return result;
        } catch (SQLException err){
            getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "[XD] " + ChatColor.DARK_RED + "Database Error: " + err.getErrorCode() + "-" + err.getMessage() );
        }
        return null;
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public void loadPlayer( Player ply, boolean force ) throws SQLException {
        if (!ply.hasPlayedBefore() || force) {
            try (PreparedStatement str = connection.prepareStatement("INSERT INTO xd_apocalypse (uuid, username, wars, alliances) VALUES( ?, ?, ?, ? )")) {
                str.setString(1, ply.getUniqueId().toString());
                str.setString(2, ply.getDisplayName());
                str.setString(3, "");
                str.setString(4, "");
                str.executeUpdate();
            }
        }
    }

    public void setPlayerWar( CommandSender admin, Player fPly, Player sPly ) throws SQLException {
        try (PreparedStatement str = connection.prepareStatement("SELECT wars FROM xd_apocalypse WHERE uuid = ?")) {
            str.setString(1, fPly.getUniqueId().toString());
            ResultSet result = str.executeQuery();
            if (result.next()) {
                String warsStr = result.getString("wars") + sPly.getDisplayName().toString() + ",";
                PreparedStatement query = connection.prepareStatement("UPDATE xd_apocalypse SET wars = ? WHERE uuid = ?");
                query.setString(1, warsStr);
                query.setString(2, fPly.getUniqueId().toString());
                query.executeUpdate();
            }
        }

        try (PreparedStatement str = connection.prepareStatement("SELECT wars FROM xd_apocalypse WHERE uuid = ?")) {
            str.setString(1, sPly.getUniqueId().toString());
            ResultSet result = str.executeQuery();
            if (result.next()) {
                String warsStr = result.getString("wars") + fPly.getDisplayName().toString() + ",";
                PreparedStatement query = connection.prepareStatement("UPDATE xd_apocalypse SET wars = ? WHERE uuid = ?");
                query.setString(1, warsStr);
                query.setString(2, sPly.getUniqueId().toString());
                query.executeUpdate();
            }
        }
    }

    public void clearPlayerWar( CommandSender admin, Player ply ) throws SQLException {

    }

    public void getPlayerWar( CommandSender admin, Player ply ) throws SQLException {

    }

}
