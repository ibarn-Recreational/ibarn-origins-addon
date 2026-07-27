package com.ibarnstormer.ibarnorigins;

import net.minecraft.resources.ResourceLocation;

public class IbarnOriginsMain {
	
    public static final String MODID = "ibarnorigins";

    public static void init() {}

    public static ResourceLocation IOIdentifier(String name) {
        return ResourceLocation.fromNamespaceAndPath(MODID, name);
    }

}
