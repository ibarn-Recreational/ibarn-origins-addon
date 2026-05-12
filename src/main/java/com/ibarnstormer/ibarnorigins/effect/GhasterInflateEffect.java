package com.ibarnstormer.ibarnorigins.effect;

import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import com.ibarnstormer.ibarnorigins.registry.IOEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class GhasterInflateEffect extends StatusEffect implements IExtendedStatusEffect {

    public GhasterInflateEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0xffffff);
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        if(entity.getEntityWorld() instanceof ServerWorld serverWorld) {
            serverWorld.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_PUFFER_FISH_BLOW_UP, SoundCategory.PLAYERS, 1.25f, 0.5F);
            entity.addVelocity(0, 0.5F, 0);
            entity.velocityDirty = true;
        }

        if(entity instanceof IbarnOriginsEntity ioe) {
            ioe.setInflated(true);
        }
    }

    @Override
    public void onStatusEffectRemoved(ServerWorld world, LivingEntity entity, int amplifier) {
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.PLAYERS, 1.25f, 0.5F);

        if(entity instanceof IbarnOriginsEntity ioe) {
            ioe.setInflated(false);
        }
    }
}