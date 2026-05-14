package com.ibarnstormer.ibarnorigins;

import com.ibarnstormer.ibarnorigins.IbarnOriginsMain;
import com.ibarnstormer.ibarnorigins.registry.IOFabricRegisterWrapper;
import com.ibarnstormer.ibarnorigins.registry.IOEffects;
import com.ibarnstormer.ibarnorigins.registry.IOEntities;
import com.ibarnstormer.ibarnorigins.registry.IOParticles;
import com.ibarnstormer.ibarnorigins.registry.IOSounds;
import com.ibarnstormer.ibarnorigins.registry.utils.IORegisterWrapper;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;

public class IbarnOriginsFabric implements ModInitializer {

    public static final IORegisterWrapper<Registry<EntityType<?>>, EntityType<?>> IOEntityRegister = new IOFabricRegisterWrapper<>(BuiltInRegistries.ENTITY_TYPE);
    public static final IORegisterWrapper<Registry<ParticleType<?>>, ParticleType<?>> IOParticleRegister = new IOFabricRegisterWrapper<>(BuiltInRegistries.PARTICLE_TYPE);
    public static final IORegisterWrapper<Registry<MobEffect>, MobEffect> IOStatusEffectRegister = new IOFabricRegisterWrapper<>(BuiltInRegistries.MOB_EFFECT);
    public static final IORegisterWrapper<Registry<SoundEvent>, SoundEvent> IOSoundEventRegister = new IOFabricRegisterWrapper<>(BuiltInRegistries.SOUND_EVENT);


    @Override
    public void onInitialize() {
        // Initialize common things
        IbarnOriginsMain.init();

        // Register everything on Fabric
        IOEntities.init(IOEntityRegister);
        IOParticles.init(IOParticleRegister);
        IOEffects.init(IOStatusEffectRegister);
        IOSounds.init(IOSoundEventRegister);
    }
}
