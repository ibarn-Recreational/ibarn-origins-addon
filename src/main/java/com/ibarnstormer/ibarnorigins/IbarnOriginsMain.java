package com.ibarnstormer.ibarnorigins;

import net.minecraft.resources.Identifier;

public class IbarnOriginsMain {
	
    public static final String MODID = "ibarnorigins";

    public static void init() {}

    public static Identifier IOIdentifier(String name) {
        return Identifier.fromNamespaceAndPath(MODID, name);
    }

}
