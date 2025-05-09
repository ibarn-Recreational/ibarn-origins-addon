package com.ibarnstormer.ibarnorigins.forge;

import com.ibarnstormer.ibarnorigins.IbarnOriginsMain;
import com.ibarnstormer.ibarnorigins.client.render.HomingWitherSkullEntityRenderer;
import com.ibarnstormer.ibarnorigins.client.render.SoulFireBallEntityRenderer;
import com.ibarnstormer.ibarnorigins.particle.EmptyParticle;
import com.ibarnstormer.ibarnorigins.particle.SoulBlastParticle;
import com.ibarnstormer.ibarnorigins.particle.SoulMageFlameParticle;
import com.ibarnstormer.ibarnorigins.registry.IOEntities;
import com.ibarnstormer.ibarnorigins.registry.IOParticles;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = IbarnOriginsMain.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class IbarnOriginsForgeClient {

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