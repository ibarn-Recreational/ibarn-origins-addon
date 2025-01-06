package com.ibarnstormer.ibarnorigins.registry;

import com.ibarnstormer.ibarnorigins.IbarnOriginsMain;
import com.ibarnstormer.ibarnorigins.registry.utils.IORegisterWrapper;
import com.ibarnstormer.ibarnorigins.registry.utils.RegistryObjectWrapper;
import net.minecraft.particle.DefaultParticleType;

public class IOParticles {

    public static RegistryObjectWrapper<DefaultParticleType> SOUL_BLAST;
    public static RegistryObjectWrapper<DefaultParticleType> SOUL_MAGE_FLAME;

    @SuppressWarnings("unchecked")
    public static void init(IORegisterWrapper register) {
        SOUL_BLAST = register.register(IbarnOriginsMain.IOIdentifier("soul_blast_particle"), () -> new DefaultParticleType(false));
        SOUL_MAGE_FLAME = register.register(IbarnOriginsMain.IOIdentifier("soul_mage_fire_flame"), () -> new DefaultParticleType(false));
    }

}
