package org.playerswars.playerswars;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import static org.bukkit.Bukkit.getServer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.Material;

import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;


public class War {

    public static boolean inWar( Database db, Player fPly, Player sPly ){
        String[] params = {fPly.getUniqueId().toString()};
        try{
            ResultSet res = db.dbQuery( "SELECT wars FROM xd_playerswars WHERE uuid = ?", 1, params );
            String[] actualWars = res.getString("wars").split(",");
            return Arrays.asList(actualWars).contains(sPly.getDisplayName());
        } catch (SQLException err){
            err.printStackTrace();
        }
        return false;
    }

    public static void winWar( Database db, Player winner, Player looser ){
        String[] params = {winner.getUniqueId().toString()};
        try{
            ResultSet res = db.dbQuery( "SELECT wars FROM xd_playerswars WHERE uuid = ?", 1, params );
            String[] actualWars = res.getString("wars").split(",");
            String newWars = "";
            for( int i = 0; i<actualWars.length; i++ ){
                if (!Objects.equals( actualWars[i], looser.getDisplayName() )){
                    newWars += actualWars[i] + ",";
                }
            }
            String[] newParams = { newWars, winner.getUniqueId().toString() };
            db.dbUpdate( "UPDATE xd_playerswars SET wars = ? WHERE uuid = ?", 2, newParams );
        } catch (SQLException err){
            err.printStackTrace();
        }

        params[0] = looser.getUniqueId().toString();
        try{
            ResultSet res = db.dbQuery( "SELECT wars FROM xd_playerswars WHERE uuid = ?", 1, params );
            String[] actualWars = res.getString("wars").split(",");
            String newWars = "";
            for( int i = 0; i<actualWars.length; i++ ){
                if (actualWars[i] != winner.getDisplayName()){
                    newWars += actualWars[i] + ",";
                }
            }
            String[] newParams = { newWars, looser.getUniqueId().toString() };
            db.dbUpdate( "UPDATE xd_playerswars SET wars = ? WHERE uuid = ?", 2, newParams );
        } catch (SQLException err){
            err.printStackTrace();
        }

        getServer().broadcastMessage(ChatColor.GREEN + winner.getName() + ChatColor.WHITE + " wins the war! Oponnent: " + ChatColor.DARK_RED + looser.getName());
        winner.sendTitle( ChatColor.GREEN + "Вы одержали победу!", ChatColor.WHITE + "Получена награда: " + ChatColor.BLACK + "16 бедрока", 10, 70, 10 );
        looser.sendTitle( ChatColor.RED + "Вы проиграли войну!", "", 10, 70, 10 );

        ItemStack reward = new ItemStack(Material.BEDROCK, 16);
        winner.getInventory().addItem(reward);

        winner.playSound(winner.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 10, 1);
    }
    public static void warHit(EntityDamageByEntityEvent e, Database db){
        if (e.getDamager() instanceof Player) {
            if (e.getEntityType() == EntityType.PLAYER) {
                Player damager = (Player) e.getDamager();
                Player damaged = (Player) e.getEntity();
                if ( !inWar(db, damager, damaged) ){
                    getServer().broadcastMessage(ChatColor.RED + damager.getName() + ChatColor.WHITE + " hitted " + ChatColor.BLUE + damaged.getName() + ChatColor.WHITE + " without a war status!");
                }
            }
        }
    }
    public static void warKill(PlayerDeathEvent e, Database db){
        if (e.getEntity().getKiller() instanceof Player){
            Player killed = (Player) e.getEntity();
            Player killer = (Player) e.getEntity().getKiller();
            if ( !inWar(db, killer, killed) ){
                getServer().broadcastMessage(ChatColor.RED + killer.getName() + ChatColor.WHITE + ChatColor.BOLD + " KILLED " + ChatColor.BLUE + killed.getName() + ChatColor.WHITE + " without a war status!");
            } else {
                winWar( db, killer, killed );
            }
        }
    }
}
