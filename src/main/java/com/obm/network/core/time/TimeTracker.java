package com.obm.network.core.time;

import com.obm.network.core.OBMCorePlugin;
import com.obm.network.core.storage.DataStore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TimeTracker {

    private final Map<UUID, Long> lastSeen = new HashMap<>();
    private final DataStore dataStore;

    public TimeTracker() {
        this.dataStore = OBMCorePlugin.getDataStore();

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

        // ✅ Incrementar tempo total
        dataStore.add(uuid, "time.total_ms", diff);
    }
}