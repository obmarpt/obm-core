package com.obm.network.core.location;

import com.obm.network.core.OBMCorePlugin;
import com.obm.network.core.storage.DataStore;
import com.obm.network.core.world.WorldModeService;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LastLocationTracker implements Listener {

    private final DataStore dataStore;
    private final WorldModeService worldModeService;

    public LastLocationTracker() {
        this.dataStore = OBMCorePlugin.getDataStore();
        this.worldModeService = new WorldModeService();

        Bukkit.getPluginManager().registerEvents(this, OBMCorePlugin.get());
    }

    /* -----------------------------------
     * Events
     * ----------------------------------- */

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        saveLocation(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        saveLocation(player);

        long now = System.currentTimeMillis();
        dataStore.set(player.getUniqueId(), "last_quit_time", now);
    }

    /* -----------------------------------
     * Logic
     * ----------------------------------- */

    private void saveLocation(Player player) {
        Location loc = player.getLocation();
        String worldName = loc.getWorld().getName();

        if (worldModeService.isSMP(worldName)) {
            save(player, "last_smp", loc);
        } else if (worldModeService.isUHC(worldName)) {
            save(player, "last_uhc", loc);
        }

        // Sempre guarda última posição global
        save(player, "last_quit", loc);
    }

    private void save(Player player, String prefix, Location loc) {
        dataStore.set(player.getUniqueId(), prefix + "_world", loc.getWorld().getName());
        dataStore.set(player.getUniqueId(), prefix + "_x", loc.getX());
        dataStore.set(player.getUniqueId(), prefix + "_y", loc.getY());
        dataStore.set(player.getUniqueId(), prefix + "_z", loc.getZ());
    }
}