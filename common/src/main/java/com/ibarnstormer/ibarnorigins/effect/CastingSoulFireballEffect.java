package com.ibarnstormer.ibarnorigins.effect;

import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import com.ibarnstormer.ibarnorigins.entity.SoulFireBallEntity;
import com.ibarnstormer.ibarnorigins.registry.IOSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;

public class CastingSoulFireballEffect extends StatusEffect {

    private final EntityAttributeModifier MOVEMENT_MODIFIER = new EntityAttributeModifier("casting_slowdown", -0.5D, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);

    public CastingSoulFireballEffect() {
        super(StatusEffectCategory.NEUTRAL, 0xffffff);
    }

    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {

        if(entity.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.playSound(null, entity.getX(), entity.getY(), entity.getZ(), IOSounds.KI_BLAST_FIRE.get(), SoundCategory.PLAYERS, 1.25f, 1);

            SoulFireBallEntity fireball = new SoulFireBallEntity(entity, entity.getRotationVector().x, entity.getRotationVector().y, entity.getRotationVector().z, serverWorld);
            fireball.setPos(entity.getX(), entity.getEyeY(), entity.getZ());

            serverWorld.spawnEntity(fireball);
        }

        EntityAttributeInstance movement = entity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if(movement != null) if(movement.hasModifier(MOVEMENT_MODIFIER)) movement.removeModifier(MOVEMENT_MODIFIER);
    }

    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {

        if(entity.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.playSound(null, entity.getX(), entity.getY(), entity.getZ(), IOSounds.KI_BLAST_CHARGE.get(), SoundCategory.PLAYERS, 1.25f, 1);
        }

        if(entity instanceof IbarnOriginsEntity spellCaster && entity.getStatusEffect(this) != null) spellCaster.setSpellCastTicks(entity.getStatusEffect(this).getDuration());

        EntityAttributeInstance movement = entity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if(movement != null && !movement.hasModifier(MOVEMENT_MODIFIER))  movement.addTemporaryModifier(MOVEMENT_MODIFIER);
    }


}
