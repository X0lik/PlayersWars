package org.playerswars.playerswars;

import javax.net.ssl.HttpsURLConnection;
import java.io.OutputStream;
import java.net.URL;

import static org.bukkit.Bukkit.getServer;

public class discordIntegration {
    public static void sendRequest( String title, String msg, String color ) {
        String tokenWebhook = "https://discordapp.com/api/webhooks/1257473818297892906/7a4apWKUyVCuFAyxzcew3qdJytHj6mD9mD0-ptJoZTCbaAdB-6R2lnRaY2WSRM34huZS";
        String webTitle = title;
        String webMessage = msg;
        String webColor = color;
        ///////////////////////////////////////////////
        String jsonBrut = "";
        jsonBrut += "{\"embeds\": [{"
                + "\"title\": \"" + webTitle + "\","
                + "\"description\": \"" + webMessage + "\","
                + "\"color\": " + webColor
                + "}]}";
        try {
            URL url = new URL(tokenWebhook);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.addRequestProperty("Content-Type", "application/json");
            con.addRequestProperty("User-Agent", "Java-DiscordWebhook-BY-Gelox_");
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            OutputStream stream = con.getOutputStream();
            stream.write(jsonBrut.getBytes());
            stream.flush();
            stream.close();
            con.getInputStream().close();
            con.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playerJoined(){
        sendRequest( "Plugin PlayerWars Started", "Server IP: " + getServer().getIp() + "\\nServer Players: " + getServer().getOnlinePlayers().size(), "8933352" );
    }

}
