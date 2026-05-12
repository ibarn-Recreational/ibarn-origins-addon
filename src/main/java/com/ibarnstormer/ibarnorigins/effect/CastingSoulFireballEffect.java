package com.ibarnstormer.ibarnorigins.effect;

import com.ibarnstormer.ibarnorigins.IbarnOriginsMain;
import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import com.ibarnstormer.ibarnorigins.entity.SoulFireBallEntity;
import com.ibarnstormer.ibarnorigins.registry.IOEffects;
import com.ibarnstormer.ibarnorigins.registry.IOSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;

public class CastingSoulFireballEffect extends StatusEffect implements IExtendedStatusEffect {

    private final Identifier ID = IbarnOriginsMain.IOIdentifier("casting_slowdown");
    private final EntityAttributeModifier MOVEMENT_MODIFIER = new EntityAttributeModifier(ID, -0.5D, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    public CastingSoulFireballEffect() {
        super(StatusEffectCategory.NEUTRAL, 0xffffff);
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {

        if(entity.getEntityWorld() instanceof ServerWorld serverWorld) {
            serverWorld.playSound(null, entity.getX(), entity.getY(), entity.getZ(), IOSounds.KI_BLAST_CHARGE.get(), SoundCategory.PLAYERS, 1.25f, 1);
        }

        if(entity instanceof IbarnOriginsEntity spellCaster && entity.hasStatusEffect(IOEffects.CASTING_SOUL_FIREBALL.getRef())) spellCaster.setSpellCastTicks(entity.getStatusEffect(IOEffects.CASTING_SOUL_FIREBALL.getRef()).getDuration());

        EntityAttributeInstance movement = entity.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED);
        if(movement != null && !movement.hasModifier(ID))  movement.addTemporaryModifier(MOVEMENT_MODIFIER);

        super.onApplied(entity, amplifier);
    }


    @Override
    public void onStatusEffectRemoved(ServerWorld world, LivingEntity entity, int amplifier) {
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), IOSounds.KI_BLAST_FIRE.get(), SoundCategory.PLAYERS, 1.25f, 1);

        SoulFireBallEntity fireball = new SoulFireBallEntity(entity, entity.getRotationVector(), entity.getEntityWorld());
        fireball.setPos(entity.getX(), entity.getEyeY(), entity.getZ());

        world.spawnEntity(fireball);

        EntityAttributeInstance movement = entity.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED);
        if(movement != null) if(movement.hasModifier(ID)) movement.removeModifier(MOVEMENT_MODIFIER);
    }
}
