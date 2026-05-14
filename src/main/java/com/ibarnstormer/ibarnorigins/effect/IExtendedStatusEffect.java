package com.ibarnstormer.ibarnorigins.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public interface IExtendedStatusEffect {

    void onStatusEffectRemoved(ServerLevel world, LivingEntity entity, int amplifier);

}
