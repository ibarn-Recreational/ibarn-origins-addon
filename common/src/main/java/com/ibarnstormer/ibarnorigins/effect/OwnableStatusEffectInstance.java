package com.ibarnstormer.ibarnorigins.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class OwnableStatusEffectInstance extends StatusEffectInstance {

    @Nullable
    private final UUID ownerUUID;

    public OwnableStatusEffectInstance(StatusEffect type, int duration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon, UUID ownerUUID) {
        this(type, duration, amplifier, ambient, showParticles, showIcon, ownerUUID, null, type.getFactorCalculationDataSupplier());
    }

    public OwnableStatusEffectInstance(StatusEffect type, int duration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon, UUID ownerUUID, @Nullable StatusEffectInstance hiddenEffect, Optional<FactorCalculationData> factorCalculationData) {
        super(type, duration, amplifier, ambient, showParticles, showIcon, hiddenEffect, factorCalculationData);

        this.ownerUUID = ownerUUID;
    }

    public OwnableStatusEffectInstance(StatusEffect type, int duration, int amplifier, boolean ambient, boolean visible, @Nullable UUID ownerUUID) {
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
            Entity entity = ((ServerWorld)world).getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) {
                owner = (LivingEntity)entity;
            }
        }

        return owner;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity) {
        if(this.getEffectType().isBeneficial() || entity.getUuid() != this.ownerUUID) {
            super.applyUpdateEffect(entity);
        }
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        if (this.ownerUUID != null) {
            nbt.putUuid("ownerUUID", this.ownerUUID);
        }

        return nbt;
    }

    public static @NotNull OwnableStatusEffectInstance fromNbt(NbtCompound nbt) {
        StatusEffectInstance instance = StatusEffectInstance.fromNbt(nbt);

        UUID uuid = null;
        if(nbt.contains("OwnerUUID")) {
            uuid = nbt.getUuid("OwnerUUID");
        }

        return new OwnableStatusEffectInstance(instance, uuid);
    }


}
