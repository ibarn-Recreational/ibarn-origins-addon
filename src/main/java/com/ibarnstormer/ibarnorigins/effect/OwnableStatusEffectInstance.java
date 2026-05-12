package com.ibarnstormer.ibarnorigins.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class OwnableStatusEffectInstance extends StatusEffectInstance {

    @Nullable
    private final UUID ownerUUID;

    public OwnableStatusEffectInstance(RegistryEntry<StatusEffect> type, int duration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon, UUID ownerUUID) {
        this(type, duration, amplifier, ambient, showParticles, showIcon, ownerUUID, null);
    }

    public OwnableStatusEffectInstance(RegistryEntry<StatusEffect> type, int duration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon, UUID ownerUUID, @Nullable StatusEffectInstance hiddenEffect) {
        super(type, duration, amplifier, ambient, showParticles, showIcon, hiddenEffect);

        this.ownerUUID = ownerUUID;
    }

    public OwnableStatusEffectInstance(RegistryEntry<StatusEffect> type, int duration, int amplifier, boolean ambient, boolean visible, @Nullable UUID ownerUUID) {
        super(type, duration, amplifier, ambient, visible, visible);

        this.ownerUUID = ownerUUID;
    }

    public OwnableStatusEffectInstance(StatusEffectInstance instance, @Nullable UUID ownerUUID) {
        super(instance);

        this.ownerUUID = ownerUUID;
    }

    public @Nullable LivingEntity getOwner(World world) {
        LivingEntity owner = null;
        if (this.ownerUUID != null && world instanceof ServerWorld) {
            Entity entity = world.getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) {
                owner = (LivingEntity)entity;
            }
        }

        return owner;
    }

    @Override
    public boolean update(ServerWorld world, LivingEntity entity, Runnable hiddenEffectCallback) {
        if(this.getEffectType().value().isBeneficial() || entity.getUuid() != this.ownerUUID) {
            return super.update(world, entity, hiddenEffectCallback);
        }
        else return false;
    }


}
