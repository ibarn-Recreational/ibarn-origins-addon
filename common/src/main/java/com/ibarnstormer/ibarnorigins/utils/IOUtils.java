package com.ibarnstormer.ibarnorigins.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.world.World;

public class IOUtils {

    public static void renderParticles(World world, LivingEntity entity, ParticleEffect particleEffect) {
        if(world instanceof ClientWorld clientWorld) {
            boolean render = (entity instanceof ClientPlayerEntity player && MinecraftClient.getInstance().player == player && !(MinecraftClient.getInstance().options.getPerspective().isFirstPerson() && MinecraftClient.getInstance().cameraEntity == player)) ||
                    (entity instanceof ClientPlayerEntity player2 && MinecraftClient.getInstance().player != player2) ||
                    !(entity instanceof ClientPlayerEntity);

            if(render) clientWorld.addParticle(particleEffect, entity.getParticleX(0.5), entity.getRandomBodyY(), entity.getParticleZ(0.5), 0, 0, 0);
        }
    }

}
