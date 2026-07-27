package com.ibarnstormer.ibarnorigins.registry;

import com.ibarnstormer.ibarnorigins.IbarnOriginsMain;
import com.ibarnstormer.ibarnorigins.mixin.SimpleParticleTypeInvoker;
import com.ibarnstormer.ibarnorigins.registry.utils.IORegisterWrapper;
import com.ibarnstormer.ibarnorigins.registry.utils.RegistryObjectWrapper;
import net.minecraft.core.particles.SimpleParticleType;

public class IOParticles {

    public static RegistryObjectWrapper<SimpleParticleType> SOUL_BLAST;
    public static RegistryObjectWrapper<SimpleParticleType> SOUL_MAGE_FLAME;
    public static RegistryObjectWrapper<SimpleParticleType> EMPTY_PARTICLE;

    @SuppressWarnings("unchecked")
    public static void init(IORegisterWrapper register) {
        SOUL_BLAST = register.register(IbarnOriginsMain.IOIdentifier("soul_blast_particle"), () -> SimpleParticleTypeInvoker.init(false));
        SOUL_MAGE_FLAME = register.register(IbarnOriginsMain.IOIdentifier("soul_mage_fire_flame"), () -> SimpleParticleTypeInvoker.init(false));
        EMPTY_PARTICLE = register.register(IbarnOriginsMain.IOIdentifier("empty_particle"), () -> SimpleParticleTypeInvoker.init(false));
    }

}
