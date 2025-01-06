package com.ibarnstormer.ibarnorigins.forge;

import com.ibarnstormer.ibarnorigins.IOExpectPlatform;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class IOForgeExpectPlatformImpl {
    /**
     * This is our actual method to {@link IOExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
