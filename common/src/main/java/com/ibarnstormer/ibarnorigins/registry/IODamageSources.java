package com.ibarnstormer.ibarnorigins.registry;

import com.ibarnstormer.ibarnorigins.IbarnOriginsMain;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;

public class IODamageSources {

    public static DamageSource entityDamageSource(String id, Entity attacker, World world) {
        RegistryKey<DamageType> type = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, IbarnOriginsMain.IOIdentifier(id));
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(type), attacker);
    }

    public static DamageSource damageSource(String id, World world) {
        RegistryKey<DamageType> type = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, IbarnOriginsMain.IOIdentifier(id));
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(type));
    }

}
