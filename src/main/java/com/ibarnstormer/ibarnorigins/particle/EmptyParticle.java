package com.ibarnstormer.ibarnorigins.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.random.Random;

public class EmptyParticle extends NoRenderParticle {

    public EmptyParticle(ClientWorld world, double $$1, double $$2, double $$3) {
        super(world, $$1, $$2, $$3, 0.0D, 0.0D, 0.0D);
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {

        public Factory(SpriteProvider spriteProvider) {}

        public Particle createParticle(
                SimpleParticleType simpleParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, Random random
        ) {
            return new EmptyParticle(clientWorld, d, e, f);
        }
    }

}