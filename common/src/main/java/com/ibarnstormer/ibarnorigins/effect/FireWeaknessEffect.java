package com.ibarnstormer.ibarnorigins.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class FireWeaknessEffect extends StatusEffect {

    private final EntityAttributeModifier DAMAGE_MODIFIER = new EntityAttributeModifier("fire_weakness", -0.9D, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);

    public FireWeaknessEffect() {
        super(StatusEffectCategory.HARMFUL, 0xffffff);
    }

    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        EntityAttributeInstance damage = entity.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        if(damage != null && damage.hasModifier(DAMAGE_MODIFIER)) damage.removeModifier(DAMAGE_MODIFIER);
    }

    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        EntityAttributeInstance damage = entity.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        if(damage != null) if(!damage.hasModifier(DAMAGE_MODIFIER)) damage.addTemporaryModifier(DAMAGE_MODIFIER);
    }

}
