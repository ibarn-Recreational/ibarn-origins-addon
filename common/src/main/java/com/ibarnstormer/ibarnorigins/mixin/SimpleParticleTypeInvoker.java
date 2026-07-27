package com.ibarnstormer.ibarnorigins.mixin;

import net.minecraft.core.particles.SimpleParticleType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SimpleParticleType.class)
public interface SimpleParticleTypeInvoker {

    @Invoker("<init>")
    static SimpleParticleType init(boolean overrideLimiter) {
        throw new IllegalStateException();
    }

}
