package com.ibarnstormer.ibarnorigins.registry;

import com.ibarnstormer.ibarnorigins.IbarnOriginsMain;
import com.ibarnstormer.ibarnorigins.entity.HomingWitherSkullEntity;
import com.ibarnstormer.ibarnorigins.entity.SoulFireBallEntity;
import com.ibarnstormer.ibarnorigins.registry.utils.IORegisterWrapper;
import com.ibarnstormer.ibarnorigins.registry.utils.RegistryObjectWrapper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class IOEntities {

    //Identifiers
    public static Identifier SOUL_FB_ID = IbarnOriginsMain.IOIdentifier("soul_fire_ball");
    public static Identifier FIXED_WS_ID = IbarnOriginsMain.IOIdentifier("homing_wither_skull");

    public static RegistryObjectWrapper<EntityType<SoulFireBallEntity>> SOUL_FIRE_BALL_ENTITY;
    public static RegistryObjectWrapper<EntityType<HomingWitherSkullEntity>> HOMING_WITHER_SKULL_ENTITY;

    @SuppressWarnings("unchecked")
    public static void init(IORegisterWrapper register) {
        SOUL_FIRE_BALL_ENTITY = register.register(SOUL_FB_ID, () -> EntityType.Builder.<SoulFireBallEntity>create(SoulFireBallEntity::new, SpawnGroup.MISC).dimensions(0.325f,0.325f).maxTrackingRange(64).trackingTickInterval(10).makeFireImmune().build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, SOUL_FB_ID)));
        HOMING_WITHER_SKULL_ENTITY = register.register(FIXED_WS_ID, () -> EntityType.Builder.<HomingWitherSkullEntity>create(HomingWitherSkullEntity::new, SpawnGroup.MISC).dimensions(0.325f,0.325f).maxTrackingRange(64).trackingTickInterval(10).makeFireImmune().build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, FIXED_WS_ID)));

    }

}
