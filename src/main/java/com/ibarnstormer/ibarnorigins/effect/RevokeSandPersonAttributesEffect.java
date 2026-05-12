package com.ibarnstormer.ibarnorigins.effect;

import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class RevokeSandPersonAttributesEffect extends StatusEffect {

    public RevokeSandPersonAttributesEffect() {
        super(StatusEffectCategory.NEUTRAL, 0xffffff);
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        if(entity instanceof IbarnOriginsEntity ioe) ioe.setSandPerson(false);
    }
}
