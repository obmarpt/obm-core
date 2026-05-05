package com.obm.network.core.time;

import com.obm.network.core.OBMCorePlugin;
import com.obm.network.core.storage.DataStore;
import com.obm.network.core.world.WorldModeService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TimeTrackerPerWorld {

    private final Map<UUID, Long> lastSeen = new HashMap<>();
    private final DataStore dataStore;
    private final WorldModeService worldModeService;

    public TimeTrackerPerWorld() {
        this.dataStore = OBMCorePlugin.getDataStore();
        this.worldModeService = new WorldModeService();

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    track(player);
                }
            }
        }.runTaskTimerAsynchronously(OBMCorePlugin.get(), 20L, 20L);
    }

    private void track(Player player) {
        UUID uuid = player.getUniqueId();
        long now = System.currentTimeMillis();

        if (!lastSeen.containsKey(uuid)) {
            lastSeen.put(uuid, now);
            return;
        }

        long diff = now - lastSeen.get(uuid);
        lastSeen.put(uuid, now);

        String worldName = player.getWorld().getName();

        if (worldModeService.isSMP(worldName)) {
            dataStore.add(uuid, "smp.playtime_ms", diff);
        } else if (worldModeService.isUHC(worldName)) {
            dataStore.add(uuid, "uhc.playtime_ms", diff);
        }
    }
}