package com.ibarnstormer.ibarnorigins;

import net.minecraft.util.Identifier;

public class IbarnOriginsMain {
	
    public static final String MODID = "ibarnorigins";

    public static void init() {}

    public static Identifier IOIdentifier(String name) {
        return new Identifier(MODID, name);
    }

}
