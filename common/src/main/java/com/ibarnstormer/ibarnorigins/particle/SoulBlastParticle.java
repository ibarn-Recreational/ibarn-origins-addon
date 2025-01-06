package com.ibarnstormer.ibarnorigins.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class SoulBlastParticle extends SpriteBillboardParticle {

    private final SpriteProvider provider;

    public SoulBlastParticle(ClientWorld world, double $$1, double $$2, double $$3, SpriteProvider provider) {
        super(world, $$1, $$2, $$3, 0.0D, 0.0D, 0.0D);
        this.maxAge = 12;
        this.scale = 1.5F;
        this.provider = provider;
        this.setSpriteForAge(this.provider);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_LIT;
    }

    public int getBrightness(float tint) {
        return 15728880;
    }

    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge) {
            this.markDead();
        } else {
            this.setSpriteForAge(this.provider);
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType $$0, ClientWorld $$1, double $$2, double $$3, double $$4, double $$5, double $$6, double $$7) {
            return new SoulBlastParticle($$1, $$2, $$3, $$4, this.spriteProvider);
        }
    }

}
