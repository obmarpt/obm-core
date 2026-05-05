package com.obm.network.core.combat;

import com.obm.network.core.OBMCorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CombatLogService implements Listener {

    private static CombatLogService instance;

    private final Map<UUID, Long> combatMap = new HashMap<>();
    private final long timeout;

    public CombatLogService(Plugin plugin) {
        instance = this;
        this.timeout = OBMCorePlugin.get()
                .getConfig()
                .getLong("combatlog.timeout", 10000);

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    /* -----------------------------------
     * API pública
     * ----------------------------------- */

    public static boolean isInCombat(Player player) {
        if (instance == null) return false;

        UUID uuid = player.getUniqueId();
        Long last = instance.combatMap.get(uuid);
        if (last == null) return false;

        return System.currentTimeMillis() - last < instance.timeout;
    }

    public static void clearCombat(Player player) {
        if (instance != null) {
            instance.combatMap.remove(player.getUniqueId());
        }
    }

    /* -----------------------------------
     * Events
     * ----------------------------------- */

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof Player victim)) return;

        Player attacker = null;

        if (event.getDamager() instanceof Player p) {
            attacker = p;
        }

        if (attacker == null) return;

        long now = System.currentTimeMillis();

        combatMap.put(victim.getUniqueId(), now);
        combatMap.put(attacker.getUniqueId(), now);
    }
}
