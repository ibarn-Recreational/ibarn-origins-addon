package com.ibarnstormer.ibarnorigins.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class EmptyParticle extends TextureSheetParticle {

    private final SpriteSet provider;

    public EmptyParticle(ClientLevel world, double $$1, double $$2, double $$3, SpriteSet provider) {
        super(world, $$1, $$2, $$3, 0.0D, 0.0D, 0.0D);
        this.lifetime = 2;
        this.quadSize = 0.01F;
        this.provider = provider;
        this.setSpriteFromAge(this.provider);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.NO_RENDER;
    }

    public int getLightColor(float tint) {
        return 0;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.provider);
        }
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;

        public Factory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(SimpleParticleType $$0, ClientLevel $$1, double $$2, double $$3, double $$4, double $$5, double $$6, double $$7) {
            return new EmptyParticle($$1, $$2, $$3, $$4, this.spriteProvider);
        }
    }

}