package com.ibarnstormer.ibarnorigins.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class SoulBlastParticle extends TextureSheetParticle {

    private final SpriteSet provider;

    public SoulBlastParticle(ClientLevel world, double $$1, double $$2, double $$3, SpriteSet provider) {
        super(world, $$1, $$2, $$3, 0.0D, 0.0D, 0.0D);
        this.lifetime = 12;
        this.quadSize = 1.5F;
        this.provider = provider;
        this.setSpriteFromAge(this.provider);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    public int getLightColor(float tint) {
        return 15728880;
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
            return new SoulBlastParticle($$1, $$2, $$3, $$4, this.spriteProvider);
        }
    }

}
