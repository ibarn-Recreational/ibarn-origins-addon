package com.ibarnstormer.ibarnorigins.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class IOUtils {

    public static void renderParticles(Level world, LivingEntity entity, ParticleOptions particleEffect) {
        if(world instanceof ClientLevel clientWorld) {
            boolean render = (entity instanceof LocalPlayer player && Minecraft.getInstance().player == player && !(Minecraft.getInstance().options.getCameraType().isFirstPerson() && Minecraft.getInstance().cameraEntity == player)) ||
                    (entity instanceof LocalPlayer player2 && Minecraft.getInstance().player != player2) ||
                    !(entity instanceof LocalPlayer);

            if(render) clientWorld.addParticle(particleEffect, entity.getRandomX(0.5), entity.getRandomY(), entity.getRandomZ(0.5), 0, 0, 0);
        }
    }

}
