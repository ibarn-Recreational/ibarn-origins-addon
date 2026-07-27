package com.ibarnstormer.ibarnorigins.registry;

import com.ibarnstormer.ibarnorigins.IbarnOriginsMain;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class IODamageSources {

    public static DamageSource entityDamageSource(String id, Entity attacker, Level world) {
        ResourceKey<DamageType> type = ResourceKey.create(Registries.DAMAGE_TYPE, IbarnOriginsMain.IOIdentifier(id));
        return new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(type), attacker);
    }

    public static DamageSource damageSource(String id, Level world) {
        ResourceKey<DamageType> type = ResourceKey.create(Registries.DAMAGE_TYPE, IbarnOriginsMain.IOIdentifier(id));
        return new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(type));
    }

}
