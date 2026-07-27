package com.ibarnstormer.ibarnorigins;

import com.ibarnstormer.ibarnorigins.client.render.HomingWitherSkullEntityRenderer;
import com.ibarnstormer.ibarnorigins.client.render.SoulFireBallEntityRenderer;
import com.ibarnstormer.ibarnorigins.particle.EmptyParticle;
import com.ibarnstormer.ibarnorigins.particle.SoulBlastParticle;
import com.ibarnstormer.ibarnorigins.particle.SoulMageFlameParticle;
import com.ibarnstormer.ibarnorigins.registry.IOEntities;
import com.ibarnstormer.ibarnorigins.registry.IOParticles;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(modid = IbarnOriginsMain.MODID, value = Dist.CLIENT)
public class IbarnOriginsNeoForgeClient {

    @SubscribeEvent
    public static void init(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(IOEntities.SOUL_FIRE_BALL_ENTITY.get(), SoulFireBallEntityRenderer::new);
        event.registerEntityRenderer(IOEntities.HOMING_WITHER_SKULL_ENTITY.get(), HomingWitherSkullEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerParticles(final RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(IOParticles.SOUL_MAGE_FLAME.get(), SoulMageFlameParticle.Factory::new);
        event.registerSpriteSet(IOParticles.SOUL_BLAST.get(), SoulBlastParticle.Factory::new);
        event.registerSpriteSet(IOParticles.EMPTY_PARTICLE.get(), EmptyParticle.Factory::new);
    }

}