package com.ibarnstormer.ibarnorigins;

import com.ibarnstormer.ibarnorigins.client.render.HomingWitherSkullEntityRenderer;
import com.ibarnstormer.ibarnorigins.client.render.SoulFireBallEntityRenderer;
import com.ibarnstormer.ibarnorigins.particle.EmptyParticle;
import com.ibarnstormer.ibarnorigins.particle.SoulMageFlameParticle;
import com.ibarnstormer.ibarnorigins.registry.IOEntities;
import com.ibarnstormer.ibarnorigins.registry.IOParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

@Environment(EnvType.CLIENT)
public class IbarnOriginsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Renderers
        EntityRendererRegistry.register(IOEntities.SOUL_FIRE_BALL_ENTITY.get(), SoulFireBallEntityRenderer::new);
        EntityRendererRegistry.register(IOEntities.HOMING_WITHER_SKULL_ENTITY.get(), HomingWitherSkullEntityRenderer::new);

        // Particle Renderers
        ParticleProviderRegistry.getInstance().register(IOParticles.SOUL_MAGE_FLAME.get(), SoulMageFlameParticle.Factory::new);
        ParticleProviderRegistry.getInstance().register(IOParticles.EMPTY_PARTICLE.get(), EmptyParticle.Factory::new);
    }
}
