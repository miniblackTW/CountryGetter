package me.miniblacktw.countrygetter;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.entity.Player;

public class ChatListener implements Listener {

    private final CountryGetter plugin;

    public ChatListener(CountryGetter plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String prefix = plugin.getCountryPrefix(p);
        e.setFormat(prefix + p.getName() + ": " + e.getMessage());
    }
}