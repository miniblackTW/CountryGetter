package me.miniblacktw.countrygetter;

import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.*;
import org.bukkit.entity.Player;

public class PlayerListener implements Listener {

    private final CountryGetter plugin;

    public PlayerListener(CountryGetter plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        plugin.fetchCountry(p);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        plugin.removePlayerInfo(e.getPlayer());
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent e) {
        if (!(e.getRightClicked() instanceof ArmorStand)) {
            ArmorStand stand = (ArmorStand) e.getRightClicked();
            if (stand.getHelmet() != null && stand.getHelmet().getType() == Material.SKULL_ITEM) {
                e.setCancelled(true);
            }
        }
    }
}