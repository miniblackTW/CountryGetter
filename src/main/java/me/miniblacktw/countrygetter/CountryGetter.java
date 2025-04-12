package me.miniblacktw.countrygetter;

import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.*;

public class CountryGetter extends JavaPlugin {

    public class PlayerInfo {
        public String ip;
        public boolean isProxy;
        public ArmorStand armorStand;

        public PlayerInfo(String ip, boolean isProxy, ArmorStand armorStand) {
            this.ip = ip;
            this.isProxy = isProxy;
            this.armorStand = armorStand;
        }
    }

    public Map<UUID, PlayerInfo> playerInfoMap = new HashMap<>();
    public Map<UUID, String> countryPrefixMap = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("Enabled CountryGetter v1.0");

        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
        //Bukkit.getPluginManager().registerEvents(new ChatListener(this), this);
        this.getCommand("cg").setExecutor(new CGCommand(this));
    }

    public void setTabName(Player p, String suffix) {
        new BukkitRunnable() {
            @Override
            public void run() {
                p.setPlayerListName(p.getName() + " | " + suffix);
            }
        }.runTask(this);
    }

    public void saveCountryPrefix(Player p, String prefix) {
        countryPrefixMap.put(p.getUniqueId(), prefix);
    }

    public String getCountryPrefix(Player p) {
        return countryPrefixMap.getOrDefault(p.getUniqueId(), "");
    }

    public void savePlayerInfo(Player p, String ip, boolean isProxy, ArmorStand armorStand) {
        playerInfoMap.put(p.getUniqueId(), new PlayerInfo(ip, isProxy, armorStand));
    }

    public PlayerInfo getPlayerInfo(Player p) {
        return playerInfoMap.get(p.getUniqueId());
    }

    public void removePlayerInfo(Player p) {
        PlayerInfo info = getPlayerInfo(p);
        if (info != null && info.armorStand != null) {
            info.armorStand.remove();
        }
        playerInfoMap.remove(p.getUniqueId());
        countryPrefixMap.remove(p.getUniqueId());
    }

    public void fetchCountry(Player p) {
        String ip = ((InetSocketAddress) p.getAddress()).getAddress().getHostAddress();

        new Thread(() -> {
            try {
                URL url = new URL("http://ip-api.com/json/" + ip + "?fields=status,country,countryCode,proxy");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.connect();

                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                JSONParser parser = new JSONParser();
                JSONObject response = (JSONObject) parser.parse(reader);

                String status = (String) response.get("status");
                if (!status.equals("success")) return;

                String country = (String) response.get("country");
                String code = (String) response.get("countryCode");
                boolean isProxy = (Boolean) response.get("proxy");

                String flag = getCountryFlag(code);
                String display = flag + " " + country;
                String suffix = display + (isProxy ? " | Proxy/VPN" : "");
                String armorText = ChatColor.AQUA + display + (isProxy ? ChatColor.DARK_GRAY + " | " + ChatColor.YELLOW + "Proxy/VPN" : "");

                saveCountryPrefix(p, suffix);

                Bukkit.getScheduler().runTask(this, () -> {
                    setTabName(p, suffix);

                    Location loc = p.getLocation().add(0, 1.5, 0);
                    ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
                    SkullMeta meta = (SkullMeta) skull.getItemMeta();

                    meta.setOwner("Meteor49");
                    skull.setItemMeta(meta);
                    ArmorStand stand = p.getWorld().spawn(loc, ArmorStand.class);
                    stand.setVisible(false);
                    stand.setCustomNameVisible(true);
                    stand.setCustomName(armorText);
                    stand.setMarker(true);
                    stand.setGravity(false);
                    stand.setSmall(false);
                    stand.setHelmet(skull);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (!p.isOnline() || stand.isDead()) {
                                stand.remove();
                                cancel();
                                return;
                            }
                            stand.teleport(p.getLocation().add(0, 2.2, 0));
                        }
                    }.runTaskTimer(this, 0L, 1L);

                    savePlayerInfo(p, ip, isProxy, stand);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public String getCountryFlag(String code) {
        if (code == null || code.length() != 2) return "";
        code = code.toUpperCase();
        return String.valueOf((char)(code.charAt(0) + 127397)) + String.valueOf((char)(code.charAt(1) + 127397));
    }
}
