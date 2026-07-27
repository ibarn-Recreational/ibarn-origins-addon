package com.ibarnstormer.ibarnorigins.effect;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class OwnableStatusEffectInstance extends MobEffectInstance {

    @Nullable
    private final UUID ownerUUID;

    public OwnableStatusEffectInstance(Holder<MobEffect> type, int duration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon, UUID ownerUUID) {
        this(type, duration, amplifier, ambient, showParticles, showIcon, ownerUUID, null);
    }

    public OwnableStatusEffectInstance(Holder<MobEffect> type, int duration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon, UUID ownerUUID, @Nullable MobEffectInstance hiddenEffect) {
        super(type, duration, amplifier, ambient, showParticles, showIcon, hiddenEffect);

        this.ownerUUID = ownerUUID;
    }

    public OwnableStatusEffectInstance(Holder<MobEffect> type, int duration, int amplifier, boolean ambient, boolean visible, @Nullable UUID ownerUUID) {
        super(type, duration, amplifier, ambient, visible, visible);

        this.ownerUUID = ownerUUID;
    }

    public OwnableStatusEffectInstance(MobEffectInstance instance, @Nullable UUID ownerUUID) {
        super(instance);

        this.ownerUUID = ownerUUID;
    }

    public @Nullable LivingEntity getOwner(Level world) {
        LivingEntity owner = null;
        if (this.ownerUUID != null && world instanceof ServerLevel) {
            Entity entity = ((ServerLevel) world).getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) {
                owner = (LivingEntity)entity;
            }
        }

        return owner;
    }


}
