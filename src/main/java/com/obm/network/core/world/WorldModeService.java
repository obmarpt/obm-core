package com.obm.network.core.world;

import com.obm.network.core.OBMCorePlugin;

import java.util.List;

public class WorldModeService {

    private final List<String> smpWorlds;
    private final List<String> uhcWorlds;

    public WorldModeService() {
        this.smpWorlds = OBMCorePlugin.get().getConfig().getStringList("smp-worlds");
        this.uhcWorlds = OBMCorePlugin.get().getConfig().getStringList("uhc-worlds");
    }

    public boolean isSMP(String worldName) {
        return smpWorlds.contains(worldName);
    }

    public boolean isUHC(String worldName) {
        return uhcWorlds.contains(worldName);
    }
}