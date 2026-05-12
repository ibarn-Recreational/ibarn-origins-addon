package com.ibarnstormer.ibarnorigins.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;

public interface IExtendedStatusEffect {

    void onStatusEffectRemoved(ServerWorld world, LivingEntity entity, int amplifier);

}
