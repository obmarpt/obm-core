package com.obm.network.core.storage;

import com.obm.network.core.OBMCorePlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * DataStore do OBM-Core
 *
 * Responsável por guardar dados persistentes dos jogadores em data.yml
 * Estrutura:
 * players.<uuid>.<key>
 */
public class DataStore {

    private final OBMCorePlugin plugin;
    private final File file;
    private FileConfiguration config;

    public DataStore(OBMCorePlugin plugin) {
        this.plugin = plugin;

        // plugins/OBM-Core/data.yml
        this.file = new File(plugin.getDataFolder(), "data.yml");

        if (!file.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("❌ Erro ao criar data.yml");
                e.printStackTrace();
            }
        }

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    /* --------------------------------------------------
     * Helpers
     * -------------------------------------------------- */

    private String path(UUID uuid, String key) {
        return "players." + uuid + "." + key;
    }

    /* --------------------------------------------------
     * Getters
     * -------------------------------------------------- */

    public long getLong(UUID uuid, String key) {
        return config.getLong(path(uuid, key));
    }

    public int getInt(UUID uuid, String key) {
        return config.getInt(path(uuid, key));
    }

    public String getString(UUID uuid, String key) {
        return config.getString(path(uuid, key));
    }

    public boolean getBoolean(UUID uuid, String key) {
        return config.getBoolean(path(uuid, key));
    }

    /* --------------------------------------------------
     * Setters
     * -------------------------------------------------- */

    public void set(UUID uuid, String key, Object value) {
        config.set(path(uuid, key), value);
        save();
    }

    public void add(UUID uuid, String key, long value) {
        long current = getLong(uuid, key);
        set(uuid, key, current + value);
    }

    public void increment(UUID uuid, String key) {
        add(uuid, key, 1);
    }

    /* --------------------------------------------------
     * Save / Reload
     * -------------------------------------------------- */

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("❌ Erro ao guardar data.yml");
            e.printStackTrace();
        }
    }

    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getYaml() {
        return config;
    }
}