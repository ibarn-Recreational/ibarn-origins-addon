package com.ibarnstormer.ibarnorigins.forge;

import com.ibarnstormer.ibarnorigins.IbarnOriginsMain;
import com.ibarnstormer.ibarnorigins.forge.registry.IOForgeRegisterWrapper;
import com.ibarnstormer.ibarnorigins.registry.IOEffects;
import com.ibarnstormer.ibarnorigins.registry.IOEntities;
import com.ibarnstormer.ibarnorigins.registry.IOParticles;
import com.ibarnstormer.ibarnorigins.registry.IOSounds;
import com.ibarnstormer.ibarnorigins.registry.utils.IORegisterWrapper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.sound.SoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(IbarnOriginsMain.MODID)
public class IbarnOriginsForge {

    public static final IORegisterWrapper<DeferredRegister<EntityType<?>>, EntityType<?>> IOEntityRegister = new IOForgeRegisterWrapper<>(DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, IbarnOriginsMain.MODID));
    public static final IORegisterWrapper<DeferredRegister<ParticleType<?>>, ParticleType<?>> IOParticleRegister = new IOForgeRegisterWrapper<>(DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, IbarnOriginsMain.MODID));
    public static final IORegisterWrapper<DeferredRegister<StatusEffect>, StatusEffect> IOStatusEffectRegister = new IOForgeRegisterWrapper<>(DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, IbarnOriginsMain.MODID));
    public static final IORegisterWrapper<DeferredRegister<SoundEvent>, SoundEvent> IOSoundEventRegister = new IOForgeRegisterWrapper<>(DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, IbarnOriginsMain.MODID));


    public IbarnOriginsForge() {

        // Register everything on Forge
        IOEntities.init(IOEntityRegister);
        IOEntityRegister.getRegister().register(FMLJavaModLoadingContext.get().getModEventBus());

        IOParticles.init(IOParticleRegister);
        IOParticleRegister.getRegister().register(FMLJavaModLoadingContext.get().getModEventBus());

        IOEffects.init(IOStatusEffectRegister);
        IOStatusEffectRegister.getRegister().register(FMLJavaModLoadingContext.get().getModEventBus());

        IOSounds.init(IOSoundEventRegister);
        IOSoundEventRegister.getRegister().register(FMLJavaModLoadingContext.get().getModEventBus());

        // Submit our event bus to let architectury register our content on the right time

        // Initialize common things
        IbarnOriginsMain.init();


    }
}
