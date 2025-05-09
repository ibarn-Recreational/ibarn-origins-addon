package com.ibarnstormer.ibarnorigins.effect;

import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class GhasterInflateEffect extends StatusEffect {

    public GhasterInflateEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0xffffff);
    }

    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if(entity.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_PUFFER_FISH_BLOW_UP, SoundCategory.PLAYERS, 1.25f, 0.5F);
            entity.addVelocity(0, 0.5F, 0);
            entity.velocityModified = true;
        }
    }

    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if(entity.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.PLAYERS, 1.25f, 0.5F);
        }
    }
}