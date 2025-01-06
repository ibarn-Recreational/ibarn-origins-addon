package com.ibarnstormer.ibarnorigins.fabric;

import com.ibarnstormer.ibarnorigins.client.render.KiBlastProjectileEntityRenderer;
import com.ibarnstormer.ibarnorigins.client.render.SoulFireBallEntityRenderer;
import com.ibarnstormer.ibarnorigins.particle.SoulBlastParticle;
import com.ibarnstormer.ibarnorigins.particle.SoulMageFlameParticle;
import com.ibarnstormer.ibarnorigins.registry.IOEntities;
import com.ibarnstormer.ibarnorigins.registry.IOParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.entity.WitherSkullEntityRenderer;
import net.minecraft.network.PacketByteBuf;

@Environment(EnvType.CLIENT)
public class IbarnOriginsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        //Renderers
        EntityRendererRegistry.register(IOEntities.SOUL_FIRE_BALL_ENTITY.get(), SoulFireBallEntityRenderer::new);
        EntityRendererRegistry.register(IOEntities.HOMING_WITHER_SKULL_ENTITY.get(), WitherSkullEntityRenderer::new);
        EntityRendererRegistry.register(IOEntities.KI_BLAST_PROJECTILE_ENTITY.get(), KiBlastProjectileEntityRenderer::new);

        //Particle Renderers
        ParticleFactoryRegistry.getInstance().register(IOParticles.SOUL_BLAST.get(), SoulBlastParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(IOParticles.SOUL_MAGE_FLAME.get(), SoulMageFlameParticle.Factory::new);

        //Spawning projectiles
        ClientPlayNetworking.registerGlobalReceiver(IOEntities.SOUL_FB_ID, (MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf buffer, PacketSender sender) -> {});
        ClientPlayNetworking.registerGlobalReceiver(IOEntities.FIXED_WS_ID, (MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf buffer, PacketSender sender) -> {});
        ClientPlayNetworking.registerGlobalReceiver(IOEntities.KI_BLAST_ID, (MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf buffer, PacketSender sender) -> {});
    }
}
