package com.ibarnstormer.ibarnorigins.effect;

import com.ibarnstormer.ibarnorigins.registry.IOEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;

import java.util.UUID;

public class SoulFireStrengthEffect extends StatusEffect {

    private final UUID SOUL_FIRE_STRENGTH_UUID = UUID.fromString("d1bf33ec-a9da-4ccf-aa95-1401ee6a1dae");

    public SoulFireStrengthEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0x1f70f2);
    }

    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        EntityAttributeInstance damage = entity.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);

        if(entity.hasStatusEffect(IOEffects.SOUL_FIRE_STRENGTH.get())) {
            StatusEffectInstance current = entity.getStatusEffect(IOEffects.SOUL_FIRE_STRENGTH.get());
            if(current.getAmplifier() < amplifier) entity.removeStatusEffect(IOEffects.SOUL_FIRE_STRENGTH.get());
        }

        EntityAttributeModifier modifier = new EntityAttributeModifier(SOUL_FIRE_STRENGTH_UUID, "soul_fire_strength", Math.min(0.5D + (double) amplifier / 2.0D, 1.0D), EntityAttributeModifier.Operation.MULTIPLY_TOTAL);

        if(damage != null && damage.getModifier(SOUL_FIRE_STRENGTH_UUID) == null) damage.addTemporaryModifier(modifier);
    }

    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        EntityAttributeInstance damage = entity.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        if (damage != null && damage.getModifier(SOUL_FIRE_STRENGTH_UUID) != null)
            damage.removeModifier(SOUL_FIRE_STRENGTH_UUID);
    }

}
