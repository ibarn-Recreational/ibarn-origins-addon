package com.ibarnstormer.ibarnorigins.registry;

import com.ibarnstormer.ibarnorigins.IbarnOriginsMain;
import com.ibarnstormer.ibarnorigins.entity.HomingWitherSkullEntity;
import com.ibarnstormer.ibarnorigins.entity.KiBlastProjectileEntity;
import com.ibarnstormer.ibarnorigins.entity.OwnedAreaEffectCloudEntity;
import com.ibarnstormer.ibarnorigins.entity.SoulFireBallEntity;
import com.ibarnstormer.ibarnorigins.registry.utils.IORegisterWrapper;
import com.ibarnstormer.ibarnorigins.registry.utils.RegistryObjectWrapper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;

public class IOEntities {

    //Identifiers
    public static Identifier SOUL_FB_ID = IbarnOriginsMain.IOIdentifier("soul_fire_ball");
    public static Identifier FIXED_WS_ID = IbarnOriginsMain.IOIdentifier("homing_wither_skull");
    public static Identifier KI_BLAST_ID = IbarnOriginsMain.IOIdentifier("ki_blast_projectile");
    public static Identifier OAECE_ID = IbarnOriginsMain.IOIdentifier("owned_area_effect_cloud");

    public static RegistryObjectWrapper<EntityType<SoulFireBallEntity>> SOUL_FIRE_BALL_ENTITY;
    public static RegistryObjectWrapper<EntityType<HomingWitherSkullEntity>> HOMING_WITHER_SKULL_ENTITY;
    public static RegistryObjectWrapper<EntityType<KiBlastProjectileEntity>> KI_BLAST_PROJECTILE_ENTITY;
    public static RegistryObjectWrapper<EntityType<OwnedAreaEffectCloudEntity>> OWNED_AREA_EFFECT_CLOUD_ENTITY;

    @SuppressWarnings("unchecked")
    public static void init(IORegisterWrapper register) {
        SOUL_FIRE_BALL_ENTITY = register.register(SOUL_FB_ID, () -> EntityType.Builder.<SoulFireBallEntity>create(SoulFireBallEntity::new, SpawnGroup.MISC).setDimensions(0.325f,0.325f).maxTrackingRange(64).trackingTickInterval(10).makeFireImmune().build(SOUL_FB_ID.toString()));
        HOMING_WITHER_SKULL_ENTITY = register.register(FIXED_WS_ID, () -> EntityType.Builder.<HomingWitherSkullEntity>create(HomingWitherSkullEntity::new, SpawnGroup.MISC).setDimensions(0.325f,0.325f).maxTrackingRange(64).trackingTickInterval(10).makeFireImmune().build(FIXED_WS_ID.toString()));
        KI_BLAST_PROJECTILE_ENTITY = register.register(KI_BLAST_ID, () -> EntityType.Builder.<KiBlastProjectileEntity>create(KiBlastProjectileEntity::new, SpawnGroup.MISC).setDimensions(0.325f,0.325f).maxTrackingRange(64).trackingTickInterval(10).makeFireImmune().build(KI_BLAST_ID.toString()));
        OWNED_AREA_EFFECT_CLOUD_ENTITY = register.register(OAECE_ID, () -> EntityType.Builder.<OwnedAreaEffectCloudEntity>create(OwnedAreaEffectCloudEntity::new, SpawnGroup.MISC).setDimensions(6.0F, 0.5F).maxTrackingRange(10).trackingTickInterval(Integer.MAX_VALUE).makeFireImmune().build(OAECE_ID.toString()));
    }

}
