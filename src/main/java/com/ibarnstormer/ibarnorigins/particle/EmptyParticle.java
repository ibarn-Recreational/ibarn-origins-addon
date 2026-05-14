package com.ibarnstormer.ibarnorigins.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class EmptyParticle extends NoRenderParticle {

    public EmptyParticle(ClientLevel world, double $$1, double $$2, double $$3) {
        super(world, $$1, $$2, $$3, 0.0D, 0.0D, 0.0D);
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {

        public Factory(SpriteSet spriteProvider) {}

        public Particle createParticle(
                SimpleParticleType simpleParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i, RandomSource random
        ) {
            return new EmptyParticle(clientWorld, d, e, f);
        }
    }

}