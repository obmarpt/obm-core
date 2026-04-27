package com.obm.network.core;

import org.bukkit.plugin.java.JavaPlugin;

public class OBMCorePlugin extends JavaPlugin {

    private static OBMCorePlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getLogger().info("✅ OBM-Core iniciado");
    }

    @Override
    public void onDisable() {
        getLogger().info("⛔ OBM-Core desligado");
    }

    public static OBMCorePlugin get() {
        return instance;
    }
}