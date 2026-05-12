package com.ibarnstormer.ibarnorigins.registry;

import com.ibarnstormer.ibarnorigins.IbarnOriginsMain;
import com.ibarnstormer.ibarnorigins.registry.utils.IORegisterWrapper;
import com.ibarnstormer.ibarnorigins.registry.utils.RegistryObjectWrapper;
import net.minecraft.particle.SimpleParticleType;

public class IOParticles {

    public static RegistryObjectWrapper<SimpleParticleType> SOUL_MAGE_FLAME;
    public static RegistryObjectWrapper<SimpleParticleType> EMPTY_PARTICLE;

    @SuppressWarnings("unchecked")
    public static void init(IORegisterWrapper register) {
        SOUL_MAGE_FLAME = register.register(IbarnOriginsMain.IOIdentifier("soul_mage_fire_flame"), () -> new SimpleParticleType(false));
        EMPTY_PARTICLE = register.register(IbarnOriginsMain.IOIdentifier("empty_particle"), () -> new SimpleParticleType(false));
    }

}
