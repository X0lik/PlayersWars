package org.playerswars.playerswars;
import java.sql.*;

import net.md_5.bungee.api.ChatColor;

import static org.bukkit.Bukkit.getServer;


public class Database {

    private final Connection connection;

    public Database(String path) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS xd_playerswars (" +
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

}
