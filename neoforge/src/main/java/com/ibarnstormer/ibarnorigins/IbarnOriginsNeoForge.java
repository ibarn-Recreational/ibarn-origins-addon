package com.ibarnstormer.ibarnorigins;

import com.ibarnstormer.ibarnorigins.registry.*;
import com.ibarnstormer.ibarnorigins.registry.utils.IORegisterWrapper;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;


@Mod(IbarnOriginsMain.MODID)
public class IbarnOriginsNeoForge {

    public static final IORegisterWrapper<DeferredRegister<EntityType<?>>, EntityType<?>> IOEntityRegister = new IONeoForgeRegisterWrapper<>(DeferredRegister.create(Registries.ENTITY_TYPE, IbarnOriginsMain.MODID));
    public static final IORegisterWrapper<DeferredRegister<ParticleType<?>>, ParticleType<?>> IOParticleRegister = new IONeoForgeRegisterWrapper<>(DeferredRegister.create(Registries.PARTICLE_TYPE, IbarnOriginsMain.MODID));
    public static final IORegisterWrapper<DeferredRegister<MobEffect>, MobEffect> IOStatusEffectRegister = new IONeoForgeRegisterWrapper<>(DeferredRegister.create(Registries.MOB_EFFECT, IbarnOriginsMain.MODID));
    public static final IORegisterWrapper<DeferredRegister<SoundEvent>, SoundEvent> IOSoundEventRegister = new IONeoForgeRegisterWrapper<>(DeferredRegister.create(Registries.SOUND_EVENT, IbarnOriginsMain.MODID));


    public IbarnOriginsNeoForge(IEventBus modEventBus, ModContainer modContainer) {

        // Register everything on NeoForge
        IOEntities.init(IOEntityRegister);
        IOEntityRegister.getRegister().register(modEventBus);

        IOParticles.init(IOParticleRegister);
        IOParticleRegister.getRegister().register(modEventBus);

        IOEffects.init(IOStatusEffectRegister);
        IOStatusEffectRegister.getRegister().register(modEventBus);

        IOSounds.init(IOSoundEventRegister);
        IOSoundEventRegister.getRegister().register(modEventBus);

        // Initialize common things
        IbarnOriginsMain.init();


    }
}
