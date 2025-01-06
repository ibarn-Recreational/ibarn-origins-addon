package com.ibarnstormer.ibarnorigins.fabric;

import com.ibarnstormer.ibarnorigins.IbarnOriginsMain;
import com.ibarnstormer.ibarnorigins.fabric.registry.IOFabricRegisterWrapper;
import com.ibarnstormer.ibarnorigins.registry.IOEffects;
import com.ibarnstormer.ibarnorigins.registry.IOEntities;
import com.ibarnstormer.ibarnorigins.registry.IOParticles;
import com.ibarnstormer.ibarnorigins.registry.IOSounds;
import com.ibarnstormer.ibarnorigins.registry.utils.IORegisterWrapper;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;

public class IbarnOriginsFabric implements ModInitializer {

    public static final IORegisterWrapper<Registry<EntityType<?>>, EntityType<?>> IOEntityRegister = new IOFabricRegisterWrapper<>(Registries.ENTITY_TYPE);
    public static final IORegisterWrapper<Registry<ParticleType<?>>, ParticleType<?>> IOParticleRegister = new IOFabricRegisterWrapper<>(Registries.PARTICLE_TYPE);
    public static final IORegisterWrapper<Registry<StatusEffect>, StatusEffect> IOStatusEffectRegister = new IOFabricRegisterWrapper<>(Registries.STATUS_EFFECT);
    public static final IORegisterWrapper<Registry<SoundEvent>, SoundEvent> IOSoundEventRegister = new IOFabricRegisterWrapper<>(Registries.SOUND_EVENT);


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
