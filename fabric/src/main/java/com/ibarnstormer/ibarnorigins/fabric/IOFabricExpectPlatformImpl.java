package com.ibarnstormer.ibarnorigins.fabric;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class IOFabricExpectPlatformImpl {

    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
