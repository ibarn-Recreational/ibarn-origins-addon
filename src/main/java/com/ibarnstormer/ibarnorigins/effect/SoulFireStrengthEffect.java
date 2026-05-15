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

import java.util.UUID;

public class SoulFireStrengthEffect extends StatusEffect implements IExtendedStatusEffect {

    private final Identifier ID = IbarnOriginsMain.IOIdentifier("soul_fire_strength");

    public SoulFireStrengthEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0x1f70f2);
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        EntityAttributeInstance damage = entity.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE);

        if(entity.hasStatusEffect(IOEffects.SOUL_FIRE_STRENGTH.getRef())) {
            StatusEffectInstance current = entity.getStatusEffect(IOEffects.SOUL_FIRE_STRENGTH.getRef());
            if(current.getAmplifier() < amplifier) entity.removeStatusEffect(IOEffects.SOUL_FIRE_STRENGTH.getRef());
        }

        EntityAttributeModifier modifier = new EntityAttributeModifier(ID, Math.min(0.5D + (double) amplifier / 2.0D, 1.0D), EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

        if(damage != null && damage.getModifier(ID) == null) damage.addTemporaryModifier(modifier);

        if(amplifier >= 1 && entity instanceof IbarnOriginsEntity ioe) {
            ioe.setOnSoulFire(true);
        }
    }

    @Override
    public void onStatusEffectRemoved(ServerWorld world, LivingEntity entity, int amplifier) {
        EntityAttributeInstance damage = entity.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE);
        if (damage != null && damage.getModifier(ID) != null)
            damage.removeModifier(ID);

        if(amplifier >= 1 && entity instanceof IbarnOriginsEntity ioe) {
            ioe.setOnSoulFire(false);
        }
    }
}
