package com.ibarnstormer.ibarnorigins.registry;

import com.ibarnstormer.ibarnorigins.IbarnOriginsMain;
import com.ibarnstormer.ibarnorigins.entity.HomingWitherSkullEntity;
import com.ibarnstormer.ibarnorigins.entity.SoulFireBallEntity;
import com.ibarnstormer.ibarnorigins.registry.utils.IORegisterWrapper;
import com.ibarnstormer.ibarnorigins.registry.utils.RegistryObjectWrapper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class IOEntities {

    //Identifiers
    public static ResourceLocation SOUL_FB_ID = IbarnOriginsMain.IOIdentifier("soul_fire_ball");
    public static ResourceLocation FIXED_WS_ID = IbarnOriginsMain.IOIdentifier("homing_wither_skull");

    public static RegistryObjectWrapper<EntityType<SoulFireBallEntity>> SOUL_FIRE_BALL_ENTITY;
    public static RegistryObjectWrapper<EntityType<HomingWitherSkullEntity>> HOMING_WITHER_SKULL_ENTITY;

    @SuppressWarnings("unchecked")
    public static void init(IORegisterWrapper register) {
        SOUL_FIRE_BALL_ENTITY = register.register(SOUL_FB_ID, () -> EntityType.Builder.<SoulFireBallEntity>of(SoulFireBallEntity::new, MobCategory.MISC).sized(0.325f,0.325f).clientTrackingRange(64).updateInterval(10).fireImmune().build(SOUL_FB_ID.toString()));
        HOMING_WITHER_SKULL_ENTITY = register.register(FIXED_WS_ID, () -> EntityType.Builder.<HomingWitherSkullEntity>of(HomingWitherSkullEntity::new, MobCategory.MISC).sized(0.325f,0.325f).clientTrackingRange(64).updateInterval(10).fireImmune().build(FIXED_WS_ID.toString()));
    }

}
