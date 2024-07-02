package org.playerswars.playerswars;

import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class GlowManager {

    public static void setGlowColor(Player ply, ChatColor color ){
        Scoreboard mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        ply.setScoreboard(mainScoreboard);
        if( mainScoreboard.getTeam( color + "team" ) == null ){
            Team team = mainScoreboard.registerNewTeam(color + "team");
            ((Team) team).setColor( color );
            team.addEntry(ply.getName());
        } else {
            Team team = mainScoreboard.getTeam( color + "team" );
            team.addEntry(ply.getName());
        }
    }
    public static void setGlow(Player ply, boolean IsEnable ){
        if (IsEnable){
            ((Entity) ply).setGlowing( true );
        } else {
            ((Entity) ply).setGlowing( false );
        }
    }

}
