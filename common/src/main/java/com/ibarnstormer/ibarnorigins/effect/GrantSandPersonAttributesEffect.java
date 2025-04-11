package com.ibarnstormer.ibarnorigins.effect;

import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class GrantSandPersonAttributesEffect extends StatusEffect {

    public GrantSandPersonAttributesEffect() {
        super(StatusEffectCategory.NEUTRAL, 0xffffff);
    }

    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if(entity instanceof IbarnOriginsEntity ioe) ioe.setSandPerson(true);
    }
}
