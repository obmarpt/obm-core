package com.obm.network.core.papi;

import com.obm.network.core.OBMCorePlugin;
import com.obm.network.core.combat.CombatLogService;
import com.obm.network.core.storage.DataStore;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class OBMExpansion extends PlaceholderExpansion {

    private final DataStore dataStore;

    public OBMExpansion() {
        this.dataStore = OBMCorePlugin.getDataStore();
    }

    @Override
    public String getIdentifier() {
        return "obm";
    }

    @Override
    public String getAuthor() {
        return "Obmar";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {

        if (player == null) return "";

        // ⏱️ TEMPO
        if (params.equalsIgnoreCase("time_total")) {
            return String.valueOf(
                    dataStore.getLong(player.getUniqueId(), "time.total_ms") / 1000
            );
        }

        if (params.equalsIgnoreCase("smp_playtime")) {
            return String.valueOf(
                    dataStore.getLong(player.getUniqueId(), "smp.playtime_ms") / 1000
            );
        }

        if (params.equalsIgnoreCase("uhc_playtime")) {
            return String.valueOf(
                    dataStore.getLong(player.getUniqueId(), "uhc.playtime_ms") / 1000
            );
        }

        // ⚔️ COMBATE
        if (params.equalsIgnoreCase("in_combat")) {
            return CombatLogService.isInCombat(player) ? "yes" : "no";
        }

        return null;
    }
}