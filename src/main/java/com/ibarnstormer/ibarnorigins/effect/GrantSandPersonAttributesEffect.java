package com.ibarnstormer.ibarnorigins.effect;

import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class GrantSandPersonAttributesEffect extends MobEffect {

    public GrantSandPersonAttributesEffect() {
        super(MobEffectCategory.NEUTRAL, 0xffffff);
    }

    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {
        if(entity instanceof IbarnOriginsEntity ioe) ioe.setSandPerson(true);
    }
}
