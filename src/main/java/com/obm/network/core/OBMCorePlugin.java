package com.obm.network.core;

import com.obm.network.core.combat.CombatLogService;
import com.obm.network.core.location.LastLocationTracker;
import com.obm.network.core.storage.DataStore;
import com.obm.network.core.time.TimeTracker;
import com.obm.network.core.time.TimeTrackerPerWorld;

import org.bukkit.plugin.java.JavaPlugin;

public class OBMCorePlugin extends JavaPlugin {

    private static OBMCorePlugin instance;
    private DataStore dataStore;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        dataStore = new DataStore(this);

        // ⏱️ Tempo
        new TimeTracker();
        new TimeTrackerPerWorld();

        // ⚔️ CombatLog
        new CombatLogService(this);

        // 📍 Localização
        new LastLocationTracker();

        getLogger().info("✅ OBM-Core iniciado (Time, Combat, Location ativos)");
    }

    @Override
    public void onDisable() {
        if (dataStore != null) {
            dataStore.save();
        }
        getLogger().info("⛔ OBM-Core desligado");
    }

    public static OBMCorePlugin get() {
        return instance;
    }

    public static DataStore getDataStore() {
        return instance.dataStore;
    }
}