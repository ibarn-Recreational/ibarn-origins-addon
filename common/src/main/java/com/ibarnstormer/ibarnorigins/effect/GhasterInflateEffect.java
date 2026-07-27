package com.ibarnstormer.ibarnorigins.effect;

import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class GhasterInflateEffect extends MobEffect implements IExtendedStatusEffect {

    public GhasterInflateEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xffffff);
    }

    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {
        if(entity.level() instanceof ServerLevel serverWorld) {
            serverWorld.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.PUFFER_FISH_BLOW_UP, SoundSource.PLAYERS, 1.25f, 0.5F);
            entity.push(0, 0.5F, 0);
            entity.hasImpulse = true;
        }

        if(entity instanceof IbarnOriginsEntity ioe) {
            ioe.setInflated(true);
        }
    }

    @Override
    public void onStatusEffectRemoved(ServerLevel world, LivingEntity entity, int amplifier) {
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.LAVA_EXTINGUISH, SoundSource.PLAYERS, 1.25f, 0.5F);

        if(entity instanceof IbarnOriginsEntity ioe) {
            ioe.setInflated(false);
        }
    }
}